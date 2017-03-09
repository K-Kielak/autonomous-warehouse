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
		System.out.println(selector.viewOrder(0).getTotalReward());
		System.out.println(selector.viewOrder(1).getTotalReward());
		System.out.println(selector.viewOrder(2).getTotalReward());
		assertEquals(selector.viewOrder(0).getTotalReward(), 6509.8682f, 0.001f);
		assertEquals(selector.viewOrder(1).getTotalReward(), 9764.969f, 0.001f);
		assertEquals(selector.viewOrder(2).getTotalReward(), 10.33333f, 0.001f);
	}
}
