package com.bestroboticsteam.pathfinding;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.util.Pair;

import org.apache.log4j.Logger;

public class AStar {
	
	final static Logger logger = Logger.getLogger(AStar.class);
	
	// multi robot AStar
	public static Point[] multiGetPath(Pair<Point, Point> locationDestinationPair, Point[][] otherRobotPaths) {
		HashMap<TimePoint, Boolean> timedReservationTable = new HashMap<>(100);
		//Set up the timesReservationTable to avoid collisions with other robots
		for(Point[] path : otherRobotPaths){
			if(path != null){
				for(int i = 0; i<path.length; i++){
					timedReservationTable.put(new TimePoint(path[i], i+1), true);
					//This is needed because the other robots may have already started to move to the next position
					timedReservationTable.put(new TimePoint(path[i], i), true);
				}
			}
		}
		List<Point> path = AStarPath(locationDestinationPair, timedReservationTable);
		
		return path==null ? null : path.toArray(new Point[path.size()]);
	}
	
	public static LinkedList<Point> singleGetPath(Pair<Point, Point> locationDestinationPair){
		return AStarPath(locationDestinationPair, new HashMap<TimePoint, Boolean>());
	}
	
	//Single robot AStar
	public static LinkedList<Point> AStarPath(Pair<Point, Point> locationDestinationPair, HashMap<TimePoint, Boolean> timedReservationTable){ //Follows a basic implementation of the A* pathfinding algorithm.
		GridMap map = MapUtils.createRealWarehouse();
		
		ArrayList<AStarNode> openList  = new ArrayList<AStarNode>();
		ArrayList<AStarNode> closedList = new ArrayList<AStarNode>();
		boolean[][] openListLocations = new boolean[map.getXSize()][map.getYSize()];
		boolean[][] closedListLocations = new boolean[map.getXSize()][map.getYSize()]; //Lists to show if a certain cell is on either of the lists to make looking them up easier.
		
		Point botPosition = locationDestinationPair.getItem1();
		Point doorPosition = locationDestinationPair.getItem2();
		
		//Tests to make sure start and destination are within the map boundaries.
		Rectangle mapBounds = new Rectangle(0, 0, map.getXSize(), map.getYSize());
		if(!mapBounds.contains(botPosition) || !mapBounds.contains(doorPosition)){
			logger.warn("Invalid path from (" + (int)botPosition.getX() + ", " + (int)botPosition.getY() + ") to (" + (int)doorPosition.getX() + ", " + (int)doorPosition.getY() + "), one of the coordinates is out of bounds.");
			return null;
		}
		
		LinkedList<Point> path = new LinkedList<Point>();
		
		openList.add(new AStarNode(botPosition, new AStarNode(true), botPosition.x+botPosition.y-doorPosition.x-doorPosition.y, 0, botPosition.x+botPosition.y-doorPosition.x-doorPosition.y)); //Adds the robots square to the open list.
		while(true){
			if(openList.size()==0){
				logger.warn("No paths found from (" + (int)botPosition.getX() + ", " + (int)botPosition.getY() + ") to (" + (int)doorPosition.getX() + ", " + (int)doorPosition.getY() + ")");
				return null;
				}//Stops pathfinding when all possible paths have been examined and no path is possible.
			int lowestFCost=Integer.MAX_VALUE;
			AStarNode currentNode = null;
			for(AStarNode n : openList){
				if(n.fCost<=lowestFCost){ // Makes the node with the lowest fCost on the openList the current node.
					lowestFCost=n.fCost;
					currentNode=n;
				}
			}
			if(currentNode.location.x==doorPosition.x && currentNode.location.y==doorPosition.y){ //Stops pathfinding when a path has been found.
				closedList.add(currentNode);
				break;
			}
			int nodeX = currentNode.location.x;
			int nodeY = currentNode.location.y;
			closedList.add(currentNode);
			openList.remove(currentNode);
			openListLocations[nodeX][nodeY] = false;
			closedListLocations[nodeX][nodeY] = true;
			
			//Add adjacent nodes to the open list
			openList = addToOpenList(new Point(nodeX, nodeY+1), currentNode, openListLocations, openList, closedListLocations, doorPosition, timedReservationTable);
			openList = addToOpenList(new Point(nodeX, nodeY-1), currentNode, openListLocations, openList, closedListLocations, doorPosition, timedReservationTable);
			openList = addToOpenList(new Point(nodeX+1, nodeY), currentNode, openListLocations, openList, closedListLocations, doorPosition, timedReservationTable);
			openList = addToOpenList(new Point(nodeX-1, nodeY), currentNode, openListLocations, openList, closedListLocations, doorPosition, timedReservationTable);
			
		}
		
		path = new LinkedList<Point>();
		AStarNode currentNode = closedList.get(closedList.size()-1);
		while(!(currentNode.location.x==botPosition.x && currentNode.location.y==botPosition.y)){
			path.add(currentNode.location);
			for(AStarNode n : closedList){
				if(n.location.x==currentNode.parentNode.location.x && n.location.y==currentNode.parentNode.location.y){
					currentNode=n;
					break;
				}
			}
		}
		openList.clear();
		closedList.clear();
		openListLocations = new boolean[map.getXSize()][map.getYSize()];
		closedListLocations = new boolean[map.getXSize()][map.getYSize()];
		Collections.reverse(path);
		return path;
		
	}
	
	//Adds a point to the openList if it is not blocked
	private static ArrayList<AStarNode> addToOpenList(Point location, AStarNode currentNode, boolean[][] openListLocations, ArrayList<AStarNode> openList, boolean[][] closedListLocations, Point doorPosition, HashMap<TimePoint, Boolean> timedReservationTable){
		GridMap map = MapUtils.createRealWarehouse();
		//Calculates the TimePoint for the current node based on its gCost
		TimePoint nodeTimePoint = new TimePoint(new Point(currentNode.location.x, currentNode.location.y), currentNode.gCost);
		if(location.x>=0 && location.y>=0 && location.x<map.getXSize() && location.y <map.getYSize()){
			if(!map.isObstructed(location.x, location.y) && !closedListLocations[location.x][location.y] && !timedReservationTable.containsKey(nodeTimePoint)){
				if(!openListLocations[location.x][location.y]){
					openList.add(new AStarNode(new Point(location.x,location.y), currentNode, currentNode.gCost+1+Math.abs(location.x-doorPosition.x)+Math.abs(location.y-doorPosition.y), currentNode.gCost+1, location.x+location.y-doorPosition.x-doorPosition.y));
					openListLocations[location.x][location.y]=true;
				}else{
					for(AStarNode n : openList){
						// Calculates the information needed by the new node
						if(n.location.x==location.x && n.location.y==location.y){
							if(n.gCost>currentNode.gCost){
								n.gCost=currentNode.gCost+1;
								n.parentNode=currentNode;
								n.fCost=n.gCost+n.hCost;
							}
							break;
						}
					}
				}
			}
		}
		return openList;
	}
	
	

}
