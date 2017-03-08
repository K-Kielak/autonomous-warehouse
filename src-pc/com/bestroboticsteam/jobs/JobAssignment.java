package com.bestroboticsteam.jobs;

import java.util.LinkedList;

import com.bestroboticsteam.jobs.JobInfo;

public class JobAssignment {

	private final JobSelection selection;

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

	private void setInfoJobs() throws NullPointerException{
		Order nextOrder = selection.take();
		if(nextOrder == null)
			throw new NullPointerException("there are no more jobs left");
		
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
