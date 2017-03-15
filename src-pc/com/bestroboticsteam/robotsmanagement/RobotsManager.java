//TODO integrate

package com.bestroboticsteam.robotsmanagement;

import com.bestroboticsteam.jobs.JobAssignment;
import com.bestroboticsteam.pathfinding.AStar;
import org.apache.log4j.Logger;

public class RobotsManager {

	//private final int MS_DELAY = 500;
	private Robot[] robots;
	private AStar pathFinder;

	final Logger logger = Logger.getLogger(RobotsManager.class);

	public RobotsManager(RobotInfo[] robotInfos, JobAssignment jobs, AStar pathFinder) {
		this.robots = new Robot[robotInfos.length];
		for (int i = 0; i < robotInfos.length; i++)
			this.robots[i] = new Robot(robotInfos[i], jobs);

		this.pathFinder = pathFinder;
		
		logger.info("robots manager initialised");
	}

	public void start() {
		for (int i = 0; i < robots.length; i++){
			this.robots[i].start();
			logger.info("robot " + robots[i].getInfo().getName() + " initialised");
		}
	}
	/*
		for (int i = 0; i < connectionHandlers.length; i++) {
			connectionHandlers[i].run();
		}

		while (true) {
			for (RobotInfo r : robots) {
				if (r.finished()) {
					logger.info(r.getName() + " finished his job, assigning new one");
					JobInfo nextJob = jobs.getNextJob();
					LinkedList<Point> path = AStar.singleGetPath(Pair.makePair(r.getPosition(), nextJob.getPosition()));
					r.setCurrentJob(nextJob, path);
				}
			}

			// TODO Check connection status

			logger.info("Sending information to robots");
			for (int i = 0; i < connectionHandlers.length; i++) {
				try {
					connectionHandlers[i].sendObject(robots[i]);
				} catch (ConnectionNotEstablishedException e) {
					logger.error("Connection to robot " + i + " not established", e);
				}
			}

			logger.info("Receiving information from robots");
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
				logger.error(e.getMessage());
			}
		}
	}*/

	public RobotInfo[] getRobotInfos() {
		RobotInfo[] robotInfos = new RobotInfo[robots.length];
		for(int i=0; i<robots.length; i++)
			robotInfos[i] = robots[i].getInfo();
		
		return robotInfos;
	}

}