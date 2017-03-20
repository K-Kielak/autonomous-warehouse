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
		while (true) {
			int xDifference = (actualRobotX() - getPosX());
			int yDifference = (actualRobotY() - getPosY());
			if (yDifference > 0) {
				for (int i = 0; i < yDifference; i++) {
					pilot.moveForward();
				}
			} else if (yDifference < 0) {
				pilot.rotatePositive();
				pilot.rotatePositive();
				for (int i = 0; i < yDifference; i++) {
					pilot.moveForward();
				}
			} else {
				break;
			}
			if (xDifference > 0) {
				pilot.rotateNegative();
				for (int i = 0; i < xDifference; i++) {
					pilot.moveForward();
				}
			} else if (xDifference < 0) {
				pilot.rotatePositive();
				for (int i = 0; i < xDifference; i++) {
					pilot.moveForward();
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
		int actual = (int) pos.getX();
		int scaled = (int) (actual * 3.67);
		return scaled;
	}

	public int getPosY() {
		Pose pos = robot.getPose();
		int actual = (int) pos.getY();
		int scaled = (int) (actual * 3.67);
		return scaled;
	}
}