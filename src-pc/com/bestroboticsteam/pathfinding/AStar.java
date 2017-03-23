package com.bestroboticsteam.pathfinding;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.util.Pair;

import org.apache.log4j.Logger;

import com.bestroboticsteam.robotsmanagement.RobotInfo;

public class AStar {
	
	final static Logger logger = Logger.getLogger(AStar.class);
	
	// multi robot AStar
	public synchronized static LinkedList<Point> multiGetPath(Pair<Point, Point> locationDestinationPair, RobotInfo[] otherRobots) {
		HashMap<TimePoint, Boolean> timedReservationTable = new HashMap<>(100);
		//Set up the timesReservationTable to avoid collisions with other robots
		for(RobotInfo robot : otherRobots){
			if(robot.getCurrentPath() != null && !robot.getCurrentPath().isEmpty()){
				Point[] path = robot.getCurrentPath().toArray(new Point[robot.getCurrentPath().size()]);
				for(int i = 0; i<path.length; i++){
					timedReservationTable.put(new TimePoint(path[i], i+1), true);
					//This is needed because the other robots may have already started to move to the next position
					timedReservationTable.put(new TimePoint(path[i], i), true);
				}
				for(int i = path.length; i<100; i++){
					//This is needed because the other robots may stop at the end of their path to pick up
					timedReservationTable.put(new TimePoint(path[path.length-1], i), true);
				}
			}else{
				//If the robot has finished its current job then it will be free to move
				if(robot.getCurrentJob().getPosition().equals(robot.getPosition())){
					for(int i = 0; i<100; i++){
						//This is needed because the other robots may be stopped to pick up or drop off
						timedReservationTable.put(new TimePoint(robot.getPosition(), i), true);
					}
				}
				//Even if the robot can move, it cannot instantly teleport out of the way
				timedReservationTable.put(new TimePoint(robot.getPosition(), 0), true);
				
			}
		}
		List<Point> path = AStarPath(locationDestinationPair, timedReservationTable);
		
		return (LinkedList<Point>) path;
	}
	
	public static LinkedList<Point> singleGetPath(Point location, Point destination){
		return AStarPath(Pair.makePair(location, destination), new HashMap<TimePoint, Boolean>());
	}
	
