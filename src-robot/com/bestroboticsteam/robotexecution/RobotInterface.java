package com.bestroboticsteam.robotexecution;

import java.awt.Point;

import com.bestroboticsteam.jobs.JobInfo;
import com.bestroboticsteam.robotsmanagement.RobotInfo;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.util.Delay;

public class RobotInterface {

	private final RobotInfo info;
	private int itemsQuantity = 0;
	
	public RobotInterface(RobotInfo info){
		this.info = info;
	}
	
	public void waitForSensorCalibration() {
		/*
		 * Prints a message to the screen like follows:
		 * 
		 * ┌──────────────────┐
		 * │ Press Any Button │
		 * │ to Calibrate     │
		 * │ Sensors          │
		 * │                  │
		 * │                  │
		 * │                  │
		 * └──────────────────┘
		 *
		 * Then waits for a button to be pressed.
		 * 
		 */
		LCD.clear();
		LCD.drawString("Press Any Button", 1, 0);
		LCD.drawString("to Calibrate", 1, 1);
		LCD.drawString("Sensors", 1, 2);
		Button.waitForAnyPress();				
	}

	public void bluetoothMessage(String message) {
		/**
		 * 
		 * ┌──────────────────┐
		 * │                  │
		 * │ Status:          │
		 * │ Connected        │
		 * │                  │
		 * │                  │
		 * │                  │
		 * └──────────────────┘
		 */
		LCD.clear();
		LCD.drawString("", 1, 0);
		LCD.drawString("Status:", 2, 1);
		LCD.drawString(message, 4, 2);
		Delay.msDelay(800);
	}
	
	public void printWaitingForOrdersMessage() {
		/**
		 * ┌──────────────────┐
		 * │   John Cena      │
		 * │ Waiting for new  │
		 * │ orders from the  │
		 * │ server...        │
		 * │                  │
		 * │                  │
		 * │                  │robot
		 * └──────────────────┘
		 */
		LCD.clear();
		if(info.getName() != null)
			LCD.drawString(info.getName(), 3, 0);
		LCD.drawString("Waiting for new", 1, 1);
		LCD.drawString("orders from the", 1, 2);
		LCD.drawString("server...", 1, 3);
	}
	
	public void printMovingMessage() {
		if(info.getCurrentJob().isDropPoint())
			printMovingToDropPointMessage();
		else
			printMovingToItemMessage();
	}
	
	public void printCheckpointMessage(){
		if(info.getCurrentJob().isDropPoint())
			printUnloadMessage();
		else
			printLoadMessage();
	}
	
	public void resetItemsQuantity() {
		itemsQuantity = 0;
	}

	public int getItemsQuantity() {
		return itemsQuantity;
	}
	
	private void printLoadMessage() {
		LCD.clear();
		LCD.drawString(info.getName(), 3, 0);
		JobInfo j = info.getCurrentJob();
		LCD.drawString("Job: " + j.getItem(), 1, 1);
		LCD.drawString("Please load: " + Integer.toString(j.getQuantity()-this.getItemsQuantity()) + " of " + j.getItem(), 1, 2);
		LCD.drawString("-> to load item", 0, 3);
		LCD.drawString("<- to drop item", 0, 4);
		LCD.drawString("Esc to cancel order", 0, 5);
		waitForLoading();
	}
	
	private void printUnloadMessage(){
		LCD.clear();
		LCD.drawString(info.getName(), 1, 0);
		JobInfo j = info.getCurrentJob();
		LCD.drawString("Job code: " + j.getJobCode(), 1, 1);
		LCD.drawString("Please unload all of the items", 1, 2);
		LCD.drawString("Press any button to unload", 1, 3);
		waitForUnloading();
	}

	private void waitForLoading() {
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
				info.cancelJob();
			} 
			else
				printLoadMessage();
		}
	}
	
	private void waitForUnloading(){
		Button.waitForAnyPress();
		dropItems();
	}
	
	private void printMovingToDropPointMessage() {
		/**
		 * ┌──────────────────┐
		 * │ John Cena        │
		 * │ Job Code: aaa    │
		 * │ Moving to        │
		 * │ delivery point...│
		 * │ server...        │
		 * │ Destination: 1, 2│
		 * │                  │
		 * │                  │
		 * └──────────────────┘
		 */
		LCD.clear();
		LCD.drawString(info.getName(), 1, 0);
		LCD.drawString("Job Code: " + info.getCurrentJob().getItem(), 1, 1);
		LCD.drawString("Moving to delivery point...", 1, 2);
		Point p = info.getCurrentJob().getPosition();
		LCD.drawString("Destination: " + p.x + ", " + p.y, 1, 3);

	}
	
	private void printMovingToItemMessage() {
		/**
		 * ┌──────────────────┐
		 * |    John Cena      │
		 * │ Job Code:        │
		 * │ 1234             │
		 * │ Moving to item:  │
		 * │ Destination:     │
		 * │    x: 1          │
		 * │    y: 6          │
		 * │                  │
		 * └──────────────────┘
		 */
		LCD.clear();
		LCD.drawString("  " + info.getName() , 2, 0);
		LCD.drawString("Job code: ", 1, 1);
		JobInfo j = info.getCurrentJob();
		LCD.drawInt(j.getJobCode(), 1, 2);
		LCD.drawString("Moving to item: " + j.getItem(), 1, 3);
		LCD.drawString("Destination: ", 1, 4);
		Point p = j.getPosition();
		LCD.drawString("    x: " + p.x, 1,5);
		LCD.drawString("    y: " + p.y, 1,6);
		
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
