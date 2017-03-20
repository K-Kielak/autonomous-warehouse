package com.bestroboticsteam.robotexecution;

import com.bestroboticsteam.robotsmanagement.Direction;

import lejos.nxt.LightSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;

public class Movement {
	private final int ERROR = 5;
	private final float SPEED = 0.2f;
	private int calibratedValue;
	private final LightSensor leftSensor;
	private final LightSensor rightSensor;
	private final DifferentialPilot pilot;

	public Movement(LightSensor leftSensor, LightSensor rightSensor, DifferentialPilot pilot) {
		this.leftSensor = leftSensor;
		this.rightSensor = rightSensor;
		this.pilot = pilot;

		this.pilot.setTravelSpeed(SPEED);
	}
	
	public void calibrate() {
		Delay.msDelay(300);
		int leftValue = leftSensor.readValue();
		int rightValue = rightSensor.readValue();
		Delay.msDelay(300);
		calibratedValue = (leftValue + rightValue) / 2;
	}

	public void move(Direction direction) {
		if(direction == null)
			return; //if there is not movement order just do nothing
		
		switch (direction) {
		case LEFT:
			pilot.rotate(90);
			break;
		case RIGHT:
			pilot.rotate(-90);
			break;
		case BACKWARD:
			pilot.rotate(180);
			break;
		case FORWARD:
			// dont't rotate
			break;
		}

		boolean isLeftOnBlack = isOnBlack(leftSensor.readValue());
		boolean isRightOnBlack = isOnBlack(rightSensor.readValue());
		while (!(isRightOnBlack && isLeftOnBlack)) {
			pilot.forward();

			while (isRightOnBlack && !isLeftOnBlack) {
				pilot.rotateRight();

				isLeftOnBlack = isOnBlack(leftSensor.readValue());
				isRightOnBlack = isOnBlack(rightSensor.readValue());
			}

			while (!isRightOnBlack && isLeftOnBlack) {
				pilot.rotateLeft();

//				Delay.msDelay(DELAY);
				isLeftOnBlack = isOnBlack(leftSensor.readValue());
				isRightOnBlack = isOnBlack(rightSensor.readValue());
			}
		}

		pilot.stop();
		pilot.travel(0.07);
		pilot.stop();
	}

	private boolean isOnBlack(int sensorValue) {
		return Math.abs(calibratedValue - sensorValue) > ERROR;
	}
}