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
		String jobsText = "";
		for (int i = 0; i < 10; i++) {
			logger.debug("index position " + i);
			Order job = incomingJobs.viewOrder(i);
			logger.debug("job: " + job);
			if(job == null){
				logger.debug("not enough jobs");
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
				Thread.sleep(1000);
				// while running keep updating jobs
				getTenJobs();
				InterfaceController.sleep(5000);
				// empty the string jobListText so that an updated 10 items can be added
			} catch (InterruptedException e) {
				logger.error("InterfaceController thread has been interrupted");
			}
			warehouseInterface.emptyJobList();
		}
	}
}