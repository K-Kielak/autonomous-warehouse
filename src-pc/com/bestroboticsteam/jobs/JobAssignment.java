package com.bestroboticsteam.jobs;

import java.util.LinkedList;


public class JobAssignment {
	
	private final JobSelection selection;
	
	private LinkedList<JobInfo> jobPath = new LinkedList<JobInfo>();
	private LinkedList<Order> currentOrders = new LinkedList<Order>();
	
	public JobAssignment(JobSelection selection){
		this.selection = selection;
	}
	
	public JobInfo getNextJob(){
		
		if(jobPath.isEmpty())
			setInfoJobs();
		
		JobInfo info = jobPath.getFirst();
		jobPath.removeFirst();
		
		return info;
		
	}
	
	private void setInfoJobs(){
		Order nextOrder = selection.take();
		currentOrders.add(nextOrder);
		
		jobPath = nextOrder.toJobInfos();
	}
	
	public LinkedList<Order> getCurrentOrders(){
		return currentOrders;
	}
	
	public void removeFromCurrentOrder(Order order){
		currentOrders.remove(order);
	}
	
}
