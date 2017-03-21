package com.bestroboticsteam.localization;

import java.awt.Point;

import com.bestroboticsteam.robotsmanagement.Direction;

public class PoseLikelihood {

	public final double likelyhood;
	public final Point location;
	public final Direction direction;
	
	public PoseLikelihood(double likelyhood, Point location, Direction direction) {
		this.likelyhood = likelyhood;
		this.location = location;
		this.direction = direction;
	}
	
}
