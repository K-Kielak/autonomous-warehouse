package com.bestroboticsteam.jobs;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.BeforeClass;
import org.junit.Test;

public class JobSelectionTest {
	

	private static JobSelection selector1;
	private static JobSelection selector2;
	private static JobSelection selector3;
	private final int numb = 10000;

	@BeforeClass
	public static void ReadData(){
		
		selector1 = new JobSelection("../central-system/assets/test/TEST1");
		selector2 = new JobSelection("../central-system/assets/test/TEST2");
		selector3 = new JobSelection("../central-system/assets/test/TEST3");
	}
	
	@Test
	public void TestPrediction1(){
		ArrayList<Integer> values = new ArrayList<Integer>();
		values.add(numb+4);
		values.add(numb+8);
		values.add(numb+22);
		values.add(numb+24);
		values.add(numb+35);
		values.add(numb+37);
		values.add(numb+38);
		values.add(numb+40);
		values.add(numb+42);
		values.add(numb+45);
		values.add(numb+46);
		values.add(numb+51);
		values.add(numb+62);
		values.add(numb+70);
		values.add(numb+77);
		values.add(numb+87);
		
		int pred = 0;
		
		for(int i = 0; i < 99; i++){
			if(selector1.viewOrder(i).getPrediction()){
				if(values.contains(selector1.viewOrder(i).getId()))
					pred++;
			}
		}
		System.out.println(pred);
		assertTrue(pred >= 12);	
	
	}
	
	@Test
	public void TestPrediction2(){
		ArrayList<Integer> values = new ArrayList<Integer>();
		values.add(numb+24);
		values.add(numb+29);
		values.add(numb+33);
		values.add(numb+38);
		values.add(numb+43);
		values.add(numb+44);
		values.add(numb+45);
		values.add(numb+50);
		values.add(numb+70);
		values.add(numb+71);
		values.add(numb+78);
		values.add(numb+79);
		values.add(numb+82);
		values.add(numb+85);
		values.add(numb+97);
		
		int pred = 0;
		
		for(int i = 0; i < 99; i++){
			if(selector2.viewOrder(i).getPrediction()){
				if(values.contains(selector2.viewOrder(i).getId())){
					pred++;
				}
			}
		}
		System.out.println(pred);
		assertTrue(pred >= 10);
	}
	
	@Test
	public void TestPrediction3(){
		ArrayList<Integer> values = new ArrayList<Integer>();
		values.add(numb+4);
		values.add(numb+10);
		values.add(numb+12);
		values.add(numb+14);
		values.add(numb+17);
		values.add(numb+21);
		values.add(numb+30);
		values.add(numb+34);
		values.add(numb+37);
		values.add(numb+57);
		values.add(numb+61);
		values.add(numb+67);
		values.add(numb+69);
		values.add(numb+99);
		
		int pred = 0;
		
		for(int i = 0; i < 99; i++){
			if(selector3.viewOrder(i).getPrediction()){
				if(values.contains(selector3.viewOrder(i).getId())){
					pred++;
				}
			}
		}
		System.out.println(pred);
		assertTrue(pred >= 10);
	}

}
