package com.bestroboticsteam.warehouseinterface;

import org.apache.log4j.Logger;

import com.bestroboticsteam.robotsmanagement.RobotInfo;
import lejos.robotics.navigation.Pose;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPilot;
import rp.robotics.navigation.GridPose;
import rp.robotics.simulation.MovableRobot;
import rp.systems.StoppableRunnable;

public class RobotSimController implements StoppableRunnable {
	final static Logger logger = Logger.getLogger(CreateSimRobots.class);
	private GridMap map;
	private GridPilot pilot;
	private RobotInfo robotInfo;
	private MovableRobot robot;

	public RobotSimController(MovableRobot robot, GridMap map, GridPose start, RobotInfo robotInfo) {
		this.map = map;
		this.pilot = new GridPilot(robot.getPilot(), map, start);
		this.robot = robot;
		this.robotInfo = robotInfo;
	}

	@Override
	public void run() {
		logger.info("running robot " + robotInfo.getName());
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
						pilot.rotateNegative();
						pilot.rotateNegative();
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
					pilot.rotateNegative();
					pilot.rotateNegative();
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
	public int getPosX() {
		Pose pos = robot.getPose();
		int a = (int) pos.getX();
		int b = (int) (a * 3.67);
		return b;
	}

	public int getPosY() {
		Pose pos = robot.getPose();
		int a = (int) pos.getY();
		int b = (int) (a * 3.67);
		return b;
	}
}