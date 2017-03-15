package com.bestroboicsteam.robotexecution;



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

public class RobotInterface extends RobotInfo{
	
	private static String NAME;
	private DataInputStream a;
	private DataOutputStream b;
	private int pick;
	private int frop;
	private int pick_loc;
	private int drop_loc;
	private String Confirm_pickup = "Confirm the pick up location";
	private String Confirm_drop = "Confirm the drop off location";
	private int number = 0;
	boolean orange = Button.ENTER.isPressed();
	boolean esc = Button.ESCAPE.isPressed();
	boolean right = Button.RIGHT.isPressed();
	boolean left = Button.LEFT.isPressed();
	
	public RobotInterface(String name,Point position,Direction direction){
		super(name,position,direction);
		
	}
		
	
	public static void bluetoothMessage(){
		LCD.clear();
		LCD.drawString(NAME, 1, 0);
		LCD.drawString("Waiting for bluetooth connection", 2, 1);
		Delay.msDelay(800);
	}
	
	public void movingToPickup(int jobCode, String itemCode,Point position){
		LCD.clear();
		LCD.drawString("Robot name: " + NAME, 1, 0);
		LCD.drawString("Job code: " + jobCode, 1, 1);
		LCD.drawString("Item code: " + itemCode, 1, 2);
		LCD.drawString("X position: " +  position.x + " Y position: " + position.y,1,3);
	}
	
	public void load(String itemCode, int quantity, int number){
		LCD.clear();
		LCD.drawString(NAME, 1, 0);
		LCD.drawString("Please load: " + quantity + " items " + itemCode + " into the robot",1,1);
		LCD.drawString("There are: " + number + " items in the robot at the moment", 1, 2);
	}
	
	public void toDropPointMessage(int jobCode,Point position){
		LCD.clear();
		LCD.drawString(NAME, 1, 0);
		LCD.drawString("Job code: " + jobCode, 1, 1);
		LCD.drawString("Destination: " + position.x + ", " + position.y, 1, 0);
		LCD.drawString("Moving to delivery point...", 1, 2);
		
	}
	
	 public void addItems(){
		if(right){
			Delay.msDelay(100);
			number++;
			LCD.clear();
			LCD.drawString("Curent number of items is: " , 1, 0);
			LCD.drawInt(number, 2, 1);
		}
	}
	 
	 public void loadItems(){
		 LCD.clear();
		 LCD.drawString("Please start loading the items" , 1, 0);
	 }
	
	public void dropItems(){
		if(left){
			Delay.msDelay(100);
			number--;
			LCD.clear();
			LCD.drawString("Curent number of items is: " , 1, 0);
			LCD.drawInt(number, 2, 1);		
		}
	}
	
	public void status(int number){
		LCD.clear();
		LCD.drawString("Curent number of items is: " + number , 1, 0);
	
	}
	}


	

