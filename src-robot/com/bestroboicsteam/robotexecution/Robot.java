package com.bestroboicsteam.robotexecution;

import java.awt.Point;
import java.util.LinkedList;

import com.bestroboticsteam.communication.ConnectionNotEstablishedException;
import com.bestroboticsteam.communication.RobotCommunicationHandler;
import com.bestroboticsteam.jobs.JobInfo;
import com.bestroboticsteam.robotsmanagement.Direction;
import com.bestroboticsteam.robotsmanagement.RobotInfo;

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

public class Robot extends RobotProgrammingDemo implements StoppableRunnable{
	private Movement movement;
	private RobotInfo info;
	private RobotCommunicationHandler comms;
	
	public Robot(SensorPort leftSensorPort, SensorPort rightSensorPort, WheeledRobotConfiguration ExpressBot, RobotInfo info){
		LightSensor rightSensor = new LightSensor(rightSensorPort);
		LightSensor leftSensor = new LightSensor(leftSensorPort);
		DifferentialPilot pilot = new WheeledRobotSystem(ExpressBot).getPilot();
		this.movement = new Movement(leftSensor, rightSensor, pilot);
		this.info = info;
		this.comms = new RobotCommunicationHandler();
	}
	
	@Override
	public void run() {
		this.comms.run(); // Blocking to wait for connection
		System.out.println(this.comms.getStatus());
		Direction direction = info.move();
		while(direction != null) {
			movement.move(direction);
			direction = info.move();
		}
	}
	
	public void sendInfo() {
		try {
			this.comms.sendObject(this.info);
		} catch (ConnectionNotEstablishedException e) {
			e.printStackTrace();
		}
	}
	
	public void receiveInfo() {
		try {
			this.info = (RobotInfo) this.comms.receiveObject(this.info);
		} catch (ConnectionNotEstablishedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		LinkedList<Integer> p = new LinkedList<Integer>();
		int direction = Button.waitForAnyPress();
		while(direction != Button.ID_ENTER){
			p.add(direction);
			direction = Button.waitForAnyPress();
		}
	
		RobotInfo info = new RobotInfo("xd", new Point(0, 0), Direction.FORWARD);
		LinkedList<Point> path = new LinkedList<Point>();
		path.add(new Point(1, 0)); path.add(new Point(2, 0)); path.add(new Point(3, 0)); path.add(new Point(4, 0)); path.add(new Point(5, 0));
		path.add(new Point(6, 0)); path.add(new Point(6, 1)); path.add(new Point(6, 2)); path.add(new Point(6, 3)); path.add(new Point(6, 4));
		path.add(new Point(6, 5)); path.add(new Point(6, 6)); path.add(new Point(7, 6)); path.add(new Point(8, 6)); path.add(new Point(9, 6));
		path.add(new Point(10, 6)); path.add(new Point(11, 6)); path.add(new Point(11, 7)); path.add(new Point(11, 6));
		JobInfo job = new JobInfo("", new Point(11, 6));
		info.setCurrentJob(job, path);
		WheeledRobotConfiguration config = 
		new WheeledRobotConfiguration(RobotConfigs.EXPRESS_BOT.getWheelDiameter(), RobotConfigs.EXPRESS_BOT.getTrackWidth(), (float) RobotConfigs.EXPRESS_BOT.getRobotLength(), Motor.C, Motor.B);
		RobotProgrammingDemo demo = new Robot(SensorPort.S2, SensorPort.S3, config, info);
		demo.run();
	}

}