	//Single robot AStar
	public static LinkedList<Point> AStarPath(Pair<Point, Point> locationDestinationPair, HashMap<TimePoint, Boolean> timedReservationTable){
		GridMap map = MapUtils.createRealWarehouse();
		
		PriorityQueue<AStarNode> openList  = new PriorityQueue<AStarNode>(10, new Comparator<AStarNode>() {
			@Override
			public int compare(AStarNode o1, AStarNode o2) {
				return o1.fCost-o2.fCost;
			};
		});
		ArrayList<AStarNode> closedList = new ArrayList<AStarNode>();
		//Lists to show if a certain cell is on either of the lists to make looking them up easier.
		boolean[][] openListLocations = new boolean[map.getXSize()][map.getYSize()];
		boolean[][] closedListLocations = new boolean[map.getXSize()][map.getYSize()];
		
		Point botPosition = locationDestinationPair.getItem1();
		Point doorPosition = locationDestinationPair.getItem2();
		
		//Tests to make sure start and destination are within the map boundaries.
		Rectangle mapBounds = new Rectangle(0, 0, map.getXSize(), map.getYSize());
		if(!mapBounds.contains(botPosition) || !mapBounds.contains(doorPosition)){
			logger.warn("Invalid path from (" + (int)botPosition.getX() + ", " + (int)botPosition.getY() + ") to (" + (int)doorPosition.getX() + ", " + (int)doorPosition.getY() + "), one of the coordinates is out of bounds.");
			return null;
		}
		
		LinkedList<Point> path = new LinkedList<Point>();
		
		//Adds the robot's current location to the open list.
		openList.add(new AStarNode(botPosition, true, botPosition.x+botPosition.y-doorPosition.x-doorPosition.y, 0, botPosition.x+botPosition.y-doorPosition.x-doorPosition.y));
		closedList.add(new AStarNode(botPosition, true));
		while(true){
			if(openList.size()==0){
				logger.warn("No paths found from (" + (int)botPosition.getX() + ", " + (int)botPosition.getY() + ") to (" + (int)doorPosition.getX() + ", " + (int)doorPosition.getY() + ")");
				return null;
				}//Stops pathfinding when all possible paths have been examined and no path is possible.
			AStarNode currentNode = null;
			/*for(AStarNode n : openList){
				if(n.fCost<=lowestFCost){ // Makes the node with the lowest fCost on the openList the current node.
					lowestFCost=n.fCost;
					currentNode=n;
				}
			}*/
			currentNode = openList.poll();
			/*if(!adjacentRobot(currentNode, timedReservationTable)){
				System.out.println(currentNode.location + "Test locationN :" + doorPosition);
			}else{
				System.out.println(currentNode.location + "Test location :" + doorPosition);
			}*/
			if(currentNode.location.x==doorPosition.x && currentNode.location.y==doorPosition.y){ //Stops pathfinding when a path has been found.
				closedList.add(currentNode);
				break;
			}
			int nodeX = currentNode.location.x;
			int nodeY = currentNode.location.y;
			if(!adjacentRobot(currentNode, timedReservationTable)){
				closedListLocations[nodeX][nodeY] = true;
			}else{
				openList = addToOpenList(currentNode.location, currentNode, openListLocations, openList, closedListLocations, doorPosition, timedReservationTable);
			}
			closedList.add(currentNode);
			openListLocations[nodeX][nodeY] = false;
			
			//Add adjacent nodes to the open list
			openList = addToOpenList(new Point(nodeX, nodeY+1), currentNode, openListLocations, openList, closedListLocations, doorPosition, timedReservationTable);
			openList = addToOpenList(new Point(nodeX, nodeY-1), currentNode, openListLocations, openList, closedListLocations, doorPosition, timedReservationTable);
			openList = addToOpenList(new Point(nodeX+1, nodeY), currentNode, openListLocations, openList, closedListLocations, doorPosition, timedReservationTable);
			openList = addToOpenList(new Point(nodeX-1, nodeY), currentNode, openListLocations, openList, closedListLocations, doorPosition, timedReservationTable);
			openList = addToOpenList(new Point(nodeX, nodeY), currentNode, openListLocations, openList, closedListLocations, doorPosition, timedReservationTable);
			
		}
		
		path = new LinkedList<Point>();
		AStarNode currentNode = closedList.get(closedList.size()-1);
		ArrayList<AStarNode> fullList = (ArrayList<AStarNode>) closedList.clone();
		Collections.addAll(fullList, openList.toArray(new AStarNode[openList.size()]));
		while(!(currentNode.isStart)){
			path.add(currentNode.location);
			//System.out.println(doorPosition.toString() + " : " + path);
			/*for(AStarNode n : fullList){
				//System.out.println(doorPosition + "Testing" + currentNode);
				if((currentNode.parentNode != null) && n.location.x==currentNode.parentNode.location.x && n.location.y==currentNode.parentNode.location.y && n.gCost == currentNode.parentNode.gCost){
					//System.out.println(doorPosition + "Testing" + currentNode);
					currentNode=n;
					fullList.remove(currentNode);
					break;
				}
			}*/
			currentNode = currentNode.parentNode;
		}
		openList.clear();
		closedList.clear();
		fullList.clear();
		openListLocations = new boolean[map.getXSize()][map.getYSize()];
		closedListLocations = new boolean[map.getXSize()][map.getYSize()];
		Collections.reverse(path);
		return path;
		
	}
	
	private static boolean adjacentRobot(AStarNode currentNode, HashMap<TimePoint, Boolean> timedReservationTable) {
		boolean adjacentRobot = false;
		adjacentRobot |= timedReservationTable.containsKey(new TimePoint(new Point(currentNode.location.x+1, currentNode.location.y), currentNode.gCost));
		adjacentRobot |= timedReservationTable.containsKey(new TimePoint(new Point(currentNode.location.x-1, currentNode.location.y), currentNode.gCost));
		adjacentRobot |= timedReservationTable.containsKey(new TimePoint(new Point(currentNode.location.x, currentNode.location.y+1), currentNode.gCost));
		adjacentRobot |= timedReservationTable.containsKey(new TimePoint(new Point(currentNode.location.x, currentNode.location.y-1), currentNode.gCost));
		
		return adjacentRobot;
	}

	//Adds a point to the openList if it is not blocked
	private static PriorityQueue<AStarNode> addToOpenList(Point location, AStarNode currentNode, boolean[][] openListLocations, PriorityQueue<AStarNode> openList, boolean[][] closedListLocations, Point doorPosition, HashMap<TimePoint, Boolean> timedReservationTable){
		GridMap map = MapUtils.createRealWarehouse();
		//Calculates the TimePoint for the current node based on its gCost
		TimePoint nodeTimePoint = new TimePoint(location, currentNode.gCost);
		if(location.x>=0 && location.y>=0 && location.x<map.getXSize() && location.y <map.getYSize()){
			if(!map.isObstructed(location.x, location.y) && !closedListLocations[location.x][location.y] && !timedReservationTable.containsKey(nodeTimePoint)){
				if(!openListLocations[location.x][location.y]){
					openList.add(new AStarNode(new Point(location.x,location.y), currentNode, currentNode.gCost+1+Math.abs(location.x-doorPosition.x)+Math.abs(location.y-doorPosition.y), currentNode.gCost+1, Math.abs(location.x-doorPosition.x)+Math.abs(location.y-doorPosition.y)));
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
