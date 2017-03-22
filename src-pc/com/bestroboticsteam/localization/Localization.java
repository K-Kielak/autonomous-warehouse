package com.bestroboticsteam.localization;

import java.util.Arrays;

import com.bestroboticsteam.robotsmanagement.Direction;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;

public class Localization {
	private GridMap map;
	public double[][] poseLikelihoods; // Y, X


	public Localization() { // TODO Refactor
		// Create the warehouse map
		this.map = MapUtils.createRealWarehouse();
		
		// Initialise the 2d array with map sizes
		this.poseLikelihoods = new double[map.getYSize()][map.getXSize()];
		
		// Initialise all poses as equally likely by first getting number of elements on map
		int numberOfSquares = map.getXSize() * map.getYSize();
		
		// then get the number of obstructions 
		for (int y=0; y<map.getYSize(); y++) {
			for(int x=0; x<map.getXSize(); x++) {
				if (this.map.isObstructed(x, y)) {
					numberOfSquares--;
				}
			}
		}
		
		// Calculate the normal distribution probability
		double uniform_proability = 1.0  / numberOfSquares;
		
		// Fill the array		
		for (int i = 0; i < this.map.getXSize(); i++) {
			for (int j = 0; j < this.map.getYSize(); j++) {
				if (this.map.isObstructed(i, j)){
					poseLikelihoods[j][i] = 0.0;
				}
				else {
					poseLikelihoods[j][i] = uniform_proability;
				}
			}
		}
	}
	
	public void updateDistributionAfterMove(Direction d) {
		/**
		 * 
		 * This should be called after a move has been made.
		 * 
		 * Direction is the direction towards a certain wall from origin. e.g.
		 *   FORWARD -> towards positive y
		 *   BACKWARS -> towards negative y
		 *   LEFT -> towards negative x
		 *   RIGHT -> towards positive x
		 * 
		 * Assuming a perfect ActionModel... at the moment
		 * 
		 */
		
		double[][] newArray = fillZeroArray(this.map.getXSize(), this.map.getYSize());
		double[][] temp;

		switch (d) {
		case FORWARD: {
			System.out.println("Going forwards");
			// We've gone up towards the top of the map. (positive y)
			// This means we copy all but the top of the array to 0,0
			temp = sub2darray(this.poseLikelihoods, 0, this.map.getXSize(), 0, this.map.getYSize()-1);
			for (int j = 0; j < temp.length; j++) {
				for (int i = 0; i < temp[j].length; i++) {
					newArray[j+1][i] = temp[j][i];
				}
			}
			break;
		}
		case BACKWARD: {
			System.out.println("Going backwards");
			// We've gone up towards the back wall. (negative y)
			// This means we copy all but the bottom of the array to 0,0
			temp = sub2darray(this.poseLikelihoods, 0, this.map.getXSize(), 1, this.map.getYSize());
			for (int j = 0; j < temp.length; j++) {
				for (int i = 0; i < temp[j].length; i++) {
					newArray[j][i] = temp[j][i];
				}
			}
			break;
		}
		case LEFT: {
			System.out.println("Going left");
			// We've gone towards the left wall
			// This means we copy all the item but the left side and move left
			
			temp = sub2darray(this.poseLikelihoods, 0, this.map.getXSize()-1, 0, this.map.getYSize());
			System.out.println("Size:" + temp.length + " ~ " + temp[0].length);
			System.out.println("New Array:" + newArray.length + " ~ " + newArray[0].length);
			for (int j = 0; j < temp.length; j++) {
				for (int i = 0; i < temp[j].length; i++) {
					newArray[j][i+1] = temp[j][i];
				}
			}
			break;
		}
		case RIGHT: {
			System.out.println("Going Right");
			// We've gone up towards the right wall. (positive x)
			// This means we copy all but the right items of the current array
			temp = sub2darray(this.poseLikelihoods, 1, this.map.getXSize(), 0, this.map.getYSize());
			System.out.println("Size:" + temp.length + " ~ " + temp[0].length);
			System.out.println("New Array:" + newArray.length + " ~ " + newArray[0].length);
			for (int j = 0; j < temp.length; j++) {
				for (int i = 0; i < temp[j].length; i++) {
					newArray[j][i] = temp[j][i];
				}
			}
			break;
		}
		default: {
			break;
		}
		}
		
		
		this.poseLikelihoods = this.normalize(newArray);
		pprintarray(this.poseLikelihoods);
	}
	
	private double[][] normalize(double[][] newArray) {
		double sum = sum(newArray);
		for (int j = 0; j < newArray.length; j++) {
			for (int i = 0; i < newArray[j].length; i++) {
				if (newArray[j][i] != 0.0) {
					newArray[j][i] = newArray[j][i] / sum;
				}

			}
		}
		return newArray;		
	}

	private static double sum(double[][] newArray) {
		double acc = 0.0;
		for (int j = 0; j < newArray.length; j++) {
			for (int i = 0; i < newArray[j].length; i++) {
				acc += newArray[j][i];
			}
		}
		return acc;
	}

