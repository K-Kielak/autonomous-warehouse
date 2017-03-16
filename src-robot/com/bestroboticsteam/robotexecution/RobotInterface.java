package com.bestroboticsteam.robotexecution;



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

public class RobotInterface{

	private final String NAME; 
	/*private String Confirm_pickup = "Confirm the ick up location";
	private String Confirm_drop = "Confirm the drop off location";*/
	private int itemsQuantity = 0;
	
	public RobotInterface(String name){
		this.NAME = name;
	}
			
	public void bluetoothMessage(){
		LCD.clear();
		LCD.drawString(NAME, 1, 0);
		LCD.drawString("Waiting for bluetooth connection", 2, 1);
		Delay.msDelay(800);
	}
	
	public void printMovingToItemMessage(int jobCode, String itemCode,Point position){
		LCD.clear();
		LCD.drawString("Robot name: " + NAME, 1, 0);
		LCD.drawString("Job code: " + jobCode, 1, 1);
		LCD.drawString("Item code: " + itemCode, 1, 2);
		LCD.drawString("X position: " +  position.x + " Y position: " + position.y,1,3);
	}
	
	public void printLoadMessage(String itemCode, int quantity, int number){
		LCD.clear();
		LCD.drawString(NAME, 1, 0);
		LCD.drawString("Please load: " + quantity + " items " + itemCode + " into the robot",1,1);
		LCD.drawString("There are: " + number + " items in the robot at the moment", 1, 2);
	}
	
	public void printMovingToDropPointMessage(int jobCode,Point position){
		LCD.clear();
		LCD.drawString(NAME, 1, 0);
		LCD.drawString("Job code: " + jobCode, 1, 1);
		LCD.drawString("Destination: " + position.x + ", " + position.y, 1, 2);
		LCD.drawString("Moving to delivery point...", 1, 3);
		
	}
	
	 private void addItems(){
			Delay.msDelay(100);
			itemsQuantity++;
			LCD.clear();
			LCD.drawString("Curent number of items is: " , 1, 0);
			LCD.drawInt(itemsQuantity, 2, 1);
	}
	
	 public void dropItems(){
			Delay.msDelay(100);
			itemsQuantity++;
			LCD.clear();
			LCD.drawString("Curent number of items is: " , 1, 0);
			LCD.drawInt(itemsQuantity++, 2, 1);		
	}	
	 
	 
	 
	 public void waitForButton(){
		 int pressedButtonID = Button.waitForAnyPress();
		 if(pressedButtonID == Button.ID_RIGHT)
			 dropItems();
		 else if(pressedButtonID == Button.ID_RIGHT)
			 addItems();
		 else if(pressedButtonID == Button.ID_ESCAPE){
			 LCD.drawString("Are you sure that you want to cancel?",1,0);
			 LCD.drawString("Press RIGHT arrow for \"YES\" or LEFT arrow for \"NO\"",1,1);
			 int pressedButtonID2 = Button.waitForAnyPress();
			 if(pressedButtonID2 == Button.ID_RIGHT){
				 //cancel job
			 }
			 else 
				 waitForButton();
		 }
	 }
	 
	public void status(int number){
		LCD.clear();
		LCD.drawString("Curent number of items is: " + number , 1, 0);
	}
}


	

