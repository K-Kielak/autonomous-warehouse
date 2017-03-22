package com.bestroboticsteam.warehouseinterface;

import org.apache.log4j.Logger;

import lejos.robotics.navigation.Pose;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPilot;
import rp.robotics.navigation.GridPose;
import rp.robotics.simulation.MovableRobot;

public class RobotSimController extends Thread {
	final static Logger logger = Logger.getLogger(RobotSimController.class);
	private GridMap map;
	private GridPilot pilot;
	private int theRobot;
	private MovableRobot robot;
	private float posx;
	private float posy;
	private Pose position = new Pose();

	public RobotSimController(MovableRobot robot, GridMap map, GridPose start, int theRobot) {
		this.map = map;
		this.pilot = new GridPilot(robot.getPilot(), map, start);
		this.robot = robot;
		this.theRobot = theRobot;
	}

	public void run() {
		while (true) {
			float posForSimY = 0.0f;
			float posForSimX = 0.0f;
			posy = CreateSimRobots.getPosY(theRobot);
			posx = CreateSimRobots.getPosX(theRobot);
			if (posy != simY() || posx != simX()) {
				posx = 5.0f;
				posy = 7.0f;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					logger.error("RobotSim thread has been interrupted");
				}
				if (posy == 0) {
					posForSimY = 0.155f;
				} else if (posy == 1) {
					posForSimY = 0.455f;
				} else if (posy == 2) {
					posForSimY = 0.755f;
				} else if (posy == 3) {
					posForSimY = 1.055f;
				} else if (posy == 4) {
					posForSimY = 1.355f;
				} else if (posy == 5) {
					posForSimY = 1.655f;
				} else if (posy == 6) {
					posForSimY = 1.955f;
				} else if (posy == 7) {
					posForSimY = 2.255f;
				}

				if (posx == 0) {
					posForSimX = 0.17f;
				} else if (posx == 1) {
					posForSimX = 0.47f;
				} else if (posx == 2) {
					posForSimX = 0.77f;
				} else if (posx == 3) {
					posForSimX = 1.07f;
				} else if (posx == 4) {
					posForSimX = 1.37f;
				} else if (posx == 5) {
					posForSimX = 1.67f;
				} else if (posx == 6) {
					posForSimX = 1.97f;
				} else if (posx == 7) {
					posForSimX = 2.27f;
				}
				position.setLocation(posForSimX, posForSimY);
				robot.setPose(position);
			}
		}
	}

	private float simX() {
		float pos = robot.getPose().getX();
		return pos;
	}

	private float simY() {
		float pos = robot.getPose().getY();
		return pos;
	}
}