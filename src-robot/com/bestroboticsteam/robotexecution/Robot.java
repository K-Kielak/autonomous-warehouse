package com.bestroboticsteam.robotexecution;

import com.bestroboticsteam.communication.ConnectionNotEstablishedException;
import com.bestroboticsteam.communication.RobotCommunicationHandler;
import com.bestroboticsteam.robot.RobotConfig;
import com.bestroboticsteam.robotsmanagement.Direction;
import com.bestroboticsteam.robotsmanagement.RobotInfo;

import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.robotics.navigation.DifferentialPilot;
import rp.config.WheeledRobotConfiguration;
import rp.systems.StoppableRunnable;
import rp.systems.WheeledRobotSystem;

public class Robot implements StoppableRunnable {
	/*
	 * The interface is implemented as a set of states that the robot can be in.
	 * This is mostly for the interface on the LCD.
	 * 
	 * The main states are:
	 * - Waiting for connection
	 * - Going to an item
	 * - Going to a drop off point
	 * 
	 */
	private Movement movement;
	private RobotInterface robotInterface;
	private RobotInfo info = new RobotInfo();
	private RobotCommunicationHandler comms;
	private boolean m_run = true;
	private Direction direction;

	public Robot(SensorPort leftSensorPort, SensorPort rightSensorPort, WheeledRobotConfiguration ExpressBot) {
		LightSensor rightSensor = new LightSensor(rightSensorPort);
		LightSensor leftSensor = new LightSensor(leftSensorPort);
		DifferentialPilot pilot = new WheeledRobotSystem(ExpressBot).getPilot();
		this.movement = new Movement(leftSensor, rightSensor, pilot);
		this.robotInterface = new RobotInterface();
		this.comms = new RobotCommunicationHandler();
	}

	@Override
	public void run() {
		this.robotInterface.waitForSensorCalibration();
		this.movement.calibrate();
		Thread connection = new Thread(this.comms);
		connection.start();

		robotInterface.bluetoothMessage(RobotCommunicationHandler.CONNECTING);
		
		try {
			connection.join(); // Thread has ended
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while (m_run) {
			this.receiveInfo();
			this.robotInterface.printMovingToItemMessage(this.info);
			// Going to destination
			direction = info.move();
			if (direction != null) {
				System.out.println("moving to: " + direction);
				// If we get a direction move to it. This means that we have not arrived yet.
				movement.move(direction);
				if (info.getCurrentJob().isGoingToDropPoint()) { // Is the current route going to a drop off point?
					robotInterface.printMovingToDropPointMessage(info);
					
				} else { // Not a drop off point. Therefore, going to an item.
					robotInterface.printMovingToItemMessage(info);
				}	
			} else if (!info.finished()) { // Have we finished a job?
				Sound.playTone(110, 800); // We play a sound
				while (info.getCurrentJob().getQuantity() != robotInterface.getItemsQuantity()) {
					robotInterface.printLoadMessage(info);
				}
				this.info.pickAll();
			}
			robotInterface.setItemsQuantity(0); // We've collected items so we reset item quantity
			this.sendInfo();
		}
	}
	
	public void stop() {
		m_run = false;
	}
	
	public void sendInfo() {
		try {
			this.comms.sendObject(this.info);
		} catch (ConnectionNotEstablishedException e) {
			e.printStackTrace();
		}
	}
	
	public void receiveInfo() { // Note: Block
		try {
			this.info = (RobotInfo) this.comms.receiveObject(this.info);
			// TODO Check for null
		} catch (ConnectionNotEstablishedException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		WheeledRobotConfiguration config = RobotConfig.CUSTOM_EXPRESS_BOT;
		final SensorPort left = SensorPort.S2;
		final SensorPort right = SensorPort.S3;
		Robot demo = new Robot(left, right, config);
		demo.run();
	}

}
