//TODO integrate

package com.bestroboticsteam.robotsmanagement;

import java.awt.Point;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import rp.util.Pair;

import com.bestroboticsteam.jobs.JobAssignmentTest;
import com.bestroboticsteam.jobs.JobInfo;
import com.bestroboticsteam.pathfinding.AStar;
import com.bestroboticsteam.communication.PCConnectionHandler;

public class RobotsManager extends Thread {

	private final int MS_DELAY = 500;
	private RobotInfo[] robots;
	private PCConnectionHandler[] connectionHandlers;
	private JobAssignmentTest jobs;
	private AStar pathFinder;

	final Logger logger = Logger.getLogger(RobotsManager.class);

	public RobotsManager(RobotInfo[] robots, JobAssignment jobs, AStar pathFinder) {
		this.robots = robots;
		this.connectionHandlers = new PCConnectionHandler[robots.length];
		for (int i = 0; i < robots.length; i++)
			this.connectionHandlers[i] = new PCConnectionHandler(robots[i].name);

		this.jobs = jobs;
		this.pathFinder = pathFinder;
	}

	public void run() {
		for (RobotInfo r : robots) {
			if (r.finished()) {
				JobInfo nextJob = jobs.getNextJob();
				LinkedList<Point> path = AStar.singleGetPath(Pair.makePair(r.getPosition(), nextJob.getPosition()));
				r.setCurrentJob(nextJob, path);
			}
		}
	
		//TODO communication

		try {
			Thread.sleep(MS_DELAY);
		} catch (InterruptedException e) {
			System.out.println(e.getMessage());
		}
	}

	public RobotInfo[] getRobots() {
		return robots;
	}

}