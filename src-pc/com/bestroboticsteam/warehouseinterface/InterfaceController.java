package com.bestroboticsteam.warehouseinterface;

import org.apache.log4j.Logger;

import Job.*;

public class InterfaceController extends Thread {
	final static Logger logger = Logger.getLogger(InterfaceController.class);
	private InterfaceView warehouseInterface;
	public static JobSelection incomingJobs;

	public InterfaceController(JobSelection incomingJobs) {
		this.warehouseInterface = new InterfaceView();
		InterfaceController.incomingJobs = incomingJobs;
	}

	public void getTenJobs() {
		// get input for jobsList
		// get the first ten jobs from JobSelection and output them to displayText in IView
		for (int i = 0; i < 10; i++) {
			String tenJobs = warehouseInterface.getJobList() + "/n " + i + "/n" + incomingJobs.viewOrder(i);
			warehouseInterface.setJobList(tenJobs);
			logger.info("adding job number " + i);
		}
	}

	public void run() {
		while (true) {
			Thread.sleep(1000);
			// while running keep updating jobs - sleep for 1s between updates
			getTenJobs();
			try {
				InterfaceController.sleep(5000);
			} catch (InterruptedException e) {
				logger.error("InterfaceController thread has been interrupted");
			}
			// empty the string jobListText so that an updated 10 items can be added
			warehouseInterface.setJobList("");
		}
	}

	public static void main(String[] args) {
		InterfaceController test = new InterfaceController(incomingJobs);
		test.start();
		logger.info("Running");
	}

}