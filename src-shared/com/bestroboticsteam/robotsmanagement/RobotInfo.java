package com.bestroboticsteam.robotsmanagement;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Optional;

import com.bestroboticsteam.jobs.JobInfo;

public class RobotInfo {
	public final String NAME;
	private Point position;
	private Optional<JobInfo> currentJob = Optional.empty();
	private LinkedList<Point> currentPath = new LinkedList<Point>();
	
	public RobotInfo(String name, Point position){
		this.NAME = name;
		this.position = position;
		
	}
	
	//returns true if whole path was finished
	public boolean moved(){ 
		position = currentPath.poll();
		return currentPath.isEmpty();
	}
	
	//returns true if number of clicks was sufficient
	public boolean clicked(){ 
		//TODO decrease quantity
		return true;
	}
	
	public boolean finished(){
		return !currentJob.isPresent();
	}
	
	public Point getPosition(){
		return position;
	}
	
	public void setCurrentJob(JobInfo job, LinkedList<Point> path){
		currentJob = Optional.of(job);
		currentPath = path;
	}
	
	public JobInfo getCurrentJob(){
		return currentJob.get();
	}
	
	public LinkedList<Point> getCurrentPath(){
		return currentPath;
	}
}
