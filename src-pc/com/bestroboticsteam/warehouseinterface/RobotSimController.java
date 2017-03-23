package com.bestroboticsteam.warehouseinterface;

import java.awt.Point;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.bestroboticsteam.robotsmanagement.Direction;
import lejos.robotics.navigation.Pose;
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
	private float posx;
	private float posy;
	private Pose position = new Pose();
	private LinkedList<Point> path;
	
	public RobotSimController(MovableRobot robot, GridMap map, GridPose start, int theRobot) {
		this.map = map;
		this.pilot = new GridPilot(robot.getPilot(), map, start);
		this.robot = robot;
		this.theRobot = theRobot;
		this.path = CreateSimRobots.getPath(theRobot);	
	}
	
	public void run() {
		while (true) {
			float posForSimY = simY();
			float posForSimX = simX();
			posy = CreateSimRobots.getPosY(theRobot);
			posx = CreateSimRobots.getPosX(theRobot);
			float newPosx = convertX(posx);
			float newPosY = convertY(posy);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("RobotSim thread has been interrupted");
			}
			if (newPosY != simY()) {
				posForSimY = newPosY;
			}
			if (newPosY != simX()) {
				posForSimX = newPosx;
			}
			Direction dir = CreateSimRobots.getDirection(theRobot);
			if (dir == Direction.LEFT){
				position.setHeading(180);
			} else if (dir == Direction.FORWARD){
				position.setHeading(90);
			} else if (dir == Direction.RIGHT){
				position.setHeading(0);
			} else if (dir == Direction.BACKWARD){
				position.setHeading(270);
			}
			position.setLocation(posForSimX, posForSimY);
			robot.setPose(position);
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

	public static float convertX(float posx) {
		float x = -1.0f;
		if (posx == 0.0f) {
			x = 0.17f;
		} else if (posx == 1.0f) {
			x = 0.47f;
		} else if (posx == 2.0f) {
			x = 0.77f;
		} else if (posx == 3.0f) {
			x = 1.07f;
		} else if (posx == 4.0f) {
			x = 1.37f;
		} else if (posx == 5.0f) {
			x = 1.67f;
		} else if (posx == 6.0f) {
			x = 1.97f;
		} else if (posx == 7.0f) {
			x = 2.27f;
		} else if (posx == 8.0f) {
			x = 2.57f;
		} else if (posx == 9.0f) {
			x = 2.28f;
		} else if (posx == 10.0f) {
			x = 3.17f;
		} else if (posx == 11.0f) {
			x = 3.47f;
		} else {
			logger.error("position is not on map");
		}
		return x;
	}

	static float convertY(float posy) {
		float y = -1.0f;
		if (posy == 0) {
			y = 0.155f;
		} else if (posy == 1.0f) {
			y = 0.455f;
		} else if (posy == 2.0f) {
			y = 0.755f;
		} else if (posy == 3.0f) {
			y = 1.055f;
		} else if (posy == 4.0f) {
			y = 1.355f;
		} else if (posy == 5.0f) {
			y = 1.655f;
		} else if (posy == 6.0f) {
			y = 1.955f;
		} else if (posy == 7.0f) {
			y = 2.255f;
		} else {
			logger.error("position is not on map");
		}
		return y;
	}
	
	/*public static Point mapOutPath(){
		Point theGoal = CreateSimRobots.getGoalPoint(theRobot);
		return theGoal;
		
	}*/
}