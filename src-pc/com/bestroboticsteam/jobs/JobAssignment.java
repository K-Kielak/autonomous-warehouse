package com.bestroboticsteam.jobs;

public class JobAssignment {
	
	private final JobSelection selection;

	public JobAssignment(JobSelection selection){
		this.selection = selection;
	}
	
	public JobInfo getNextOrder(){
		Order nextOrder = selection.take();
		//this.orderItem
		//this.getInfoJob
		return null;
	}
	
	
}
