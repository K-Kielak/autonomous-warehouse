package com.bestroboticsteam.jobs;
import java.awt.Point;
import java.util.HashMap;
import java.util.LinkedList;
import org.apache.log4j.Logger;
import com.bestroboticsteam.jobs.JobInfo;
public class JobAssignment {
	private final JobSelection selection;
	private Point position = new Point(0, 0);
	private final float MAX_WEIGHT = 50f;
	private float weight = 0f;
	
	final Logger logger = Logger.getLogger(JobAssignment.class);
	//jobPath will store a collections of subJobs(resulted from breaking an Order) 
	private LinkedList<JobInfo> jobPath = new LinkedList<JobInfo>();
	private LinkedList<Order> currentOrders = new LinkedList<Order>();
	private LinkedList<Order> finishedOrders = new LinkedList<Order>();
	
	public JobAssignment(JobSelection selection) {
		this.selection = selection;
	}
	public synchronized JobInfo getNextJob() {
		if (jobPath.isEmpty()){
			finishedOrders.addFirst((currentOrders.pollFirst()));
			setInfoJobs();
		}
		return jobPath.pop();
	}
	
	public Order viewFinishedOrder(int index){
		if(index > finishedOrders.size())
			return null;
		return finishedOrders.get(index);
	}
	private void setInfoJobs(){
		//In case there is no subJob in the list, get the next Order and break it
		Order nextOrder = selection.take();
		if(nextOrder == null){
			logger.info("No more jobs!");
		}else{
			currentOrders.add(nextOrder);
			jobPath.addAll(this.orderPath(nextOrder.toJobInfos()));
		}
	}
	public LinkedList<Order> getCurrentOrders(){
		return currentOrders;
	}
	public void removeFromCurrentOrder(Order order) {
		currentOrders.remove(order);
	}
	
	
	
	public boolean isCurrentJob(int order){
		
		for(Order o: currentOrders)
			if(o.getId() == order)
				return true;
			}
		
		return false;
	}
	
	public void cancelOrder(int order){
		while(jobPath.getFirst().getJobCode() == order)
			jobPath.removeFirst();
		for(Order o: currentOrders){
			if(o.getId() == order){
				currentOrders.remove(o);
				break;
			}
		}
		
	}
	
	private LinkedList<JobInfo> orderPath(LinkedList<JobInfo> path, int robotIndex){
		
		MyRobotInfo robot = robotMap.get(robots[robotIndex].getName());
		
		LinkedList<JobInfo> aux = (LinkedList<JobInfo>) path.clone();
		
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
				ress.get(i).setCost(itemToDrop[path.indexOf(ress.get(i))]);
			}else{
				ress.get(i).setCost(itemToItem[path.indexOf(ress.get(i-1))][path.indexOf(ress.get(i))]);
				costs[robotIndex] += itemToItem[path.indexOf(ress.get(i-1))][path.indexOf(ress.get(i))];
			}
		}
		
		return ress;
	}
	
	private int averageDistance(Point point, Point point2) {
		
		return Math.abs(point.x - point2.x) + Math.abs(point.y - point2.y);
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