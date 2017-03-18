package com.bestroboticsteam.warehouseinterface;

import com.bestroboticsteam.robotsmanagement.RobotInfo;
import lejos.robotics.navigation.Pose;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPilot;
import rp.robotics.navigation.GridPose;
import rp.robotics.simulation.MovableRobot;
import rp.systems.StoppableRunnable;

public class RobotSimController implements StoppableRunnable {
	private GridMap map;
	private GridPilot pilot;
	private RobotInfo robotInfo;
	private static MovableRobot robot;

	public RobotSimController(MovableRobot robot, GridMap map, GridPose start, RobotInfo robotInfo) {
		this.map = map;
		this.pilot = new GridPilot(robot.getPilot(), map, start);
		this.robot = robot;
		this.robotInfo = robotInfo;
	}

	@Override
	public void run() {
		boolean inPositionY = false;
		boolean inPositionX = false;
		while (!inPositionX) {
			int xDifference = (actualRobotX() - getPosX());
			while (!inPositionY) {
				int yDifference = (actualRobotY() - getPosY());
				if (yDifference > 0) {
					for (int i = 0; i < yDifference; i++) {
						pilot.moveForward();
						inPositionY = true;
					}
				} else if (yDifference < 0) {
					for (int i = 0; i < yDifference; i++) {
						pilot.moveForward();
						inPositionY = true;
					}
				} else {
					break;
				}
			}
			if (xDifference > 0) {
				pilot.rotateNegative();
				for (int i = 0; i < xDifference; i++) {
					pilot.moveForward();
					inPositionX = true;
				}
			} else if (xDifference < 0) {
				pilot.rotatePositive();
				for (int i = 0; i < xDifference; i++) {
					pilot.moveForward();
					inPositionX = true;
				}
			}
		}
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	// the actual robot
	public int actualRobotX() {
		return robotInfo.getPosition().x;
	}

	public int actualRobotY() {
		return robotInfo.getPosition().y;
	}

	// visual robot
	public static int getPosX() {
		Pose pos = robot.getPose();
		return (int) pos.getX();
	}

	public static int getPosY() {
		Pose pos = robot.getPose();
		return (int) pos.getY();
	}
}