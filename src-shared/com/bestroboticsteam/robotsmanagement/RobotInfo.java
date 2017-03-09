package com.bestroboticsteam.robotsmanagement;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Optional;

import com.bestroboticsteam.jobs.JobInfo;

public class RobotInfo {
	public final String NAME;
	private Point position;
	private Direction direction;
	private Optional<JobInfo> currentJob = Optional.empty();
	private LinkedList<Point> currentPath = new LinkedList<Point>();
	
	public RobotInfo(String name, Point position, Direction direction){
		this.NAME = name;
		this.position = position;
		this.direction = direction;	
	}
	
	//returns null whole path was finished
	public Direction move(){ 
		Point newPos = currentPath.poll();
		if(position.distance(newPos) != 1)
			throw new IllegalArgumentException("wrong path");
		
		if(position.x+1 == newPos.x)
			return turn(Direction.LEFT); //turn west
		
		if(position.x-1 == newPos.x)
			return turn(Direction.RIGHT); //turn east
		
		if(position.y+1 == newPos.y)
			return turn(Direction.FORWARD); //turn north
		
		//if(position.y-1 == newPos.y)
		return turn(Direction.BACKWARD); //turn south
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
	
	private Direction turn(Direction goal){
		direction = goal;
		
		if(direction == goal)
			return Direction.FORWARD;
		
		if(direction.ordinal() == (goal.ordinal()+1)%4){
			return Direction.RIGHT;	
		}
		
		if(direction.ordinal() == (goal.ordinal()+2)%4)
			return Direction.BACKWARD;
		
		//if(direction.ordinal() == (goal.ordinal()+3)%4)
		return Direction.LEFT;
	}
}
