package com.bestroboicsteam.robotexecution;

/*<<<<<<< Updated upstream
import java.awt.Point;
=======
*/

//>>>>>>> Stashed changes
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
/*<<<<<<< Updated upstream
	private RobotInfo info;
	private boolean m_run = true;
	
	public Robot(SensorPort leftSensorPort, SensorPort rightSensorPort, WheeledRobotConfiguration ExpressBot, RobotInfo info){
=======
*/
	private RobotInfo info = new RobotInfo();
	private RobotCommunicationHandler comms;
    private boolean m_run = true;
    private RobotInterface robot_interface;
    boolean orange = Button.ENTER.isPressed();
	boolean esc = Button.ESCAPE.isPressed();
	boolean right = Button.RIGHT.isPressed();
	boolean left = Button.LEFT.isPressed();
    
	public Robot(SensorPort leftSensorPort, SensorPort rightSensorPort, WheeledRobotConfiguration ExpressBot) {
//>>>>>>> Stashed changes
		LightSensor rightSensor = new LightSensor(rightSensorPort);
		LightSensor leftSensor = new LightSensor(leftSensorPort);
		DifferentialPilot pilot = new WheeledRobotSystem(ExpressBot).getPilot();
		this.movement = new Movement(leftSensor, rightSensor, pilot);
		this.info = info;
	}
	
	@Override
	public void run() {	
		
		while(!this.comms.getStatus().equals("CONNECTED")){
			robot_interface.bluetoothMessage();
		}
		this.comms.run();
		System.out.println(this.comms.getStatus());
		
		while(m_run){
			this.receiveInfo();
			int number  = 0;
		//	printInfo();
			
			Direction direction = info.move();
			if(direction != null){
				movement.move(direction);
				robot_interface.movingToPickup(info.getCurrentJob().getJobCode(),info.getCurrentJob().getItem(),info.getCurrentJob().getPosition());
			
			
			}
			else if(!info.finished()){
				Button.waitForAnyPress();
				//robot_interface.load(
				LCD.clear();
				LCD.drawString("Please press ENTER to confirm the pick up location", 1, 0);
				robot_interface.loadItems();
				
				}
				LCD.clear();
				while(info.getCurrentJob().getQuantity() != number){
					robot_interface.load(info.getCurrentJob().getItem(),info.getCurrentJob().getQuantity(), number);
					if(left){
						Delay.msDelay(100);
						number--;
						robot_interface.status(number);
					}
					if(right){
						Delay.msDelay(100);
						number++;
						robot_interface.status(number);
					}
					
					//info.click();
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
	
<<<<<<< Updated upstream
	private void printInfo(){
		System.out.println(info.getName());
		System.out.println("Current job code: " + info.getCurrentJob().getJobCode());
		System.out.println("Destination: " + "(" + info.getPosition().getX() + ", " + info.getPosition().getY() + ")");
		System.out.println("Items left to pick: " + info.getCurrentJob().getQuantity());
	}
=======
//	private void printInfo(){
//		JobInfo job = info.getCurrentJob();
//		LCD.clear();
//		System.out.println(info.getName());
//		System.out.println("Current job code: " + job.getJobCode());
//		System.out.println("Destination: " + "(" + job.getPosition().getX() + ", " + job.getPosition().getY() + ")");
//		System.out.println("Items left to pick: " + info.getCurrentJob().getQuantity());
//	}
>>>>>>> Stashed changes

	public static void main(String[] args) {
<<<<<<< Updated upstream
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
		JobInfo job = new JobInfo("", new Point(11, 6), 4, 123);
		info.setCurrentJob(job, path);
		WheeledRobotConfiguration config = 
		new WheeledRobotConfiguration(RobotConfigs.EXPRESS_BOT.getWheelDiameter(), RobotConfigs.EXPRESS_BOT.getTrackWidth(), (float) RobotConfigs.EXPRESS_BOT.getRobotLength(), Motor.C, Motor.B);
		RobotProgrammingDemo demo = new Robot(SensorPort.S2, SensorPort.S3, config, info);
=======
//		LinkedList<Integer> p = new LinkedList<Integer>();
//		int direction = Button.waitForAnyPress();
//		while (direction != Button.ID_ENTER) {
//			p.add(direction);
//			direction = Button.waitForAnyPress();
//		}

//		RobotInfo info = new RobotInfo("xd", new Point(0, 0), Direction.FORWARD);
//		LinkedList<Point> path = new LinkedList<Point>();
//		path.add(new Point(1, 0));
//		path.add(new Point(2, 0));
//		path.add(new Point(3, 0));
//		path.add(new Point(4, 0));
//		path.add(new Point(5, 0));
//		path.add(new Point(6, 0));
//		path.add(new Point(6, 1));
//		path.add(new Point(6, 2));
//		path.add(new Point(6, 3));
//		path.add(new Point(6, 4));
//		path.add(new Point(6, 5));
//		path.add(new Point(6, 6));
//		path.add(new Point(7, 6));
//		path.add(new Point(8, 6));
//		path.add(new Point(9, 6));
//		path.add(new Point(10, 6));
//		path.add(new Point(11, 6));
//		path.add(new Point(11, 7));
//		path.add(new Point(11, 6));
//		JobInfo job = new JobInfo("", new Point(11, 6));
//		info.setCurrentJob(job, path);
		WheeledRobotConfiguration config = new WheeledRobotConfiguration(RobotConfigs.EXPRESS_BOT.getWheelDiameter(),
				RobotConfigs.EXPRESS_BOT.getTrackWidth(), (float) RobotConfigs.EXPRESS_BOT.getRobotLength(), Motor.C,
				Motor.B);
		RobotProgrammingDemo demo = new Robot(SensorPort.S2, SensorPort.S3, config);
>>>>>>> Stashed changes
		demo.run();
	}

}
