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
		while (true) {
				posx = CreateSimRobots.getPosX(theRobot);
				posy = CreateSimRobots.getPosY(theRobot);
				GridPose position = new  GridPose(posx, posy, Heading.PLUS_Y);
				pilot.setGridPose(position);
				Delay.msDelay(2000);
		}
	}
}