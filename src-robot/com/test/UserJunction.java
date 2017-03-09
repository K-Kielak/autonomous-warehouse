package com.test;

import java.util.LinkedList;

import lejos.nxt.Button;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.util.Delay;
import rp.config.RobotConfigs;
import rp.config.WheeledRobotConfiguration;
import rp.systems.RobotProgrammingDemo;
import rp.systems.StoppableRunnable;
import rp.systems.WheeledRobotSystem;


public class UserJunction extends RobotProgrammingDemo implements StoppableRunnable{
	
	private LinkedList<Integer> path;
	private DifferentialPilot pilot;
	private LightSensor leftSensor;
	private LightSensor rightSensor;
	private int calibratedValue;
	private int error = 7;
	private boolean isMoving = true;
	
	public UserJunction(SensorPort leftSensor, SensorPort rightSensor, WheeledRobotConfiguration ExpressBot, LinkedList<Integer> path){
		this.pilot = new WheeledRobotSystem(ExpressBot).getPilot();
		this.rightSensor = new LightSensor(rightSensor);
		this.leftSensor = new LightSensor(leftSensor);
		this.calibratedValue = getCalibratedValue(this.leftSensor, this.rightSensor);
		this.path = path;
		
		this.pilot.setTravelSpeed(0.2f);
		this.leftSensor.setFloodlight(true);
		this.rightSensor.setFloodlight(true);
	}

	private int getCalibratedValue(LightSensor leftSensor, LightSensor rightSensor){
		Delay.msDelay(300);
		int leftValue = leftSensor.readValue();
		int rightValue = rightSensor.readValue();
		Delay.msDelay(300);
		int calibratedValue = (leftValue + rightValue) / 2;
		return calibratedValue;
	}
	
	@Override
	public void run() {		
		pilot.forward();
		
		boolean isLeftOnBlack = false;
		boolean isRightOnBlack = false;
		Integer direction = 0;
		
		while(isMoving && !path.isEmpty()){
			isRightOnBlack = isOnBlack(rightSensor.readValue());
		
			isLeftOnBlack = isOnBlack(leftSensor.readValue());
			
			if(isRightOnBlack && isLeftOnBlack){
				pilot.travel(0.07);
				
				direction = path.get(0);
				path.remove(0);
				switch(direction){
					case Button.ID_LEFT:
						pilot.rotate(90);
						break;
					case Button.ID_RIGHT:
						pilot.rotate(-90);
						break;
				}
				
				pilot.forward();
				continue;
			}
			
			while(isRightOnBlack && !isLeftOnBlack){
				pilot.rotateRight();
				
				isLeftOnBlack = isOnBlack(leftSensor.readValue());
				isRightOnBlack = isOnBlack(rightSensor.readValue());
			}
			
			while(!isRightOnBlack && isLeftOnBlack){
				pilot.rotateLeft();
				
				isLeftOnBlack = isOnBlack(leftSensor.readValue());
				isRightOnBlack = isOnBlack(rightSensor.readValue());
			}
			
			pilot.forward();
		}
	}
	
	private boolean isOnBlack(int sensorValue){
		return Math.abs(calibratedValue - sensorValue) > error;
	}
	
	public static void main(String[] args) {
		LinkedList<Integer> p = new LinkedList<Integer>();
		int direction = Button.waitForAnyPress();
		while(direction != Button.ID_ENTER){
			p.add(direction);
			direction = Button.waitForAnyPress();
		}
	

		WheeledRobotConfiguration config = 
		new WheeledRobotConfiguration(RobotConfigs.EXPRESS_BOT.getWheelDiameter(), RobotConfigs.EXPRESS_BOT.getTrackWidth(), (float) RobotConfigs.EXPRESS_BOT.getRobotLength(), Motor.C, Motor.B);
		RobotProgrammingDemo demo = new UserJunction(SensorPort.S2, SensorPort.S3, config, p);
		demo.run();
	}
}


