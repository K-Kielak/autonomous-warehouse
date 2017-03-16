package com.bestroboticsteam.main;

import java.awt.Point;

import com.bestroboticsteam.jobs.JobSelection;
import static com.bestroboticsteam.communication.RobotNames.*;
import com.bestroboticsteam.jobs.JobAssignment;
import com.bestroboticsteam.pathfinding.AStar;
import com.bestroboticsteam.robotsmanagement.*;
import com.bestroboticsteam.warehouseinterface.InterfaceController;

public class Main {

	private static final String PATH = "../central-system/assets/production";
	
	private static final RobotInfo[] robots = {  //TODO For loop
			new RobotInfo(ROBOT_1_NAME, new Point(0, 0), Direction.FORWARD),
			new RobotInfo(ROBOT_2_NAME, new Point(11, 7), Direction.FORWARD)
		};
	
	
	public static void main(String[] args) {
		JobSelection jobsSelect = new JobSelection(PATH);
		JobAssignment jobsAssign = new JobAssignment(jobsSelect);
		AStar pathFinder = new AStar();

		RobotsManager manager = new RobotsManager(robots, jobsAssign);
		manager.start();
		
		InterfaceController warehouseInterface = new InterfaceController(jobsSelect, jobsAssign);
		warehouseInterface.start();
	}
}
