package com.bestroboticsteam.main;

import java.awt.Point;

import com.bestroboticsteam.jobs.JobSelection;
import com.bestroboticsteam.jobs.JobAssignment;
import com.bestroboticsteam.pathfinding.AStar;
import com.bestroboticsteam.robotsmanagement.*;
import com.bestroboticsteam.warehouseinterface.InterfaceController;

public class Main {

	private static final String PATH = "../assets/production";
	private static final RobotInfo[] robots = { 
			new RobotInfo("robot1", new Point(0, 0), Direction.FORWARD)
		};
	
	
	public static void main(String[] args) {
		JobSelection jobsSelect = new JobSelection(PATH);
		JobAssignment jobsAssign = new JobAssignment(jobsSelect);
		AStar pathFinder = new AStar();

		RobotsManager manager = new RobotsManager(robots, jobsAssign, pathFinder);
		manager.run();
		
		InterfaceController warehouseInterface = new InterfaceController(jobsSelect);
		warehouseInterface.run();
	}

}
