package com.bestroboticsteam.localization;

import java.awt.Point;

import com.bestroboticsteam.robotsmanagement.Direction;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;

public class Localization {

	private double[][][] poseLikelihoods;
	private GridMap map;
	Direction nextMovement;

	public Localization() {
		map = MapUtils.createRealWarehouse();
		// Initialise all poses as equally likely
		for (int i = 0; i < poseLikelihoods.length; i++) {
			for (int j = 0; j < poseLikelihoods[i].length; j++) {
				for (int k = 0; k < poseLikelihoods[i].length; k++) {
					poseLikelihoods[i][j][k] = 1 / (poseLikelihoods.length * poseLikelihoods[0].length);
				}
			}
		}
	}

	public void step(float range) {
		switch (nextMovement) {
		case FORWARD:
			for (int i = 0; i < poseLikelihoods.length - 1; i++) {
				// Move all likelihoods upwards
				poseLikelihoods[i] = poseLikelihoods[i + 1];
			}
			break;
		case RIGHT:
			for (int i = 0; i < poseLikelihoods.length; i++) {
				for (int j = poseLikelihoods.length - 1; j > 0; j--) {
					// Move all likelihoods to the right
					poseLikelihoods[i][j] = poseLikelihoods[i][j - 1];
				}
			}
			break;
		case BACKWARD:
			for (int i = poseLikelihoods.length - 1; i > 0; i--) {
				// Move all likelihoods downwards
				poseLikelihoods[i] = poseLikelihoods[i - 1];
			}
			break;
		case LEFT:
			for (int i = 0; i < poseLikelihoods.length; i++) {
				for (int j = 0; j < poseLikelihoods.length - 1; j++) {
					// Move all likelihoods to the left
					poseLikelihoods[i][j] = poseLikelihoods[i][j + 1];
				}
			}
			break;
		default: // null

			break;
		}
		nextMovement = Direction.FORWARD;
	}

	public PoseLikelihood mostLikelyPose() {
		double maxLikelihood = 0;
		PoseLikelihood mostLikelyPose = null;
		for (int i = 0; i < poseLikelihoods.length; i++) {
			for (int j = 0; j < poseLikelihoods[i].length; j++) {
				for (int k = 0; k < poseLikelihoods[i].length; k++) {
					if (poseLikelihoods[i][j][k] > maxLikelihood) {
						maxLikelihood = poseLikelihoods[i][j][k];
						switch (k) {
						case 0:
							mostLikelyPose = new PoseLikelihood(poseLikelihoods[i][j][k], new Point(i, j),
									Direction.FORWARD);
							break;
						case 1:
							mostLikelyPose = new PoseLikelihood(poseLikelihoods[i][j][k], new Point(i, j),
									Direction.RIGHT);
							break;
						case 2:
							mostLikelyPose = new PoseLikelihood(poseLikelihoods[i][j][k], new Point(i, j),
									Direction.BACKWARD);
							break;
						default:
							mostLikelyPose = new PoseLikelihood(poseLikelihoods[i][j][k], new Point(i, j),
									Direction.LEFT);
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
		for (double[][] row : poseLikelihoods) {
			for (double[] position : row) {
				for (double poseLikelihood : position) {
					sum += poseLikelihood;
				}
			}
		}
		double normalizationFactor = sum / (poseLikelihoods.length * poseLikelihoods[0].length);
		for (int i = 0; i < poseLikelihoods.length; i++) {
			for (int j = 0; j < poseLikelihoods[i].length; j++) {
				for (int k = 0; k < poseLikelihoods[i].length; k++) {
					poseLikelihoods[i][j][k] = poseLikelihoods[i][j][k] / normalizationFactor;
				}
			}
		}
	}

}
