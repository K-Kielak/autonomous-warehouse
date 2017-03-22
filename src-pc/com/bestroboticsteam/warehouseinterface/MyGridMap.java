package com.bestroboticsteam.warehouseinterface;

import java.util.ArrayList;
import lejos.geom.Line;
import lejos.geom.Rectangle;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.Pose;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.LineMap;

public class MyGridMap {

	public static GridMap createRealWarehouse() {
		float height = 2.44f;
		float width = 3.67f;
		int accheight = 7;
		int accwidth = 11;
		float xInset = 0.17f, yInstet = 0.155f;
		int gridWitdth = 12, gridHeight = 8;
		float cellSize = 0.30f;
		int acccellsize = 1;

		// First ins 36 39 56 188

		ArrayList<Line> lines = new ArrayList<Line>();

		// these are the walls for the world outline
		lines.add(new Line(0f, 0f, width, 0f));
		lines.add(new Line(width, 0f, width, height));
		lines.add(new Line(width, height, 0f, height));
		lines.add(new Line(0f, height, 0f, 0f));

		lines.add(new Line(0.31f, 0.29f, 0.31f, height - 0.61f));
		lines.add(new Line(0.61f, 0.29f, 0.61f, height - 0.61f));
		lines.add(new Line(0.31f, 0.29f, 0.61f, 0.29f));
		lines.add(new Line(0.31f, height - 0.61f, 0.61f, height - 0.61f));

		lines.add(new Line(1.24f, 0.28f, 1.24f, height - 0.62f));
		lines.add(new Line(1.54f, 0.28f, 1.54f, height - 0.62f));
		lines.add(new Line(1.24f, 0.28f, 1.54f, 0.28f));
		lines.add(new Line(1.24f, height - 0.62f, 1.54f, height - 0.62f));

		lines.add(new Line(2.14f, 0.29f, 2.14f, height - 0.61f));
		lines.add(new Line(2.44f, 0.29f, 2.44f, height - 0.61f));
		lines.add(new Line(2.14f, 0.29f, 2.44f, 0.29f));
		lines.add(new Line(2.14f, height - 0.61f, 2.44f, height - 0.61f));

		lines.add(new Line(3.08f, 0.30f, 3.08f, height - 0.60f));
		lines.add(new Line(3.38f, 0.30f, 3.38f, height - 0.60f));
		lines.add(new Line(3.08f, 0.30f, 3.38f, 0.30f));
		lines.add(new Line(3.08f, height - 0.60f, 3.38f, height - 0.60f));

		Line[] lineArray = new Line[lines.size()];

		lines.toArray(lineArray);

		return new GridMap(gridWitdth, gridHeight, xInset, yInstet, cellSize,
				new LineMap(lineArray, new Rectangle(0, 0, width, height)));
	}

	public static String toString(Pose _pose) {
		StringBuilder sb = new StringBuilder("Pose: ");
		sb.append(_pose.getX());
		sb.append(", ");
		sb.append(_pose.getY());
		sb.append(", ");
		sb.append(_pose.getHeading());
		return sb.toString();
	}

	/**
	 * Calculate the change in X coordinate to pose from move.
	 * 
	 * @param _previousPose
	 * @param _move
	 * @return
	 */
	public static float changeInX(Pose _previousPose, Move _move) {
		return (_move.getDistanceTraveled() * ((float) Math.cos(Math
				.toRadians(_previousPose.getHeading()))));
	}

	/**
	 * Calculate the change in Y coordinate to pose from move.
	 * 
	 * @param _previousPose
	 * @param _move
	 * @return
	 */
	public static float changeInY(Pose _previousPose, Move _move) {
		return (_move.getDistanceTraveled() * ((float) Math.sin(Math
				.toRadians(_previousPose.getHeading()))));
	}
}
