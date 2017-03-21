package com.bestroboticsteam.localization;

import java.awt.Point;

import javax.swing.text.html.HTMLDocument.HTMLReader.ParagraphAction;

import com.bestroboticsteam.robotsmanagement.Direction;

import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RangeFinder;
import lejos.robotics.navigation.Pose;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.util.Pair;

public class Localization {

	private double[][][] poseLikelyhoods;
	private GridMap map;
	Direction nextMovement;

	public Localization() {
		map = MapUtils.createRealWarehouse();
	}

	
	public void step(float range){
		
		nextMovement = Direction.FORWARD;
	}
	
	public PoseLikelyhood mostLikelyPose() {
		double maxLikelyhood = 0;
		PoseLikelyhood mostLikelyPose = null;
		for (int i = 0; i < poseLikelyhoods.length; i++) {
			for (int j = 0; j < poseLikelyhoods[i].length; j++) {
				for (int k = 0; k < poseLikelyhoods[i].length; k++) {
					if (poseLikelyhoods[i][j][k] > maxLikelyhood) {
						maxLikelyhood = poseLikelyhoods[i][j][k];
						switch (k) {
						case 0:
							mostLikelyPose = new PoseLikelyhood(poseLikelyhoods[i][j][k], new Point(i, j), Direction.FORWARD);
							break;
						case 1:
							mostLikelyPose = new PoseLikelyhood(poseLikelyhoods[i][j][k], new Point(i, j), Direction.RIGHT);
							break;
						case 2:
							mostLikelyPose = new PoseLikelyhood(poseLikelyhoods[i][j][k], new Point(i, j), Direction.BACKWARD);
							break;
						default:
							mostLikelyPose = new PoseLikelyhood(poseLikelyhoods[i][j][k], new Point(i, j), Direction.LEFT);
							break;
						}
					}
				}
			}
		}
		return mostLikelyPose;
	}

	private void normalizeLocations() {
		double sum = 0;
		for (double[][] row : poseLikelyhoods) {
			for (double[] position : row) {
				for (double poseLikelyhood : position) {
					sum += poseLikelyhood;
				}
			}
		}
		double normalizationFactor = sum / (poseLikelyhoods.length * poseLikelyhoods[0].length);
		for (int i = 0; i < poseLikelyhoods.length; i++) {
			for (int j = 0; j < poseLikelyhoods[i].length; j++) {
				for (int k = 0; k < poseLikelyhoods[i].length; k++) {
					poseLikelyhoods[i][j][k] = poseLikelyhoods[i][j][k] / normalizationFactor;
				}
			}
		}
	}

}
