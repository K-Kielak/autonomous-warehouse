package com.bestroboticsteam.robotsmanagement;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import com.bestroboticsteam.communication.Communicatable;
import com.bestroboticsteam.jobs.JobInfo;

public class RobotInfo implements Communicatable {
	private String name;
	private Point position;
	private Direction direction;
	private JobInfo currentJob = null;
	private LinkedList<Point> currentPath = new LinkedList<Point>();

	public RobotInfo(String name, Point position, Direction direction) {
		this.name = name;
		this.position = position;
		this.direction = direction;
	}

	// returns null whole path was finished
	public Direction move() {
		if(currentPath.isEmpty())
			return null;
			
		Point newPos = currentPath.get(0);
		currentPath.remove(0);
		Direction newDir;
		if(position.distance(newPos) != 1)
			throw new IllegalArgumentException("wrong path");
		
		if(position.x-1 == newPos.x)
			newDir = Direction.LEFT; //turn west
		else if(position.x+1 == newPos.x)
			newDir = Direction.RIGHT; //turn east
		else if(position.y+1 == newPos.y)
			newDir = Direction.FORWARD; //turn north
		else //if(position.y-1 == newPos.y)
			newDir = Direction.BACKWARD; //turn south
		
		position = newPos;
		return turn(newDir);
	}

	public void click() {
		currentJob.decreaseQuantity();
		if(currentJob.getQuantity() <= 0)
			currentJob = null;
	}

	public boolean finished() {
		return currentJob == null;
	}
	
	public String getName(){
		return name;
	}

	public Point getPosition() {
		return position;
	}

	public void setCurrentJob(JobInfo job, LinkedList<Point> path) {
		currentJob = job;
		currentPath = path;
	}

	public JobInfo getCurrentJob() {
		return currentJob;
	}

	public LinkedList<Point> getCurrentPath() {
		return currentPath;
	}

	private Direction turn(Direction goal) {
		Direction turnSide;
		
		if (direction == goal)
			turnSide = Direction.FORWARD;
		else if ((direction.ordinal()  + 1) % 4 == goal.ordinal()) 
			turnSide = Direction.RIGHT;
		else if ((direction.ordinal()  + 2) % 4 == goal.ordinal())
			turnSide = Direction.BACKWARD;
		else// if(direction.ordinal() == (goal.ordinal()+3)%4)
			turnSide =  Direction.LEFT;
		
		direction = goal;
		return turnSide;
	}

	@Override
	public void sendObject(DataOutputStream o) throws IOException {
		// this.name
		this.writeString(o, this.name);
		// this.position
		this.writePoint(o, this.position);
		// this.direction
		o.writeInt(this.direction.ordinal());
		// this.currentJob
		this.currentJob.sendObject(o);
		// this.currentPath
		o.writeInt(this.currentPath.size());
		for (Iterator<Point> iterator = currentPath.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			this.writePoint(o, point);
		}
	}

	@Override
	public RobotInfo receiveObject(DataInputStream i) throws IOException {
		this.name = this.readString(i);
		this.position = this.readPoint(i);
		this.direction = Direction.values()[i.readInt()];
		this.currentJob.receiveObject(i);
		// currentPath
		int pathSize = i.readInt();
		this.currentPath.clear();
		for (int j = 0; j < pathSize; j++) {
			this.currentPath.add(j, this.readPoint(i));
		}
		return this;
	}
}
