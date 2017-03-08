package com.bestroboticsteam.pathfinding;

import java.awt.Point;

public class AStarNode {

	public Point location;
	public AStarNode parentNode;
	public int fCost;
	public int gCost;
	public int hCost;
	
	public boolean isStart = false;
	
	public AStarNode(boolean start){
		this.isStart = start;
	}

	public AStarNode(Point location, AStarNode parentNode) {
		this.location = location;
		this.parentNode = parentNode;
	}

	public AStarNode(Point location, AStarNode parentNode, int fCost, int gCost, int hCost) {
		this.location = location;
		this.parentNode = parentNode;
		this.fCost = fCost;
		this.gCost = gCost;
		this.hCost = hCost;
	}

}
