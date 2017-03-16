package com.bestroboticsteam.robotsmanagement;

import java.awt.Point;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.bestroboticsteam.communication.ConnectionNotEstablishedException;
import com.bestroboticsteam.communication.PCConnectionHandler;
import com.bestroboticsteam.jobs.JobAssignment;
import com.bestroboticsteam.jobs.JobInfo;
import com.bestroboticsteam.pathfinding.AStar;

import rp.util.Pair;

public class Robot extends Thread{
	private final int DELAY = 500;
	private RobotInfo info;
	private JobAssignment jobs;
	private PCConnectionHandler connectionHandler;
	
	final Logger logger = Logger.getLogger(Robot.class);
	
	public Robot(RobotInfo info, JobAssignment jobs){
		this.info = info;
		this.jobs = jobs;
		this.connectionHandler = new PCConnectionHandler(info.getName());
	}
	
	public void run(){
		connectionHandler.run();
		while(true){
			if(info.finished()){
				logger.info(info.getName() + " finished his job, assigning new one");
				JobInfo nextJob = jobs.getNextJob();
				LinkedList<Point> path = AStar.singleGetPath(Pair.makePair(info.getPosition(), nextJob.getPosition()));
				info.setCurrentJob(nextJob, path);
			}
		
			logger.info("Sending information to robot " + info.getName());
			try {
				connectionHandler.sendObject(info);
			} catch (ConnectionNotEstablishedException e) {
				logger.error("Connection to robot " + info.getName() + " not established", e);
			}
	
			logger.info("Receiving information from robot " + info.getName());
			try {
				connectionHandler.receiveObject(info);
			} catch (ConnectionNotEstablishedException e) {
				logger.error("Connection to robot " + info.getName() + " not established", e);
			}
	
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
	}
	
	public synchronized RobotInfo getInfo(){
		return info;
	}
}
