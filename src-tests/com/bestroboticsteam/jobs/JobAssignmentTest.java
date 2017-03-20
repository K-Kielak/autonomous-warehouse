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
		
	
	}
	
	@Test
	public void TestNextJob(){
		
		
	}
	
}
