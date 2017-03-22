package com.bestroboticsteam.localization;

import java.util.Arrays;

import com.bestroboticsteam.robotsmanagement.Direction;

public class EmulateAroundMaze {
	/**
	 * 
	 * 
		
		

			 *     
			 *     
			 *     int[][] temp={
			 *        {1,2,3,4},
			 *        {5,6,7,8},
			 *        {9,10,11,12}
			 *        };
			 *     sub2darray(array, 1, 2, 1, 2)
			 *     {
			 *       {6,7}
			 *       {10,11}
			 *     }
	 */
	
	public static void test_sub2darray() {
		
		double[][] test = new double[][]{
			 { 1,2,3,4,5 },
			 { 1,2,3,4,5 },
			 { 1,2,3,4,5 },
			 { 1,2,3,4,5 },
			 { 1,2,3,4,5 }
			 };
			 
		double[][] result = Localization.sub2darray(test, 1, 3, 1, 4);
		
		double[][] expected = new double[][]{
			{ 2,3,4 },
			{ 2,3,4 },
			{ 2,3,4 },
			{ 2,3,4 }
		};
		assert Arrays.equals(result, expected);
		
		double[][] test2={
				{1,2,3,4},
				{5,6,7,8},
				{9,10,11,12}
				};
		
		double[][] result2 = Localization.sub2darray(test2, 1, 2, 1, 2);
		
		double[][] expected2 = new double[][]{
			{6,7},
			{10,11}
			};
			
		assert Arrays.equals(result2, expected2);
	}
	
	public static void main(String[] args) {
//		EmulateAroundMaze.test_sub2darray();
		Localization l = new Localization();
		l.pprintarray(l.poseLikelihoods);
		for (int i = 0; i < 7; i++) {
			l.updateDistributionAfterMove(Direction.FORWARD);
		}
		for (int i = 0; i < 3; i++) {
			l.updateDistributionAfterMove(Direction.LEFT);
		}
		
	}
}
