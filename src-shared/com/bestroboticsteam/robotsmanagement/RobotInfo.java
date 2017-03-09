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
	public String name;
	private Point position;
	private Direction direction;
	private JobInfo currentJob = null;
	private LinkedList<Point> currentPath = new LinkedList<Point>();

	public RobotInfo(String name, Point position, Direction direction) {
		this.name = name;
		this.position = position;
		this.direction = direction;
	}
	
	public RobotInfo() {}

	// returns null whole path was finished
	public Direction move() {
		if (currentPath.isEmpty()) {
			return null;
		}
		Point newPos = currentPath.get(0);
		currentPath.remove(0);
		Direction newDir;
		if(position.distance(newPos) != 1)
			throw new IllegalArgumentException("wrong path");
		
		if(position.x+1 == newPos.x)
			newDir = Direction.LEFT; //turn west
		else if(position.x-1 == newPos.x)
			newDir = Direction.RIGHT; //turn east
		else if(position.y+1 == newPos.y)
			newDir = Direction.FORWARD; //turn north
		else //if(position.y-1 == newPos.y)
			newDir = Direction.BACKWARD; //turn south
		
		position = newPos;
		return turn(newDir);
	}

	// returns true if number of clicks was sufficient
	public boolean clicked() {
		// TODO decrease quantity
		return true;
	}

	public boolean finished() {
		return currentJob == null;
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
		direction = goal;

		if (direction == goal)
			return Direction.FORWARD;

		if (direction.ordinal() == (goal.ordinal() + 1) % 4) {
			return Direction.RIGHT;
		}

		if (direction.ordinal() == (goal.ordinal() + 2) % 4)
			return Direction.BACKWARD;

		// if(direction.ordinal() == (goal.ordinal()+3)%4)
		return Direction.LEFT;
	}

	@Override
	public void sendObject(MyDataOutputStream o) throws IOException {
		// this.name
		o.writeString(this.name);
		// this.position
		o.writePoint(this.position);
		// this.direction
		o.writeInt(this.direction.ordinal());
		// this.currentJob
		this.currentJob.sendObject(o);
		// this.currentPath
		o.writeInt(this.currentPath.size());
		for (Iterator<Point> iterator = currentPath.iterator(); iterator.hasNext();) {
			Point point = (Point) iterator.next();
			o.writePoint(point);
		}
	}

	@Override
	public RobotInfo receiveObject(MyDataInputStream i) throws IOException {
		this.name = i.readString();
		this.position = i.readPoint();
		this.direction = Direction.values()[i.readInt()];
		this.currentJob.receiveObject(i);
		// currentPath
		int pathSize = i.readInt();
		this.currentPath.clear();
		for (int j = 0; j < pathSize; j++) {
			this.currentPath.add(j, i.readPoint());
		}
		return this;
	}
}
