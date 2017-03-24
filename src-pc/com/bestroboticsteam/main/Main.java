package com.bestroboticsteam.main;
import java.awt.Point;
import com.bestroboticsteam.jobs.JobSelection;
import static com.bestroboticsteam.communication.RobotNames.*;
import com.bestroboticsteam.jobs.JobAssignment;
import com.bestroboticsteam.robotsmanagement.*;
import com.bestroboticsteam.warehouseinterface.InterfaceController;
public class Main {
	private static final String PATH = "../central-system/assets/production";
	
	private static final RobotInfo[] robots = { 
			new RobotInfo(ROBOT_1_NAME, new Point(0, 0), Direction.FORWARD, 50f),
			new RobotInfo(ROBOT_2_NAME, new Point(11, 7), Direction.FORWARD, 50f),
			new RobotInfo(ROBOT_3_NAME, new Point(11, 0), Direction.FORWARD, 50f)
		};
	
	
	public static void main(String[] args) {
		JobSelection jobsSelect = new JobSelection(PATH);
		JobAssignment jobsAssign = new JobAssignment(jobsSelect, robots);
		
		RobotsManager manager = new RobotsManager(robots, jobsAssign);
		manager.setName("RobotsManager"); //setting thread name for debugging purposes
		manager.start();
		
		InterfaceController warehouseInterface = new InterfaceController(jobsSelect, jobsAssign, manager);
		warehouseInterface.setName("WarehouseInterface"); //setting thread name for debugging purposes
		warehouseInterface.start(); 
	}
}