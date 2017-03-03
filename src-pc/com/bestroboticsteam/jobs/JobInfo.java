package com.bestroboticsteam.jobs;

import java.awt.Point;

public class JobInfo {
	
	private String itemCode;
	private Point position;
	private int quantity;
	private int jobCode;
	
	public JobInfo(String code, Point position, int _quantity, int _jobCode){
		
		this.itemCode = code;
		this.position = position;
		this.quantity = _quantity;
		this.jobCode = jobCode;
	}
	
	public String getItem(){
		return itemCode;
	}
	
	public Point getPosition(){
		return position;
	}
	
	public int getQuantity(){
		return quantity;
	}
	
	public int getJobCode(){
		return jobCode;
	}
	
}
