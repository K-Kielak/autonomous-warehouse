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
		new RobotInfo(ROBOT_1_NAME, new Point(0, 0), Direction.FORWARD, 50f)
	};
	
	private static JobSelection selector;
	private static JobAssignment assignment;

	@BeforeClass
	public static void ReadData(){
		
		selector = new JobSelection("../central-system/assets/test/TEST1");
		assignment = new JobAssignment(selector, robots);
	
	}
	
	@Test
	public void TestNextJob(){
		
		for(int i = 0; i < 5; i ++){
			
			JobInfo info = assignment.getNextJob(robots[0].getName());
			
			float w = 0f;
			
			while(!info.getItem().equals("DropBox")){
				w += info.getWeight();
				info = assignment.getNextJob(robots[0].getName());
			}
			
			assertTrue(w <= 50f);
		}
	}
	
}
