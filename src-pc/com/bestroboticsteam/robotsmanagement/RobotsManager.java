package com.bestroboticsteam.robotsmanagement;

import com.bestroboticsteam.jobs.JobAssignment;

import org.apache.log4j.Logger;

public class RobotsManager extends Thread{

	private Robot[] robots;

	final Logger logger = Logger.getLogger(RobotsManager.class);

	public RobotsManager(RobotInfo[] robotInfos, JobAssignment jobs) {
		this.robots = new Robot[robotInfos.length];
		for (int i = 0; i < robotInfos.length; i++)
			this.robots[i] = new Robot(robotInfos[i], jobs, getOtherRobotInfos(robotInfos[i], robotInfos));

		logger.info("robots manager initialised");
	}

	public void run() {
		for (int i = 0; i < robots.length; i++){
			robots[i].setName(robots[i].getInfo().getName()); //setting thread name for debugging purposes
			robots[i].start();
			logger.info("robot " + robots[i].getInfo().getName() + " initialised");
		}
	}

	public RobotInfo[] getRobotInfos() {
		RobotInfo[] robotInfos = new RobotInfo[robots.length];
		for(int i=0; i<robots.length; i++)
			robotInfos[i] = robots[i].getInfo();
		
		return robotInfos;
	}
	
	private RobotInfo[] getOtherRobotInfos(RobotInfo robotInfo, RobotInfo[] robotInfos){
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