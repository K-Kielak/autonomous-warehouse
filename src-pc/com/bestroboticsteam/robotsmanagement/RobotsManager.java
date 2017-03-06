//TODO integrate

package com.bestroboticsteam.robotsmanagement;

import java.awt.Point;
import java.util.LinkedList;

import com.bestroboticsteam.communication;
import com.bestroboticsteam.jobs;
import com.bestroboticsteam.pathfinding;

public class RobotsManager extends Thread{
	
	private final int MS_DELAY = 500;
	private RobotInfo[] robots;
	private PCConnectionHandler[] connectionHandlers;
	private JobAssignment jobs;
	private PathFinder pathFinder;
	
	public RobotsManager(String[] robotNames, JobAssignment jobs, PathFinder pathFinder){
		this.robots = new RobotInfo[robotNames.length];
		this.connectionHandlers = new PCConnectionHandler[robotNames.length];
		for(int i=0; i<robotNames.length; i++){
			this.robots[i] = new RobotInfo(robotNames[i]);
			this.connectionHandlers[i] = new PCConnectionHandler(robotNames[i]);
		}
		
		this.jobs = jobs;
		this.pathFinder = pathFinder;
	}
	
	public void run(){
		for(RobotInfo r: robots){
			if(r.finished()){
				JobInfo nextJob = jobs.getNextJob();
				LinkedList<Point> path = pathFinder.getPath(r.getPosition(), nextJob.getPosition());
				r.setCurrentJob(nextJob, path);
			}
			
			//TODO communication with robots
		}
			
		Thread.sleep(MS_DELAY);
	}
	
	public RobotInfo[] getRobots(){
		return robots;
	}
	
}
