package com.bestroboticsteam.jobs;

public class JobInfo {
	
	private Item item;
	private int quantaty;
	private int jobCode;
	
	public JobInfo(Item _item, int _quantaty, int _jobCode){
		this.item = _item;
		this.quantaty = _quantaty;
		this.jobCode = jobCode;
	}
	
	public Item getItem(){
		return item;
	}
	
	public int getQuantaty(){
		return quantaty;
	}
	
	public int getJobCode(){
		return jobCode;
	}
	
}
