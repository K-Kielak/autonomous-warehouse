package com.bestroboticsteam.jobs;

import java.awt.Point;
import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyRobotInfo {
	
	private final float MAX_WEIGHT;
	private float weight;
	private Point position;
	private BlockingQueue<JobInfo> jobPath = new LinkedBlockingQueue<JobInfo>();
	private int totalCost;
	private int numberJobsAssigned;
	private JobInfo currentJob = null;
	private Order currentOrder = null;
	
	public MyRobotInfo(float maxWeight, float weight, Point position){
		this.MAX_WEIGHT = maxWeight;
		this.position = position;
		this.weight = weight;
		this.totalCost = 0;
		this.numberJobsAssigned = 0;
	}
	
	public void setCurrentOrder(Order o){
		currentOrder = o;
	}
	
	public Order getCurrentOrder(){
		return currentOrder;
	}
	
	public void setCurrentJob(JobInfo info){
		currentJob = info;
	}
	
	public JobInfo getCurrentJob(){
		return currentJob;
	}
	
	public void setWeight(float weight){
		this.weight = weight;
	}
	
	public void setPosition(Point position){
		this.position = position;
	}
	
	public void addJobPath(LinkedList<JobInfo> list){
		jobPath.addAll(list);
	}
	
	public float getWeight(){
		return weight;
	}
	
	public float getMaxWeight(){
		return MAX_WEIGHT;
	}
	
	public Point getPosition(){
		return position;
	}
	
	public synchronized int getCost(){
		return totalCost;
	}
	
	public int getNumberAssigned(){
		return numberJobsAssigned;
	}
	
	public void decrementNumberAssigned(){
		numberJobsAssigned--;
	}
	
	public void incementNumberAssigned(){
		numberJobsAssigned++;
	}
	
	public synchronized void setCost(int cost){
		this.totalCost = cost;
	}
	
	public JobInfo getNextJob(){
		while(true){
			try {
				return jobPath.take();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void cancelOrder(int code){
		
		boolean removed = false;
		
		for(JobInfo info: jobPath){
			if(info.getJobCode() == code){
				jobPath.remove(info);
				removed = true;
			}
		}
		
		if(removed)
			this.decrementNumberAssigned();
		
		
		if(this.currentOrder != null)
			if(this.currentOrder.getId() == code)
				this.currentOrder = null;
		
		if(this.currentJob != null)
			if(this.currentJob.getJobCode() == code)
				this.currentJob = null;
		
	}
}
