package com.bestroboticsteam.jobs;

import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.bestroboticsteam.jobs.JobInfo;

public class JobAssignment {

	private final JobSelection selection;
	
	final Logger logger = Logger.getLogger(JobAssignment.class);

	//jobPath will store a collections of subJobs(resulted from breaking an Order) 
	private LinkedList<JobInfo> jobPath = new LinkedList<JobInfo>();
	private LinkedList<Order> currentOrders = new LinkedList<Order>();

	public JobAssignment(JobSelection selection) {
		this.selection = selection;
	}

	public JobInfo getNextJob() {

		if (jobPath.isEmpty())
			setInfoJobs();

		return jobPath.pop();
	}

	private void setInfoJobs(){
		//In case there is no subJob in the list, get the next Order and break it
		Order nextOrder = selection.take();
		if(nextOrder == null){
			logger.info("No more jobs!");
		}
		
		currentOrders.add(nextOrder);
		jobPath = nextOrder.toJobInfos();
		jobPath.add(new JobInfo("DropBox", selection.getDropLocation().getFirst()));
	}

	public LinkedList<Order> getCurrentOrders() {
		return currentOrders;
	}

	public void removeFromCurrentOrder(Order order) {
		currentOrders.remove(order);
	}

}
