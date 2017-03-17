package com.bestroboticsteam.robotsmanagement;

import java.awt.Point;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.bestroboticsteam.communication.ConnectionNotEstablishedException;
import com.bestroboticsteam.communication.PCConnectionHandler;
import com.bestroboticsteam.jobs.JobInfo;

public class Robot extends Thread{
	private final int DELAY = 500;
	private RobotInfo info;
	private PCConnectionHandler connectionHandler;
	
	final Logger logger = Logger.getLogger(Robot.class);
	
	public Robot(RobotInfo info){
		this.info = info;
		this.connectionHandler = new PCConnectionHandler(info.getName());
	}
	
	public void run(){
		connectionHandler.run();
		while(true){
			//wait for assignment of new job
			while(info.finished()){
				logger.info(info.getName() + " waiting for job...");
			}
		
			logger.info("Sending information to robot " + info.getName());
			try {
				connectionHandler.sendObject(info);
			} catch (ConnectionNotEstablishedException e) {
				logger.error("Connection to robot " + info.getName() + " not established", e);
			}
			
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
	
			logger.info("Receiving information from robot " + info.getName());
			try {
				connectionHandler.receiveObject(info);
			} catch (ConnectionNotEstablishedException e) {
				logger.error("Connection to robot " + info.getName() + " not established", e);
			}
		}
	}
	
	public void assignNewJob(JobInfo job, LinkedList<Point> path){
		info.setCurrentJob(job, path);
	}
	
	public synchronized RobotInfo getInfo(){
		return info;
	}
}
