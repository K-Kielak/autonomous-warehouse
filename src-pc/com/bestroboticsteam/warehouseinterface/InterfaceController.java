package com.bestroboticsteam.warehouseinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.Logger;
import com.bestroboticsteam.jobs.*;
import com.bestroboticsteam.robotsmanagement.RobotInfo;
import com.bestroboticsteam.robotsmanagement.RobotsManager;

public class InterfaceController extends Thread {
	final static Logger logger = Logger.getLogger(InterfaceController.class);
	private InterfaceView warehouseInterface;
	private JobSelection incomingJobs;
	private JobAssignment assign;
	private RobotsManager robots;
	private ConcurrentMap<Integer, Order> tenJobsMap = new ConcurrentHashMap<Integer, Order>();
	private ConcurrentMap<Integer, Order> progJobsMap = new ConcurrentHashMap<Integer, Order>();
	private static RobotInfo[] robotArray;
	private float reward = 0.0f;
	public InterfaceController(JobSelection incomingJobs, JobAssignment assign, RobotsManager robots) {
		this.robots = robots;
		this.warehouseInterface = new InterfaceView(robots);
		this.incomingJobs = incomingJobs;
		this.assign = assign;
		this.warehouseInterface.addCancelListener(new cancelListener());
		robotArray = robots.getRobotInfos();
	}

	public void setRobotStatus() {
		String robotInfo = "";
		// String status = "";
		for (int i = 0; i < robotArray.length; i++) {
			int jobId = 0;
			String robot = robotArray[i].getName();
			int posx = CreateSimRobots.getPosX(i);
			int posy = CreateSimRobots.getPosY(i);
			jobId = robotArray[i].getCurrentJob().getJobCode();
			int goalx = CreateSimRobots.getGoalPoint(i).x;
			int goaly = CreateSimRobots.getGoalPoint(i).y;
			if (jobId != 0 && goalx != -1 && goaly != -1) {
				robotInfo = robot + " - " + "(" + posx + "," + posy + ") " + jobId + " Goal: " + "(" + goalx + "," + goaly + 
						") " + " : " + robotInfo;
			} else if (jobId != 0 && goalx == -1 && goaly == -1){
				robotInfo = robot + " - " + "(" + posx + "," + posy + ") " + jobId + " Goal is unknown " + " : " + robotInfo;
			} else {
				robotInfo = robot + " - " + "(" + posx + "," + posy + ") " + "Disconnected" + " : " + robotInfo;
			}
		}
		warehouseInterface.setStatusText(robotInfo);
	}
	
	public void setRobotWeight() {
		String robotInfo = "";
		// String status = "";
		for (int i = 0; i < robotArray.length; i++) {
			String robot = robotArray[i].getName();
			float maxWeight = robotArray[i].getMaxCapacity();
			float current = robotArray[i].getCurrentLoad();
			robotInfo = robot + " - " + "Current: " + current +  " Max -> " + maxWeight + " : " + robotInfo;
		}
		warehouseInterface.setWeightText(robotInfo);
	}

	public void setFinishedJobs() {
		String jobsText = "";
		for (int i = 0; i < 5; i++) {
			if (assign.viewFinishedOrder(i) != null) {
				Order job = assign.viewFinishedOrder(i);
				reward += assign.viewFinishedOrder(i).getTotalReward();
				jobsText = jobsText + " : " + job.toString();
			}
		}
		warehouseInterface.setReward(getReward());
		warehouseInterface.setFinishedList(jobsText);
	}
	
	public float getReward(){
		return reward;
	}

	public void setCurrentJobs() {
		String jobsText = "";
		int length = assign.getCurrentOrders().size();
		if (length == 0) {
			jobsText = "No jobs are currently in progress";
		} else {
			for (int i = 0; i < length; i++) {
				warehouseInterface.emptyProgList();
				Order job = assign.getCurrentOrders().get(i);
				if (job == null) {
					logger.error("No jobs in progress");
					break;
				} else {
					jobsText = jobsText + " : " + job.toString();
					int jobID = job.getId();
					progJobsMap.put(jobID, job);
				}
			}
		}
		warehouseInterface.setInProgList(jobsText);
	}

	public void setUpcomingjobs() {
		// get input for jobsList
		String jobsText = "";
		LinkedList<Order> jobs = assign.getAssignedOrders();
		for (int i = 0; i < jobs.size(); i++) {
			Order job = jobs.get(i);
			if (job == null) {
				logger.debug("Not enough jobs left");
				break;
			}
			String inputJob = job.toString();
			int jobID = job.getId();
			tenJobsMap.put(jobID, job);
			jobsText = jobsText + " : " + inputJob;
		}
		warehouseInterface.setJobList(jobsText);
	}

	public void run() {
		logger.info("warehouse interface running");
		while (true) {
			try {
				// while running keep updating jobs
				setRobotStatus();
				setRobotWeight();
				setUpcomingjobs();
				setCurrentJobs();
				setFinishedJobs();
				Thread.sleep(500);
			} catch (InterruptedException e) {
				logger.error("InterfaceController thread has been interrupted");
			}
			warehouseInterface.emptyJobList();
		}
	}

	public class cancelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == warehouseInterface.cancelUpcoming) {
				logger.info("cancel upcoming job button has been pressed");
				if (warehouseInterface.cancelTextU.getText() == null) {
					logger.error("No job has been inputted - cannot cancel");
				} else {
					String text = warehouseInterface.cancelTextU.getText();
					int itemID = Integer.parseInt(text);
					assign.cancelOrder(itemID);
					tenJobsMap.remove(itemID);
					warehouseInterface.cancelTextU.setText("");
				}
			} else if (e.getSource() == warehouseInterface.cancelCurrent) {
				logger.info("cancel current job button has been pressed");
				if (warehouseInterface.cancelTextC.getText() == null) {
					logger.error("No job has been inputted - cannot cancel");
				} else {
					String text = warehouseInterface.cancelTextC.getText();
					int itemID = Integer.parseInt(text);
					assign.cancelOrder(itemID);
					progJobsMap.remove(itemID);
					warehouseInterface.cancelTextC.setText("");
				}
			}
		}
	}
}