package com.bestroboticsteam.jobs;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

public class JobSelectionTest {
	

	private static JobSelection selector;
	private final int numb = 10000;

	@BeforeClass
	public static void ReadData(){
		
		selector = new JobSelection("../central-system/assets/test/TEST1");
	}
	
	@Test
	public void TestPrediction1(){
		ArrayList<Integer> values = new ArrayList<Integer>();
		values.add(numb+4);
		values.add(numb+8);
		values.add(numb+22);
		values.add(numb);
		values.add(numb);
		
		
	
	}

}
