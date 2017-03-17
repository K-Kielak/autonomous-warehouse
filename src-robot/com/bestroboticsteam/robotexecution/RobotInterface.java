package com.bestroboticsteam.robotexecution;

import java.awt.Point;

import com.bestroboticsteam.communication.RobotCommunicationHandler;
import com.bestroboticsteam.jobs.JobInfo;
import com.bestroboticsteam.robotsmanagement.RobotInfo;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;

public class RobotInterface {

	private int itemsQuantity = 0;

	public void setItemsQuantity(int q) {
		itemsQuantity = q;
	}

	public int getItemsQuantity() {
		return itemsQuantity;
	}

	public void bluetoothMessage(RobotInfo robot, RobotCommunicationHandler comms) {
		LCD.clear();
		LCD.drawString("", 1, 0);
		LCD.drawString("Status:", 2, 1);
		LCD.drawString(comms.getStatus(), 4, 2);
		Delay.msDelay(800);
	}

	public void printMovingToItemMessage(RobotInfo robot) {
		LCD.clear();
		LCD.drawString("Robot name: ", 1, 0);
		LCD.drawString("  " + robot.getName() , 1, 1);
		LCD.drawString("Job code: ", 1, 2);
		JobInfo j = robot.getCurrentJob();
		LCD.drawInt(j.getJobCode(), 1, 3);
		LCD.drawString("Item code: " + j.getItem(), 1, 4);
		LCD.drawString("Position: ", 1, 5);
		Point p = robot.getPosition();
		LCD.drawString("    x: " + p.x, 1,6);
		LCD.drawString("    y: " + p.y, 1,7);
		
	}

	public void printLoadMessage(RobotInfo robot) {
		LCD.clear();
		LCD.drawString(robot.getName(), 1, 0);
		JobInfo j = robot.getCurrentJob();
		LCD.drawString("Please load: " + j.getQuantity() + " items " + j.getItem() + " into the robot", 1, 1);
		LCD.drawString("There are: " + this.itemsQuantity + " items in the robot at the moment", 1, 2);
	}

	//public void printMovingToDropPointMessage(String name, int jobCode, Point position) {
	public void printMovingToDropPointMessage(RobotInfo robot) {
		LCD.clear();
		LCD.drawString(robot.getName(), 1, 0);
		LCD.drawString("Job code: " + robot.getCurrentJob().getItem(), 1, 1);
		Point p = robot.getCurrentJob().getPosition();
		LCD.drawString("Destination: " + p.x + ", " + p.y, 1, 2);
		LCD.drawString("Moving to delivery point...", 1, 3);

	}

	private void addItems() {
		Delay.msDelay(100);
		itemsQuantity++;
		LCD.clear();
		LCD.drawString("Curent number of items is: ", 1, 0);
		LCD.drawInt(itemsQuantity, 2, 1);
	}

	public void dropItems() {
		Delay.msDelay(100);
		itemsQuantity--;
		LCD.clear();
		LCD.drawString("Curent number of items is: ", 1, 0);
		LCD.drawInt(itemsQuantity, 2, 1);
	}
	
	public void waitForSensorCalibration() {
		LCD.clear();
		LCD.drawString("Press Any Button", 1, 0);
		LCD.drawString("to Calibrate", 1, 1);
		LCD.drawString("Sensors", 1, 2);
		Button.waitForAnyPress();				
	}

	public void waitForButton() {
		int pressedButtonID = Button.waitForAnyPress();
		if (pressedButtonID == Button.ID_LEFT)
			dropItems();
		else if (pressedButtonID == Button.ID_RIGHT)
			addItems();
		else if (pressedButtonID == Button.ID_ESCAPE) {
			LCD.drawString("Are you sure that you want to cancel?", 1, 0);
			LCD.drawString("Press RIGHT arrow for \"YES\" or LEFT arrow for \"NO\"", 1, 1);
			int pressedButtonID2 = Button.waitForAnyPress();
			if (pressedButtonID2 == Button.ID_RIGHT) {
				// cancel job
			} else
				waitForButton();
		}
	}

	public void status(int number) {
		LCD.clear();
		LCD.drawString("Curent number of items is: " + number, 1, 0);
	}
}
