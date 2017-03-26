package com.bestroboticsteam.jobs;

import java.awt.Point;
import java.util.LinkedList;

public class MyRobotInfo {
	
	private final float MAX_WEIGHT;
	private float weight;
	private Point position;
	private LinkedList<JobInfo> jobPath = new LinkedList<JobInfo>();
	private int totalCost;
	private int numberJobsAssigned;
	private JobInfo currentJob = null;
	private LinkedList<Order> currentOrders = new LinkedList<Order>();
	
	public MyRobotInfo(float maxWeight, float weight, Point position){
		this.MAX_WEIGHT = maxWeight;
		this.position = position;
		this.weight = weight;
		this.totalCost = 0;
		this.numberJobsAssigned = 0;
	}
	
	public void setCurrentOrders(LinkedList<Order> o){
		currentOrders = (LinkedList<Order>) o.clone();
	}
	
	public LinkedList<Order> getCurrentOrders(){
		return currentOrders;
	}
	
	public void setCurrentJob(JobInfo info){
		currentJob = info;
	}
	
	public JobInfo getCurrentJob(){
		return currentJob;
	}
	
	public JobInfo getLastJobInfoAssigned(){
		return jobPath.peekLast();
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
	
	public void addJobPath(JobInfo info){
		jobPath.add(info);
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
		return jobPath.poll();	
	}
	
	public int cancelOrder(int code){
		
		boolean removed = false;
		
		for(Order o: currentOrders){
			if(o.getId() == code){
				currentOrders.remove(o);
				break;
			}
		}
		
		for(JobInfo j: jobPath){
			if(j.getJobCode() == code){
				removed = true;
				break;
			}
		}
		
		if(removed){
			if(jobPath.peekLast().getJobCode() == code){
				
				boolean check = false;
				
				while(!check){
					check = true;
				
					for(JobInfo info: jobPath){
						if(info.getJobCode() == code){
							jobPath.remove(info);
							check = false;
							break;
						}
					}
				}
					
				if(this.currentJob != null)
					if(this.currentJob.getJobCode() == code)
						this.currentJob = null;
			
				this.decrementNumberAssigned();
		
				return 2;
				
			}else {
				
				boolean check = false;
				
				while(!check){
					check = true;
				
					for(JobInfo info: jobPath){
						if(info.getJobCode() == code){
							jobPath.remove(info);
							check = false;
							break;
						}
					}
				}
						
				if(this.currentJob != null)
					if(this.currentJob.getJobCode() == code)
						this.currentJob = null;
						
				this.decrementNumberAssigned();
			
				return 1;
			}
	
		}else 
			return 0;
		
		
	}
}
