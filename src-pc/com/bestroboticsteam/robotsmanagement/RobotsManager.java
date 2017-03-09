//TODO integrate

package com.bestroboticsteam.robotsmanagement;

import java.awt.Point;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import rp.util.Pair;

import com.bestroboticsteam.jobs.JobAssignment;
import com.bestroboticsteam.jobs.JobInfo;
import com.bestroboticsteam.pathfinding.AStar;
import com.bestroboticsteam.communication.ConnectionNotEstablishedException;
import com.bestroboticsteam.communication.PCConnectionHandler;

public class RobotsManager extends Thread {

	private final int MS_DELAY = 500;
	private RobotInfo[] robots;
	private PCConnectionHandler[] connectionHandlers;
	private JobAssignment jobs;
	private AStar pathFinder;

	final Logger logger = Logger.getLogger(RobotsManager.class);

	public RobotsManager(RobotInfo[] robots, JobAssignment jobs, AStar pathFinder) {
		this.robots = robots;
		this.connectionHandlers = new PCConnectionHandler[robots.length];
		for (int i = 0; i < robots.length; i++)
			this.connectionHandlers[i] = new PCConnectionHandler(robots[i].getName());

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
		
		// TODO Check connection status

		for (int i = 0; i < connectionHandlers.length; i++) {
			connectionHandlers[i].run();
			try {
				connectionHandlers[i].sendObject(robots[i]);
			} catch (ConnectionNotEstablishedException e) {
				logger.error("Connection to robot " + i + " not established", e);
			}
		}
		for (int i = 0; i < connectionHandlers.length; i++) {
			try {
				connectionHandlers[i].receiveObject(robots[i]);
			} catch (ConnectionNotEstablishedException e) {
				logger.error("Connection to robot " + i + " not established", e);
			} // TODO Fix blocking
		}

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