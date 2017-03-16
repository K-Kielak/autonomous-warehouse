package com.bestroboticsteam.robotexecution;

import java.util.LinkedList;

import com.bestroboticsteam.communication.RobotCommunicationHandler;
import com.bestroboticsteam.jobs.JobInfo;
import com.bestroboticsteam.robotsmanagement.Direction;
import com.bestroboticsteam.robotsmanagement.RobotInfo;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import rp.config.RobotConfigs;
import rp.config.WheeledRobotConfiguration;
import rp.systems.RobotProgrammingDemo;
import rp.systems.StoppableRunnable;
import rp.systems.WheeledRobotSystem;
import lejos.util.Delay;

public class Robot extends RobotProgrammingDemo implements StoppableRunnable{
	private Movement movement;
	private RobotInterface robotInterface;
	private RobotInfo info = new RobotInfo();
	private RobotCommunicationHandler comms;
    private boolean m_run = true;
    
	public Robot(SensorPort leftSensorPort, SensorPort rightSensorPort, WheeledRobotConfiguration ExpressBot) {
		LightSensor rightSensor = new LightSensor(rightSensorPort);
		LightSensor leftSensor = new LightSensor(leftSensorPort);
		DifferentialPilot pilot = new WheeledRobotSystem(ExpressBot).getPilot();
		this.movement = new Movement(leftSensor, rightSensor, pilot);
		this.robotInterface = new RobotInterface("TODO");
	}
	
	@Override
	public void run() {	
		
		while(!this.comms.getStatus().equals("CONNECTED")){
			robotInterface.bluetoothMessage();
		}
		this.comms.run();
		System.out.println(this.comms.getStatus());
		
		this.receiveInfo();
		
		while(m_run){
			this.receiveInfo();
			int number  = 0;
		//	printInfo();
			
			Direction direction = info.move();
			if(direction != null){
				movement.move(direction);
				robotInterface.movingToPickup(info.getCurrentJob().getJobCode(),info.getCurrentJob().getItem(),info.getCurrentJob().getPosition());
			
			
			}
			else if(!info.finished()){
				Button.waitForAnyPress();
				//robotInterface.load(
				LCD.clear();
				LCD.drawString("Please press ENTER to confirm the pick up location", 1, 0);
				robotInterface.loadItems();
				
				}
				LCD.clear();
				while(info.getCurrentJob().getQuantity() != number){
					robotInterface.load(info.getCurrentJob().getItem(),info.getCurrentJob().getQuantity(), number);
					
					if(left){
						Delay.msDelay(100);
						number--;
						robotInterface.status(number);
					}
					
					if(right){
						Delay.msDelay(100);
						number++;
						robotInterface.status(number);
					}
					
					robotInterface.load(info.getCurrentJob().getItem(),info.getCurrentJob().getQuantity(), number);
					
			}
			
			//TODO send to robot;
		}
		
		
		while(info.finished()){
			
		}
	}
	
	@Override
	public void stop(){
		m_run = false;
	}

	public static void main(String[] args) {
		WheeledRobotConfiguration config = new WheeledRobotConfiguration(RobotConfigs.EXPRESS_BOT.getWheelDiameter(),
				RobotConfigs.EXPRESS_BOT.getTrackWidth(), (float) RobotConfigs.EXPRESS_BOT.getRobotLength(), Motor.C,
				Motor.B);
		RobotProgrammingDemo demo = new Robot(SensorPort.S2, SensorPort.S3, config);
		demo.run();
	}

}
