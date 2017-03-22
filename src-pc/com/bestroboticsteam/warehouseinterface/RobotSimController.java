package com.bestroboticsteam.warehouseinterface;

import org.apache.log4j.Logger;

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
			int county1 = 0;
			posy = CreateSimRobots.getPosY(theRobot);
			posx = CreateSimRobots.getPosX(theRobot);
			while (!ypos) {
			//	System.out.println("HEEEEEEERE");
			//	logger.info("actualy " + posy);
			//	logger.info("simy " + simY());
				int yDifference = posy - simY();
			//	logger.info("ydiff " + yDifference);
				if (yDifference > 0) {
					logger.debug("move forward ");
					for (int i = 0; i < yDifference; i++) {
						pilot.moveForward();
					}
					county1++;
					ypos = true;
				} else if (yDifference < 0) {
					logger.debug("move back ");
					if (county1 == 0) {
						pilot.rotateNegative();
						pilot.rotateNegative();
					}
					for (int i = 0; i < yDifference; i++) {
						pilot.moveForward();
					}
					county1++;
					ypos = true;
				} else {
					ypos = true;
				}
			}
			int countx1 = 0;
			int countx2 = 0;
			while (!xpos) {
				logger.debug("actualx " + posx);
				logger.debug("simx " + simX());
				int xDifference = posx - simX();
				logger.info("xdiff " + xDifference);
				if (xDifference > 0) {
					logger.debug("move right ");
					if (countx1 == 0) {
						pilot.rotateNegative();
					}
					for (int i = 0; i < xDifference; i++) {
						pilot.moveForward();
					}
					countx1++;
					xpos = true;
				} else if (xDifference < 0) {
					logger.debug("move left ");
					if (countx2 == 0) {
						pilot.rotatePositive();
					}
					for (int i = 0; i < xDifference; i++) {
						pilot.moveForward();
					}
					countx2++;
					xpos = true;
				} else {
					xpos = true;
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				logger.error("thread has been interrupted");
			}
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