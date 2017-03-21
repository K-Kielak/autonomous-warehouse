package com.bestroboticsteam.robotsmanagement;

import java.awt.Point;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.bestroboticsteam.communication.ConnectionNotEstablishedException;
import com.bestroboticsteam.communication.PCConnectionHandler;
import com.bestroboticsteam.jobs.JobAssignment;
import com.bestroboticsteam.pathfinding.AStar;

import rp.util.Pair;

public class Robot extends Thread{
	private final int DELAY = 500;
	private RobotInfo info;
	private JobAssignment jobs;
	private RobotInfo[] otherRobotInfos;
	private PCConnectionHandler connectionHandler;
	
	final Logger logger = Logger.getLogger(Robot.class);
	
	public Robot(RobotInfo info, JobAssignment jobs, RobotInfo[] otherRobotInfos){
		this.info = info;
		this.jobs = jobs;
		this.otherRobotInfos = otherRobotInfos;
		this.connectionHandler = new PCConnectionHandler(info.getName());
	}
	
	public void run(){
		connectionHandler.run();
		while(true){
			if(info.wasJobCancelled()){
				logger.info("Job " + info.getCurrentJob().getJobCode() + "was cancelled on the robot side");
				jobs.cancelOrder(info.getCurrentJob().getJobCode());
			}
			else if(!jobs.isCurrentJob(info.getCurrentJob().getJobCode())){
				logger.info("Job " + info.getCurrentJob().getJobCode() + "was cancelled on the server side");
				info.cancelJob();
			}
			
			if(info.wasJobCancelled() || info.finished()){
				info.setCurrentJob(jobs.getNextJob(info.getName()));
				logger.info("Got new job: " + info.getCurrentJob().getJobCode());
			}
			
			recalculatePath();
		
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
	
	public synchronized RobotInfo getInfo(){
		return info;
	}
	
	private synchronized void recalculatePath(){
		Point start = info.getPosition();
		Point goal = info.getCurrentJob().getPosition();
		Pair<Point, Point> startGoalPair = Pair.makePair(start, goal);
		LinkedList<Point> path = null;
		while(path == null){
			logger.info("Robot " + info.getName() + " is waiting for a new path (goal is not accessible now)");
			path = AStar.multiGetPath(startGoalPair, otherRobotInfos);
			try {
				Thread.sleep(2*DELAY);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
		
		info.setCurrentPath(path);
	}
}
