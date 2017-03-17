package com.bestroboticsteam.robotsmanagement;

import java.awt.Point;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import com.bestroboticsteam.communication.Communicatable;
import com.bestroboticsteam.communication.MyDataInputStream;
import com.bestroboticsteam.communication.MyDataOutputStream;
import com.bestroboticsteam.jobs.JobInfo;

public class RobotInfo implements Communicatable {
	private String name;
	private Point position;
	private Direction direction;
	private float maxCapacity;
	private JobInfo currentJob = new JobInfo();
	private LinkedList<Point> currentPath = new LinkedList<Point>();
	

	public RobotInfo(String name, Point position, Direction direction, float maxCapacity) {
		super();
		this.name = name;
		this.position = position;
		this.direction = direction;
		this.maxCapacity = maxCapacity;
	}
	
	public RobotInfo() {}

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
	
	public void cancelJob(){
		currentJob = null;
	}

	public void click() {
		currentJob.decreaseQuantity();
	}

	public boolean finished() {
		return currentJob == null || currentJob.getQuantity() <= 0;
	}
	
	public String getName(){
		return name;
	}

	public Point getPosition() {
		return position;
	}
	
	public float getMaxCapacity(){
		return maxCapacity;
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
	public synchronized void sendObject(MyDataOutputStream o) throws IOException {
		// this.name
		o.writeString(this.name);
		// this.position
		o.writePoint(this.position);
		// this.direction
		o.writeInt(this.direction.ordinal());
		// this.currentJob
		if (this.currentJob == null) {
			// We tell other side that this is null
			o.writeInt(0);
		}
		else {
			// We tell other side that this is not null
			o.writeInt(1);
			this.currentJob.sendObject(o);
		}

		// this.currentPath
		o.writeInt(this.currentPath.size());
		for (Iterator<Point> iterator = currentPath.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			//System.out.println(point);
			//Button.waitForAnyPress();
			o.writePoint(point);
		}
	}

	@Override
	public synchronized RobotInfo receiveObject(MyDataInputStream i) throws IOException {
		this.name = i.readString();
		this.position = i.readPoint();
		this.direction = Direction.values()[i.readInt()];
		int currentJobIsNotNull = i.readInt();
		if (currentJobIsNotNull == 1) {
			// currentJob received is not null
			this.currentJob.receiveObject(i);
		}
		else {
			// currentJob received is null
			System.out.println("Setting currentJob to null");
			this.currentJob = null;
		}
		// currentPath
		int pathSize = i.readInt();
		this.currentPath.clear();
		for (int j = 0; j < pathSize; j++) {
			this.currentPath.add(j, i.readPoint());
		}
		return this;
	}
}