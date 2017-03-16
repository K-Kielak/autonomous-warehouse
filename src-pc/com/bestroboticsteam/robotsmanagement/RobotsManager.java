package com.bestroboticsteam.robotsmanagement;

import com.bestroboticsteam.jobs.JobAssignment;
import com.bestroboticsteam.jobs.JobInfo;
import com.bestroboticsteam.pathfinding.AStar;

import rp.util.Pair;

import java.awt.Point;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class RobotsManager extends Thread{

	private final int DELAY = 500;
	private Robot[] robots;
	private JobAssignment jobs;

	final Logger logger = Logger.getLogger(RobotsManager.class);

	public RobotsManager(RobotInfo[] robotInfos, JobAssignment jobs) {
		this.robots = new Robot[robotInfos.length];
		for (int i = 0; i < robotInfos.length; i++)
			this.robots[i] = new Robot(robotInfos[i]);

		this.jobs = jobs;
		logger.info("robots manager initialised");
	}

	public void run() {
		for (int i = 0; i < robots.length; i++){
			robots[i].start();
			logger.info("robot " + robots[i].getInfo().getName() + " initialised");
		}
		
		while(true){
			for (int i = 0; i < robots.length; i++){
				if(robots[i].getInfo().finished()){
					JobInfo job = jobs.getNextJob();
					RobotInfo currRobotInfo = robots[i].getInfo();
					//RobotInfo[] otherRobotsInfos = getOtherRobotsInfos(currRobotInfo);
					Point start = currRobotInfo.getPosition();
					Point goal = job.getPosition();
					//TODO change to multi get path
					LinkedList<Point> path = AStar.singleGetPath(Pair.makePair(start, goal));
					robots[i].assignNewJob(job, path);
				}
			}
			
			try {
				Thread.sleep(DELAY);
			} catch (InterruptedException e) {
				logger.error(e.getMessage());
			}
		}
	}

	public RobotInfo[] getRobotInfos() {
		RobotInfo[] robotInfos = new RobotInfo[robots.length];
		for(int i=0; i<robots.length; i++)
			robotInfos[i] = robots[i].getInfo();
		
		return robotInfos;
	}
	
	private RobotInfo[] getOtherRobotsInfos(RobotInfo robotInfo){
		RobotInfo[] robotInfos = getRobotInfos();
		RobotInfo[] otherRobotsInfos = new RobotInfo[robotInfos.length-1];
		int othersI = 0;
		for(int i=0; i<robots.length; i++){
			if(robotInfos[i] != robotInfo){
				otherRobotsInfos[othersI] = robotInfos[i];
				othersI++;
			}
		}
		
		return otherRobotsInfos;
	}

}