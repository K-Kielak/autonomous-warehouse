package com.bestroboticsteam.jobs;

import java.awt.Point;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.bestroboticsteam.jobs.JobInfo;
import com.bestroboticsteam.pathfinding.AStar;
import com.bestroboticsteam.robotsmanagement.RobotInfo;

public class JobAssignment extends Thread {
	private final JobSelection selection;
	
	private RobotInfo[] robots;
	private int[] costs;
	private float[] weights;
	private HashMap<String, MyRobotInfo> robotMap = new HashMap<String, MyRobotInfo>(); 
	
	final Logger logger = Logger.getLogger(JobAssignment.class);
	
	private LinkedList<Order> assignedOrders = new LinkedList<Order>();
	private LinkedList<Order> finishedOrders = new LinkedList<Order>();
	private Thread thread;
	
	public JobAssignment(JobSelection selector, RobotInfo[] robots) {
		this.selection = selector;
		this.robots = robots;
		this.costs = new int[robots.length];
		this.weights = new float[robots.length];
		
		for(int i = 0; i < robots.length; i++){
			
			MyRobotInfo info = new MyRobotInfo(robots[i].getMaxCapacity(), 0f, robots[i].getPosition());
			
			robotMap.put(robots[i].getName(), info);
			
			costs[i] = 0;
			
			weights[i] = 0f;
		}
		
		this.thread = new Thread(){
			
			@Override
			public void run(){
				Order nextOrder;
				
				while((nextOrder = selection.take()) != null){
					
					boolean check = false;
					for(MyRobotInfo r: robotMap.values())
						if(r.getNumberAssigned() == 6){
							check = true;
						}
					
					if(!check){
						assign(nextOrder);
					}
				}
			}
		};
		
		thread.start();
	}
	
	private void assign(Order nextOrder) {
		
		LinkedList<JobInfo> finalPath = null;
		int index = 0;
		int finalCost = Integer.MAX_VALUE;
		
		//compare the costs and choose the best robot for the specific path/order
		for(int i = 0; i < robots.length; i++){
			LinkedList<JobInfo> path = orderPath(nextOrder.toJobInfos(), i);
			
			if(costs[i] < finalCost){
				finalPath = path;
				finalCost = costs[i];
				index = i;
			}
		}
		
		assignedOrders.add(nextOrder);
		
		MyRobotInfo robot = robotMap.get(robots[index].getName());
		
		robot.addJobPath(finalPath);
		robot.setCost(costs[index]);
		robot.setWeight(weights[index]);
		robot.incementNumberAssigned();
		robot.setPosition(finalPath.getLast().getPosition());
		
	}
	
	public LinkedList<Order> getAssignedOrders(){
		LinkedList<Order> result = new LinkedList<Order>();
		LinkedList<Order> currentOrder = getCurrentOrders();
		
		for(Order o: assignedOrders)
			if(!currentOrder.contains(o))
				result.add(o);
		
		return result;
	}
	
	public synchronized JobInfo getNextJob(String robotCode) {
		
		MyRobotInfo robot = robotMap.get(robotCode);
		Order currentOrder = robot.getCurrentOrder();
				
		JobInfo job = robot.getNextJob();
		
		if(currentOrder == null){
			for(Order o: assignedOrders){
				if(job.getJobCode() == o.getId())
					robot.setCurrentOrder(o);
			}
		}else if (currentOrder.getId() != job.getJobCode()){
			for(Order o: assignedOrders){
				if(o.getId() == currentOrder.getId()){
					finishedOrders.add(currentOrder);
					assignedOrders.remove(o);
					robot.decrementNumberAssigned();
				}
				if(job.getJobCode() == o.getId()){
					robot.setCurrentOrder(o);
				}
			}
		}
		
		if(robot.getCurrentJob() == null)
			robot.setCurrentJob(job);
		else{
			int cost = robot.getCost() - robot.getCurrentJob().getCost();
			robot.setCurrentJob(job);
			robot.setCost(cost);
		}
			
		return job;
	}
	
	public Order viewFinishedOrder(int index){
		if(index >= finishedOrders.size())
			return null;
		return finishedOrders.get(index);
	}
	
	public LinkedList<Order> getCurrentOrders(){
		LinkedList<Order> currentOrders = new LinkedList<>();
		for(int i = 0; i < robots.length; i ++){
			Order currentOrder = robotMap.get(robots[i].getName()).getCurrentOrder();
			if(currentOrder != null)
				currentOrders.add(currentOrder);
		}
		return currentOrders;
	}
	
	public synchronized boolean isCurrentJob(int order){
		
		for(Order o: getCurrentOrders())
			if(o.getId() == order){
				return true;
			}
		
		return false;
	}
	
	
	public synchronized void cancelOrder(int code){
		//robotMap.get(robots[0].getName()).cancelJob(order);
		//currentOrder = null;
		
		boolean current = false;
		boolean assigned = false;
		
		for(Order o: assignedOrders){
			if(o.getId() == code){
				
				for(int i = 0; i < robots.length; i++){
					robotMap.get(robots[i].getName()).cancelOrder(code);
				}
				assignedOrders.remove(o);
				assigned = false;
				break;
			}
		}
		
		if(!assigned){
			selection.cancelOrder(code);
		}
		
	} 
	
