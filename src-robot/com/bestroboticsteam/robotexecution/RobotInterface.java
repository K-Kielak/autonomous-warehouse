package com.bestroboticsteam.robotexecution;

import java.awt.Point;

import com.bestroboticsteam.jobs.JobInfo;
import com.bestroboticsteam.robotsmanagement.RobotInfo;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;

public class RobotInterface {

	private int itemsQuantity = 0;
	
	public void waitForSensorCalibration() {
		LCD.clear();
		LCD.drawString("Press Any Button", 1, 0);
		LCD.drawString("to Calibrate", 1, 1);
		LCD.drawString("Sensors", 1, 2);
		Button.waitForAnyPress();				
	}

	public void bluetoothMessage(String message) {
		LCD.clear();
		LCD.drawString("", 1, 0);
		LCD.drawString("Status:", 2, 1);
		LCD.drawString(message, 4, 2);
		Delay.msDelay(800);
	}
	
	public void printWaitingForOrdersMessage(RobotInfo robot) {
		LCD.clear();
		if(robot.getName() != null)
			LCD.drawString(robot.getName(), 1, 0);
		LCD.drawString("Waiting for new orders from the server...", 1, 1);	
	}
	
	public void printMovingMessage(RobotInfo robot) {
		if(robot.getCurrentJob().isDropPoint())
			printMovingToDropPointMessage(robot);
		else
			printMovingToItemMessage(robot);
	}

	public void printLoadMessage(RobotInfo robot) {
		LCD.clear();
		LCD.drawString(robot.getName(), 1, 0);
		JobInfo j = robot.getCurrentJob();
		LCD.drawString("Please load: " + Integer.toString(j.getQuantity()-this.getItemsQuantity()) + " items", 1, 1);
		LCD.drawString("Right to load item", 0, 2);
		LCD.drawString("Left (<-) to drop item", 0, 3);
		LCD.drawString("Entr to and loading", 0, 4);
		LCD.drawString("Esc to cancel order", 0, 5);
		waitForLoading(robot);
	}
	
	public void resetItemsQuantity() {
		itemsQuantity = 0;
	}

	public int getItemsQuantity() {
		return itemsQuantity;
	}

	private void waitForLoading(RobotInfo robot) {
		int pressedButtonID = Button.waitForAnyPress();
		if (pressedButtonID == Button.ID_LEFT)
			dropItems();
		else if (pressedButtonID == Button.ID_RIGHT)
			addItems();
		else if (pressedButtonID == Button.ID_ESCAPE) {
			LCD.clear();
			LCD.drawString("Are you sure that you want to cancel?", 1, 0);
			LCD.drawString("Press Enter for \"YES\" or any other button for \"NO\"", 1, 1);
			int pressedButtonID2 = Button.waitForAnyPress();
			if (pressedButtonID2 == Button.ID_ENTER) {
				robot.cancelJob();
			} 
			else
				printLoadMessage(robot);
		}
	}
	
	private void printMovingToDropPointMessage(RobotInfo robot) {
		LCD.clear();
		LCD.drawString(robot.getName(), 1, 0);
		LCD.drawString("Job code: " + robot.getCurrentJob().getItem(), 1, 1);
		LCD.drawString("Moving to delivery point...", 1, 2);
		Point p = robot.getCurrentJob().getPosition();
		LCD.drawString("Destination: " + p.x + ", " + p.y, 1, 3);

	}
	
	private void printMovingToItemMessage(RobotInfo robot) {
		LCD.clear();
		LCD.drawString("Robot name: ", 1, 0);
		LCD.drawString("  " + robot.getName() , 1, 1);
		LCD.drawString("Job code: ", 1, 2);
		JobInfo j = robot.getCurrentJob();
		LCD.drawInt(j.getJobCode(), 1, 3);
		LCD.drawString("Moving to item: " + j.getItem(), 1, 4);
		LCD.drawString("Destination: ", 1, 5);
		Point p = j.getPosition();
		LCD.drawString("    x: " + p.x, 1,6);
		LCD.drawString("    y: " + p.y, 1,7);
		
	}
	
	private void addItems() {
		//Delay.msDelay(100);
		itemsQuantity++;
	}
	
	private void dropItems() {
		//Delay.msDelay(100);
		itemsQuantity--;
		if (itemsQuantity < 0) {
			itemsQuantity = 0;
		}
	}
}
