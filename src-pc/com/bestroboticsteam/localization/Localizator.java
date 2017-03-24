package com.bestroboticsteam.localization;

import com.bestroboticsteam.robotsmanagement.Direction;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;

public class Localizator {
	private final float MIN_RANGE_TO_WALL = 20;
	private GridMap map;
	private boolean[][] possiblePositions;
	private LocalizationInfo info;

	public Localizator() { 
		this.map = MapUtils.createRealWarehouse();
		this.possiblePositions = getPossibleStartingPositions();
		this.info = new LocalizationInfo();
	}
	
	public Direction nextMove(){
		boolean[] possibleMoves = new boolean[4];
		for(int i=0; i<4; i++)
			possibleMoves[i] = true;
		
		if(info.getSensorReading(0) < MIN_RANGE_TO_WALL){
			for(int i=0; i<possiblePositions.length; i++){
				for(int j=0; j<possiblePositions[0].length; j++){
					if(!map.isObstructed(i-1, j))
						possiblePositions[i][j] = false;
				}	
			}
		}
		
		if(info.getSensorReading(1) < MIN_RANGE_TO_WALL){
			for(int i=0; i<possiblePositions.length; i++){
				for(int j=0; j<possiblePositions[0].length; j++){
					if(!map.isObstructed(i, j+1))
						possiblePositions[i][j] = false;
				}	
			}
		}
		
		if(info.getSensorReading(2) < MIN_RANGE_TO_WALL){
			for(int i=0; i<possiblePositions.length; i++){
				for(int j=0; j<possiblePositions[0].length; j++){
					if(!map.isObstructed(i+1, j))
						possiblePositions[i][j] = false;
				}	
			}
		}
		
		if(info.getSensorReading(3) < MIN_RANGE_TO_WALL){
			for(int i=0; i<possiblePositions.length; i++){
				for(int j=0; j<possiblePositions[0].length; j++){
					if(!map.isObstructed(i, j-1))
						possiblePositions[i][j] = false;
				}	
			}
		}
		
		for(int i=0; i<4; i++){
			if(possibleMoves[i])
				return Direction.valueOf(i);
		}
		
		return null;
	}
	
	public boolean foundPosition() {
		int possibilities = 0;
		for(int i=0; i<possiblePositions.length; i++){
			for(int j=0; j<possiblePositions[0].length; j++){
				if(possiblePositions[i][j]){
					possibilities++;
					if(possibilities > 1)
						return false;
				}
			}
		}
		
		if(possibilities != 1)
			return false;
		
		return true;
	}
	
	public LocalizationInfo getInfo(){
		return info;
	}
	
	private boolean[][] getPossibleStartingPositions(){
		int width = map.getXSize();
		int height = map.getYSize();
		boolean[][] possiblePositions = new boolean[width][height];
		for(int i=0; i<width; i++){
			for(int j=0; j<height; j++){
				if(map.isObstructed(i, j))
					possiblePositions[i][j] = false;
				else
					possiblePositions[i][j] = true;
			}
		}
		
		return possiblePositions;
	}
}
