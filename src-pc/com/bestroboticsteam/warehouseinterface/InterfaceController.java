package com.bestroboticsteam.warehouseinterface;

import org.apache.log4j.Logger;
import com.bestroboticsteam.jobs.*;

public class InterfaceController extends Thread {
	final static Logger logger = Logger.getLogger(InterfaceController.class);
	private InterfaceView warehouseInterface;
	private JobSelection incomingJobs;

	public InterfaceController(JobSelection incomingJobs) {
		this.warehouseInterface = new InterfaceView();
		this.incomingJobs = incomingJobs;
	}
	
	public void getTenJobs() {
		//get input for jobsList
		//get the first ten jobs from JobSelection and output them to displayText in IView
		logger.debug(incomingJobs);
		for (int i = 0; i < 10; i++) {
			logger.debug("index position " + i);
			Order job = incomingJobs.viewOrder(i);
			logger.debug("order:  " + job);
			warehouseInterface.setJobList(job, i);
			logger.debug("get jobs list " + warehouseInterface.getJobList());
		}
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				// while running keep updating jobs
				getTenJobs();
				logger.debug("here");
				InterfaceController.sleep(5000);
				// empty the string jobListText so that an updated 10 items can be added
			} catch (InterruptedException e) {
				logger.error("InterfaceController thread has been interrupted");
			}
			warehouseInterface.emptyJobList();
		}
	}

	public static void main(String[] args) {
		//This needs changing to the path of the file that readOrderData uses
		String path = ""; 
		JobSelection jobs = new JobSelection(path);
		InterfaceController test = new InterfaceController(jobs);
		test.start();
		logger.info("Running");
	}
}