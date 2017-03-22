package com.bestroboticsteam.warehouseinterface;

import org.apache.log4j.Logger;

import lejos.geom.Point;
import lejos.robotics.navigation.Pose;
import lejos.util.Delay;
import rp.robotics.mapping.GridMap;
import rp.robotics.navigation.GridPilot;
import rp.robotics.navigation.GridPose;
import rp.robotics.navigation.Heading;
import rp.robotics.simulation.MovableRobot;

public class RobotSimController extends Thread {
	final static Logger logger = Logger.getLogger(RobotSimController.class);
	private GridMap map;
	private GridPilot pilot;
	private int theRobot;
	private MovableRobot robot;
	private int posx;
	private int posy;

	public RobotSimController(MovableRobot robot, GridMap map, GridPose start, int theRobot) {
		this.map = map;
		this.pilot = new GridPilot(robot.getPilot(), map, start);
		this.robot = robot;
		this.theRobot = theRobot;
	}

	public void run() {
		boolean ypos = false;
		boolean xpos = false;
		while (true) {
			while (!ypos) {
				int county = 0;
				posx = CreateSimRobots.getPosX(theRobot);
				posy = CreateSimRobots.getPosY(theRobot);
				while (!xpos) {
					int countx1 = 0;
					int countx2 = 0;
					logger.debug("actualx " + posx);
					logger.debug("simx " + simX());
					int xDifference = posx - simX();
					logger.debug("xdiff " + xDifference);
					if (xDifference > 0) {
						// move right
						if (countx2 == 0) {
							pilot.rotatePositive();
						}
						logger.debug("moving right");
						pilot.moveForward();
						countx2++;

					} else if (xDifference < 0) {
						// move left
						logger.debug("moving left");
						if (countx1 == 0) {
							pilot.rotateNegative();
						}
						pilot.moveForward();
						countx1++;
					} else {
						xpos = true;
					}
					countx1 = 0;
					countx2 = 0;
				}
				logger.debug("actualy " + posy);
				logger.debug("simy " + simY());
				int yDifference = posy - simY();
				logger.debug(yDifference + " " + yDifference);
				if (yDifference > 0) {
					// move forward
					logger.debug("move forward");
					pilot.moveForward();

				} else if (yDifference < 0) {
					// move backward
					logger.debug("move backward");
					if (county == 0) {
						pilot.rotatePositive();
						pilot.rotatePositive();
					}
					pilot.moveForward();
					county++;
				} else {
					ypos = true;
				}
			}
			Delay.msDelay(1000);
			ypos = false;
			xpos = false;
		}
	}

	private int simX() {
		int pos = (int) robot.getPose().getX();
		if (pos != 0) {
			pos = (int) (pos + 8.33);
		}
		return pos;
	}

	private int simY() {
		int pos = (int) robot.getPose().getY();
		if (pos != 0) {
			pos = (int) (pos + 5.56);
		}
		return pos;
	}
}