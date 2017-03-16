package com.bestroboticsteam.warehouseinterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.apache.log4j.Logger;

import com.bestroboticsteam.communication.PCConnectionHandler;
import com.bestroboticsteam.jobs.*;

public class InterfaceController extends Thread {
	final static Logger logger = Logger.getLogger(InterfaceController.class);
	private InterfaceView warehouseInterface;
	private JobSelection incomingJobs;
	private JobAssignment assign;
	private PCConnectionHandler connection;
	private ConcurrentMap<Integer, Order> tenJobsMap = new ConcurrentHashMap<Integer, Order>();
	private ConcurrentMap<Integer, Order> progJobsMap = new ConcurrentHashMap<Integer, Order>();
	
	public InterfaceController(JobSelection incomingJobs, JobAssignment assign) {
		this.warehouseInterface = new InterfaceView();
		this.incomingJobs = incomingJobs;
		this.assign = assign;
		this.warehouseInterface.addCancelListener(new cancelListener());
	}

	public void setRobotStatus() {
		String status = connection.getStatus();
		warehouseInterface.commLabel.setText(status);
	}

	public void setCurrentJobs() {
		String jobsText = "";
		int length = assign.getCurrentOrders().size();
		logger.debug(length);
		if (length == 0) {
			jobsText = "No jobs are currently in progress";
		} else {
			for (int i = 0; i < length; i++) {
				warehouseInterface.emptyProgList();
				Order job = assign.getCurrentOrders().get(i);
				if (job == null) {
					logger.error("No jobs left");
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
		for (int i = 0; i < 10; i++) {
			logger.debug("index position " + i);
			Order job = incomingJobs.viewOrder(i);
			logger.debug("job: " + job);
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
				logger.debug("cancel1 has been pressed");
				if(warehouseInterface.text3.getText()  == null){
					logger.error("No inputted job to cancel" );
				} else {
					String text = warehouseInterface.text3.getText();
					int itemID = Integer.parseInt(text);
					Order cancelJob = tenJobsMap.get(itemID);
					logger.debug(cancelJob);
					incomingJobs.cancelOrder(itemID);
					tenJobsMap.remove(itemID);
					warehouseInterface.text3.setText("");
				}
			} else if (e.getSource() == warehouseInterface.cancel2) {
				logger.debug("cancel2 has been pressed");
				if(warehouseInterface.text4.getText()  == null){
					logger.error("No inputted job to cancel" );
				} else {
					String text = warehouseInterface.text4.getText();
					int itemID = Integer.parseInt(text);
					Order cancelJob = progJobsMap.get(itemID);
					assign.removeFromCurrentOrder(cancelJob);
					assign.cancelOrder(itemID);
					progJobsMap.remove(itemID);
					warehouseInterface.text4.setText("");
				}
			}
		}
	}
}