	private LinkedList<JobInfo> orderPath(LinkedList<JobInfo> path, int robotIndex){
		
		MyRobotInfo robot = robotMap.get(robots[robotIndex].getName());
		
		LinkedList<JobInfo> aux = (LinkedList<JobInfo>)path.clone();
		
		int jobNumb = path.size();
		
		int[][] itemToItem = new int[jobNumb][jobNumb];
		int[] itemToDrop = new int[jobNumb];
		int[] robotToItem = new int[jobNumb];
		
		int dist = Integer.MAX_VALUE;
		int index = 0;
		
		
		//Compute all the distances
		for(int i = 0; i < jobNumb; i ++)
			for(int j = i; j < jobNumb; j++){
				if(i == j)
					itemToItem[i][j] = 0;
				else{
					int distance = averageDistance(path.get(i).getPosition(), path.get(j).getPosition());
					itemToItem[i][j] = distance;
					itemToItem[j][i] = distance;
				}
			}
		
		for(int i = 0; i < jobNumb; i++){
			itemToDrop[i] = Integer.MAX_VALUE;
			for(int j = 0; j < selection.getDropLocation().size(); j++)
				itemToDrop[i] = Math.min(itemToDrop[i], averageDistance(path.get(i).getPosition(), selection.getDropLocation().get(j)));
		}
		
		for(int i = 0; i < jobNumb; i++){
			robotToItem[i] = averageDistance(robot.getPosition(), path.get(i).getPosition());
			if(robotToItem[i] < dist){
				dist = robotToItem[i];
				index = i;
			}
		}
		
		
		LinkedList<JobInfo> ress = new LinkedList<JobInfo>();
		ress.add(aux.get(index));
		aux.remove(index);
		
		//order the list
		while(!aux.isEmpty()){
			
			JobInfo info1 = null;
			
			int d1 = Integer.MAX_VALUE;
			
			for(int i = 0; i < aux.size(); i++){
				
				int d2 = Integer.MAX_VALUE;
				JobInfo info2 = null;
				
				for(int j = 0; j < ress.size(); j++){
				
					int value = itemToItem[path.indexOf(aux.get(i))][path.indexOf(ress.get(j))];
					if(d2 > value){
						d2 = value;
						info2 = aux.get(i);
					}
				}
				
				if(d1 > d2){
					d1 = d2;
					info1 = info2;
				}
				
			}
			
			d1 = Integer.MAX_VALUE;
			index = 0;
			
			for(int k = 0; k < ress.size()+1; k++){
				LinkedList<JobInfo> test = (LinkedList<JobInfo>) ress.clone();
				
				test.add(k, info1);
				int d2 = robotToItem[path.indexOf(test.getFirst())];
				d2 += itemToDrop[path.indexOf(test.getLast())];
				
				for(int i = 1; i < test.size(); i++)
					d2 += itemToItem[path.indexOf(test.get(i-1))][path.indexOf(test.get(i))];
				
				if(d1 > d2){
					d1 = d2;
					index = k;
				}
			}
			
			
			
			ress.add(index, info1);
			aux.remove(info1);
			
		}
		
		//add the drop-boxes
		float weight = robot.getWeight();
		float maxWeight = robot.getMaxWeight();
		
		for(int i = 0; i < ress.size(); i++){
			float value = ress.get(i).getWeight()*ress.get(i).getQuantity();
			if(weight + value == maxWeight){
				
				ress.add(i++, new JobInfo("DropBox", this.getDrop(ress.get(i-1))));
				weight = 0f;;
				
			}else if(weight + value > maxWeight){
				
				int quantity = (int)(value / (maxWeight - weight));
				
				JobInfo info = ress.get(i);
				
				ress.remove(i);
				
				ress.add(i, new JobInfo(info.getItem(), info.getPosition(), quantity, info.getJobCode(), info.getWeight()));
				
				ress.add(++i, new JobInfo("DropBox", this.getDrop(ress.get(i-1))));
				
				ress.add(++i, new JobInfo(info.getItem(), info.getPosition(), info.getQuantity() - quantity, info.getJobCode(), info.getWeight()));
				
				weight = info.getWeight()*(info.getQuantity() - quantity);
			
			} else {
				weight = weight + value;
			}
		}
		
		if(!ress.getLast().isDropPoint())
			ress.addLast(new JobInfo("DropBox", this.getDrop(ress.get(ress.size()-1))));
		
		weights[robotIndex] = weight;
		
		//compute final cost for this path + previous costs
		
		costs[robotIndex] = robot.getCost();
		
		costs[robotIndex] += robotToItem[path.indexOf(ress.peek())];
		ress.get(0).setCost(robotToItem[path.indexOf(ress.peek())]);
		
		for(int i = 1; i < ress.size(); i++){
			if( ress.get(i).getItem().equals("DropBox") && i < ress.size()-1 ){
				
				ress.get(i).setCost(itemToDrop[path.indexOf(ress.get(i-1))]);
				
				costs[robotIndex] += itemToDrop[path.indexOf(ress.get(i-1))];
				costs[robotIndex] += itemToDrop[path.indexOf(ress.get(++i))];
				
				ress.get(i).setCost(itemToDrop[path.indexOf(ress.get(i))]);
			
			}else if( ress.get(i).getItem().equals("DropBox") && i == ress.size()-1 ){
				costs[robotIndex] += itemToDrop[path.indexOf(ress.get(i-1))];
				ress.get(i).setCost(itemToDrop[path.indexOf(ress.get(i-1))]);
			}else{
				ress.get(i).setCost(itemToItem[path.indexOf(ress.get(i-1))][path.indexOf(ress.get(i))]);
				costs[robotIndex] += itemToItem[path.indexOf(ress.get(i-1))][path.indexOf(ress.get(i))];
			}
		}
		
		return ress;
	}
	
	private int averageDistance(Point point, Point point2) {
		return AStar.singleGetPath(point, point2).size();
	}
	
	private Point getDrop(JobInfo info){
		
		Point point = new Point(-1,-1);
		int distance = Integer.MAX_VALUE;
		
		for(Point p: selection.getDropLocation()){
			
			int x = averageDistance(p, info.getPosition());
			if( x < distance){
				point = p;
				distance = x;
			}
				
		}
			
		return point;
	}
}