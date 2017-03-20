package com.bestroboticsteam.jobs;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

public class JobAssignmentTest {
	
	private static JobSelection selector;
	private static JobAssignment assignment;

	@BeforeClass
	public static void ReadData(){
		
		selector = new JobSelection("../central-system/assets/test");
		assignment = new JobAssignment(selector);
	
	}
	
	@Test
	public void TestNextJob(){
		
		JobInfo info = assignment.getNextJob();
		
		assertEquals(info.getItem(), "a");
		assertEquals(info.getJobCode(), 1002);
		assertEquals(info.getQuantity(), 5);
		assertEquals(info.getPosition().x, 1);
		assertEquals(info.getPosition().y, 1);
		
	}
	
}
