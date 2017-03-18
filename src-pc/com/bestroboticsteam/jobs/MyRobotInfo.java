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
	
	public MyRobotInfo(float maxWeight, float weight, Point position){
		this.MAX_WEIGHT = maxWeight;
		this.position = position;
		this.weight = weight;
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
}
