package com.bestroboticsteam.jobs;

import static com.bestroboticsteam.communication.RobotNames.ROBOT_1_NAME;
import static com.bestroboticsteam.communication.RobotNames.ROBOT_2_NAME;
import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.BeforeClass;
import org.junit.Test;

import com.bestroboticsteam.robotsmanagement.Direction;
import com.bestroboticsteam.robotsmanagement.RobotInfo;

public class JobAssignmentTest {
	
	private static final RobotInfo[] robots = {  //TODO For loop
		new RobotInfo(ROBOT_1_NAME, new Point(0, 0), Direction.FORWARD, 30f),
		new RobotInfo(ROBOT_2_NAME, new Point(11, 7), Direction.FORWARD, 30f)
	};
	
	private static JobSelection selector;
	private static JobAssignment assignment;

	@BeforeClass
	public static void ReadData(){
		
		selector = new JobSelection("../central-system/assets/test");
		assignment = new JobAssignment(selector, robots);
	
	}
	
	@Test
	public void TestNextJob(){
		
		
	}
	
}
