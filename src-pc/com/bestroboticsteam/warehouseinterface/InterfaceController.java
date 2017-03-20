package com.bestroboticsteam.warehouseinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.Logger;

import com.bestroboticsteam.communication.PCConnectionHandler;
import com.bestroboticsteam.jobs.*;
import com.bestroboticsteam.robotsmanagement.RobotsManager;

public class InterfaceController extends Thread {
	final static Logger logger = Logger.getLogger(InterfaceController.class);
	private InterfaceView warehouseInterface;
	private JobSelection incomingJobs;
	private JobAssignment assign;
	private PCConnectionHandler connection;
	private RobotsManager robots;
	private ConcurrentMap<Integer, Order> tenJobsMap = new ConcurrentHashMap<Integer, Order>();
	private ConcurrentMap<Integer, Order> progJobsMap = new ConcurrentHashMap<Integer, Order>();
	
	public InterfaceController(JobSelection incomingJobs, JobAssignment assign, RobotsManager robots) {
		this.robots = robots;
		this.warehouseInterface = new InterfaceView(robots);
		this.incomingJobs = incomingJobs;
		this.assign = assign;
		this.warehouseInterface.addCancelListener(new cancelListener());
	}

/*	public void setRobotStatus() {
		String status = connection.getStatus();
		warehouseInterface.commLabel.setText(status);
		//change this
		
	}*/
	
	public void setFinishedJobs(){
		String jobsText = "No jobs have been completed";
		for (int i = 0; i < 5; i++){
			if(assign.viewFinishedOrder(i) != null){
				Order job = assign.viewFinishedOrder(i);
				jobsText = jobsText + " : " + job.toString();
			}	
		}
		warehouseInterface.setFinishedList(jobsText);
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
		logger.debug(jobsText);
	}

	public void setTenJobs() {
		// get input for jobsList
		// get the first ten jobs from JobSelection and output them to displayText in IView
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
			//	setRobotStatus();
				setTenJobs();
				setCurrentJobs();
				setFinishedJobs();
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				logger.error("InterfaceController thread has been interrupted");
			}
			warehouseInterface.emptyJobList();
		}
	}

	public class cancelListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == warehouseInterface.cancel) {
				logger.info("cancel1 button has been pressed");
				if(warehouseInterface.text3.getText()  == null){
					logger.error("No job has been inputted - cannot cancel" );
				} else {
					String text = warehouseInterface.text3.getText();
					int itemID = Integer.parseInt(text);
					incomingJobs.cancelOrder(itemID);
					tenJobsMap.remove(itemID);
					warehouseInterface.text3.setText("");
				}
			} else if (e.getSource() == warehouseInterface.cancel2) {
				logger.info("cancel2 button has been pressed");
				if(warehouseInterface.text4.getText()  == null){
					logger.error("No job has been inputted - cannot cancel" );
				} else {
					String text = warehouseInterface.text4.getText();
					int itemID = Integer.parseInt(text);
					assign.cancelOrder(itemID);
					progJobsMap.remove(itemID);
					warehouseInterface.text4.setText("");
				}
			}
		}
	}
}