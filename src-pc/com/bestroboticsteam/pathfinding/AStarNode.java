package com.bestroboticsteam.pathfinding;

import java.awt.Point;

import javax.xml.stream.events.StartDocument;

public class AStarNode {

	public Point location;
	public AStarNode parentNode;
	public int fCost;
	public int gCost;
	public int hCost;
	
	public boolean isStart = false;
	
	public AStarNode(Point location, boolean start){
		this.location = location;
		this.isStart = start;
	}

	public AStarNode(Point location, AStarNode parentNode) {
		this(location, false);
		this.location = location;
		this.parentNode = parentNode;
	}

	public AStarNode(Point location, AStarNode parentNode, int fCost, int gCost, int hCost) {
		this(location, parentNode);
		this.fCost = fCost;
		this.gCost = gCost;
		this.hCost = hCost;
	}

	public AStarNode(Point location, boolean start, int fCost, int gCost, int hCost) {
		this(location, start);
		this.fCost = fCost;
		this.gCost = gCost;
		this.hCost = hCost;
	}
	
	@Override
	public String toString() {
		return "AStarNode:" + location + (isStart ? "start" : "Parent:" + parentNode.location) + "g:" + gCost + "f:" + fCost;
	}

}
