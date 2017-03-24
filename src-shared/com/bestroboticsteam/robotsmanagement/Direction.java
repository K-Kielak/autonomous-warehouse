package com.bestroboticsteam.robotsmanagement;

public enum Direction {
	LEFT,
	FORWARD,
	RIGHT,
	BACKWARD,
	WAIT;
	
	public static Direction valueOf(int val){
		if(val%5 == LEFT.ordinal())
			return LEFT;
		else if(val%5 == FORWARD.ordinal())
			return FORWARD;
		else if(val%5 == RIGHT.ordinal())
			return RIGHT;
		else if(val%5 == BACKWARD.ordinal())
			return BACKWARD;
		
		return WAIT;
	}
}