	public static double[][] sub2darray(double[][] array, int x1, int x2, int y1, int y2) {
		/*
		 * 
		 * Gets a 2d double array and returns a section of that array
		 * 
		 * 
		 *     {   0 1 2 3 4
		 *     0 { 1,2,3,4,5 },
		 *     1 { 1,2,3,4,5 },
		 *     2 { 1,2,3,4,5 },
		 *     3 { 1,2,3,4,5 },
		 *     4 { 1,2,3,4,5 }
		 *     }
		 *     
		 *     sub2darray(array, 1, 3, 1, 4)
		 *     {   0 1 2
		 *     1 { 2,3,4 },
		 *     2 { 2,3,4 },
		 *     3 { 2,3,4 },
		 *     4 { 2,3,4 }
		 *     }
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
		 * 
		 */
//    	System.out.println("Start copy: "+x1 + " " + x2 + " " + y1 + " " + y2);
	    double[][] output = new double[y2-y1][(x2-x1)+1];
	    for (int i = 0; i < (y2-y1); i++) {
//	    	System.out.println("Start copy: "+ i);
	        output[i] = Arrays.copyOfRange(array[y1+i], x1, x2);
	    }
//	    System.out.println("End copy");
//	    Localization.pprintarray(output);
	    return output;
	}
	
	public static double[][] fillZeroArray(int size_x, int size_y) {
//		System.out.println("Generating zero array of size:" + size_y + " " + size_x);
		double result[][] = new double[size_y][size_x];
		for (int i = 0; i < size_y; i++) {
			for (int j = 0; j < size_x; j++) {
				result[i][j] = 0.0;
			}
		}
		return result;
	}
	
	public static void pprintarray(double[][] array) {
		/*
		 * Pretty-prints the array.
		 * 
		 * pprintarray()
		 *  0.1, 0.1, 0.1, 0.1
		 *  0.1, 0.0, 0.0, 0.1
		 * ...
		 * 
		 * Prints floats to 3 decimal place
		 * 
		 * 0,0 is the bottom left of output
		 * 
		 */
		
		String formatString = "%.3f ";
		
		for (int i = array.length-1; i >= 0; i--) {			
			for (int j = 0; j < array[i].length; j++) {
				System.out.format(formatString, array[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
	}
	

//	public void step(float range) {
//		switch (nextMovement) {
//		case FORWARD:
//			for (int i = 0; i < poseLikelihoods.length - 1; i++) {
//				// Move all likelihoods upwards
//				poseLikelihoods[i] = poseLikelihoods[i + 1];
//			}
//			break;
//		case RIGHT:
//			for (int i = 0; i < poseLikelihoods.length; i++) {
//				for (int j = poseLikelihoods.length - 1; j > 0; j--) {
//					// Move all likelihoods to the right
//					poseLikelihoods[i][j] = poseLikelihoods[i][j - 1];
//				}
//			}
//			break;
//		case BACKWARD:
//			for (int i = poseLikelihoods.length - 1; i > 0; i--) {
//				// Move all likelihoods downwards
//				poseLikelihoods[i] = poseLikelihoods[i - 1];
//			}
//			break;
//		case LEFT:
//			for (int i = 0; i < poseLikelihoods.length; i++) {
//				for (int j = 0; j < poseLikelihoods.length - 1; j++) {
//					// Move all likelihoods to the left
//					poseLikelihoods[i][j] = poseLikelihoods[i][j + 1];
//				}
//			}
//			break;
//		default: // null
//
//			break;
//		}
//		nextMovement = Direction.FORWARD;
//	}
//
//	public PoseLikelihood mostLikelyPose() {
//		double maxLikelihood = 0;
//		PoseLikelihood mostLikelyPose = null;
//		for (int i = 0; i < poseLikelihoods.length; i++) {
//			for (int j = 0; j < poseLikelihoods[i].length; j++) {
//				for (int k = 0; k < poseLikelihoods[i].length; k++) {
//					if (poseLikelihoods[i][j][k] > maxLikelihood) {
//						maxLikelihood = poseLikelihoods[i][j][k];
//						switch (k) {
//						case 0:
//							mostLikelyPose = new PoseLikelihood(poseLikelihoods[i][j][k], new Point(i, j),
//									Direction.FORWARD);
//							break;
//						case 1:
//							mostLikelyPose = new PoseLikelihood(poseLikelihoods[i][j][k], new Point(i, j),
//									Direction.RIGHT);
//							break;
//						case 2:
//							mostLikelyPose = new PoseLikelihood(poseLikelihoods[i][j][k], new Point(i, j),
//									Direction.BACKWARD);
//							break;
//						default:
//							mostLikelyPose = new PoseLikelihood(poseLikelihoods[i][j][k], new Point(i, j),
//									Direction.LEFT);
//							break;
//						}
//					}
//				}
//			}
//		}
//		return mostLikelyPose;
//	}
//
//	private void normalizeLocations() {
//		double sum = 0;
//		for (double[][] row : poseLikelihoods) {
//			for (double[] position : row) {
//				for (double poseLikelihood : position) {
//					sum += poseLikelihood;
//				}
//			}
//		}
//		double normalizationFactor = sum / (poseLikelihoods.length * poseLikelihoods[0].length);
//		for (int i = 0; i < poseLikelihoods.length; i++) {
//			for (int j = 0; j < poseLikelihoods[i].length; j++) {
//				for (int k = 0; k < poseLikelihoods[i].length; k++) {
//					poseLikelihoods[i][j][k] = poseLikelihoods[i][j][k] / normalizationFactor;
//				}
//			}
//		}
//	}
//
}
