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
	final static Logger logger = Logger.getLogger(RobotSimController.class);
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
		boolean ypos = false;
		boolean xpos = false;
		while (true) {
			while (!ypos) {
				int yDifference = robotY() - simY();
				while (!xpos) {
					int xDifference = robotX() - simX();
					if (xDifference > 0) {
						// move left
						logger.debug("moving left");
						for (int i = 0; i < xDifference; i++) {
							pilot.moveForward();
						}
					} else if (xDifference < 0) {
						// move right
						logger.debug("moving right");
						pilot.rotateNegative();
						for (int i = 0; i < xDifference; i++) {
							pilot.moveForward();
						}
					}
					xpos = true;
				}
				if (yDifference > 0) {
					// move forward
					logger.debug("move forward");
					for (int i = 0; i < yDifference; i++) {
						pilot.moveForward();
					}
				} else if (yDifference < 0) {
					// move backward
					logger.debug("move backward");
					pilot.rotatePositive();
					pilot.rotatePositive();
					for (int i = 0; i < yDifference; i++) {
						pilot.moveForward();
					}
				}
				xpos = true;
			}
			ypos = false;
			xpos = false;
		}
	}

	private int simY() {
		// TODO Auto-generated method stub
		int yPos = (int) robot.getPose().getY();
		logger.debug(yPos);
		return yPos;
	}

	private int simX() {
		int xPos = (int) robot.getPose().getX();
		logger.debug(xPos);
		return xPos;
	}

	private int robotY() {
		int yPos = robotInfo.getPosition().x;
		yPos = 6;
		logger.debug(yPos);
		return yPos;
	}

	private int robotX() {
		int xPos = robotInfo.getPosition().x;
		logger.debug(xPos);
		xPos = 1;
		return xPos;
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}
}