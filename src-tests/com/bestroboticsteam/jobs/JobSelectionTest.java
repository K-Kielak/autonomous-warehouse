package com.bestroboticsteam.jobs;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

public class JobSelectionTest {
	

	private static JobSelection selector;

	@BeforeClass
	public static void ReadData(){
		
		selector = new JobSelection("../central-system/assets/test");
	}
	
	@Test
	public void TestReward(){
		
		float value = (1f*1f)/(2*1) + (2f*3253.323f)/(2f*23.43f) + (0.11111f*20f)/(1f*20f);
		assertEquals(selector.viewOrder(0).getTotalReward(), value, 0.001f);
		
		value = (5f*1f)/(5f*2f) + (3f*3253.321f)/(3f*23.43f);
		assertEquals(selector.viewOrder(1).getTotalReward(), value, 0.001f);
		
		value = (3*0.11111f)/(3*1f) + (1*10f)/(1*23.3f);
		assertEquals(selector.viewOrder(2).getTotalReward(), value, 0.001f);
		
		selector.cancelOrder("1001");
		assertFalse(selector.viewOrder(0).getId() == 1001);
	
	}

}
