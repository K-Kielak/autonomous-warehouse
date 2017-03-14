package com.bestroboticsteam.warehouseinterface;
import org.apache.log4j.Logger;

import com.bestroboticsteam.communication.PCConnectionHandler;
import com.bestroboticsteam.jobs.*;

public class InterfaceController extends Thread {
	final static Logger logger = Logger.getLogger(InterfaceController.class);
	private InterfaceView warehouseInterface;
	private JobSelection incomingJobs;
	private JobAssignment assign;
	private PCConnectionHandler connection;

	public InterfaceController(JobSelection incomingJobs, JobAssignment assign) {
		this.warehouseInterface = new InterfaceView();
		this.incomingJobs = incomingJobs;
		this.assign = assign;
	}
	
	public void setRobotStatus(){
		String status = connection.getStatus();
		warehouseInterface.commLabel.setText(status);
	}
	
	public void setCurrentJobs(){
		String jobsText = "";
		int length = assign.getCurrentOrders().size();
		logger.debug(length);
		if (length == 0){
			jobsText = "No jobs are currently in progress";
		} else { 
			for (int i = 0; i < length; i++){
				warehouseInterface.emptyProgList();
				Order job = assign.getCurrentOrders().get(i);
				if (job == null){
					logger.error("No jobs left");
					break;
				} else {
					jobsText = jobsText + " : " + job.toString();
				}
			}
		}
		warehouseInterface.setInProgList(jobsText);
		logger.debug(jobsText);
	}
	
	public void setTenJobs() {
		//get input for jobsList
		//get the first ten jobs from JobSelection and output them to displayText in IView
		String jobsText = "";
		for (int i = 0; i < 10; i++) {
			logger.debug("index position " + i);
			Order job = incomingJobs.viewOrder(i);
			logger.debug("job: " + job);
			if(job == null){
				logger.debug("Not enough jobs left");
				break;
			}
			String inputJob = job.toString();
			jobsText = jobsText + " : " + inputJob;
		}		
		warehouseInterface.setJobList(jobsText);
		logger.debug("get jobs list " + jobsText);
	}
	
	public void run() {
		logger.info("warehouse interface running");
		while (true) {
			try {			
				// while running keep updating jobs
				setRobotStatus();
				setTenJobs();
				setCurrentJobs();
				Thread.sleep(5000);
				// empty the string jobListText so that an updated 10 items can be added
				
			} catch (InterruptedException e) {
				logger.error("InterfaceController thread has been interrupted");
			}
			warehouseInterface.emptyJobList();
		}
	}
}