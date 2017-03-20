package com.bestroboticsteam.robotexecution;
import lejos.nxt.Sound;



import com.bestroboticsteam.robotsmanagement.Direction;
import com.bestroboticsteam.robotsmanagement.RobotInfo;
import java.util.LinkedList;

import javax.microedition.io.Connection;

//import java.util.Optional;
import com.bestroboticsteam.jobs.JobInfo;
import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;
import com.bestroboticsteam.communication.*;

import com.bestroboticsteam.communication.RobotCommunicationHandler;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.navigation.DifferentialPilot;
import rp.config.RobotConfigs;
import rp.config.WheeledRobotConfiguration;
import rp.systems.RobotProgrammingDemo;
import rp.systems.StoppableRunnable;
import rp.systems.WheeledRobotSystem;

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
		this.robotInterface = new RobotInterface();
	}
	
	public void receiveInfo() { // Note: Block
		try {
			this.info = (RobotInfo) this.comms.receiveObject(this.info);
		} catch (ConnectionNotEstablishedException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {	
		
		while(!this.comms.getStatus().equals("CONNECTED")){
			robotInterface.bluetoothMessage(info.getName());
		}
		
		this.comms.run();
		System.out.println(this.comms.getStatus());
		
		this.receiveInfo();
		
		while(m_run){
//			this.receiveInfo();
//			printInfo();
			Direction direction = info.move();
			if(direction != null){
				movement.move(direction);
				if(info.getCurrentJob().isGoingToDropPoint()){
					robotInterface.printMovingToDropPointMessage(info.getName(),info.getCurrentJob().getJobCode(),info.getCurrentJob().getPosition());
				}
				else 
					robotInterface.printMovingToItemMessage(info.getName(),info.getCurrentJob().getJobCode(),info.getCurrentJob().getItem(),info.getCurrentJob().getPosition());
			}
			else if(!info.finished()){
				Sound.playTone(110, 800);
				LCD.drawString("Please press ENTER to confirm pickup location.",1,0);
				Button.waitForAnyPress();
				while(info.getCurrentJob().getQuantity() != robotInterface.getItemsQuantity()){
				    LCD.clear();
					robotInterface.printLoadMessage(info.getName(),info.getCurrentJob().getItem(),info.getCurrentJob().getQuantity(), robotInterface.getItemsQuantity());
					robotInterface.waitForButton();
					info.getCurrentJob().decreaseQuantity();
			}
		}
			robotInterface.setItemsQuantity(0);
		}
//		while(info.finished()){
		
//		}
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
