package com.bestroboticsteam.jobs;

import java.awt.Point;
import java.util.LinkedList;

import org.apache.log4j.Logger;

import com.bestroboticsteam.jobs.JobInfo;

public class JobAssignment {

	private final JobSelection selection;
	private Point position;
	
	final Logger logger = Logger.getLogger(JobAssignment.class);

	//jobPath will store a collections of subJobs(resulted from breaking an Order) 
	private LinkedList<JobInfo> jobPath = new LinkedList<JobInfo>();
	private LinkedList<Order> currentOrders = new LinkedList<Order>();

	public JobAssignment(JobSelection selection) {
		this.selection = selection;
		position.x = 0;
		position.y = 0;
	}

	public synchronized JobInfo getNextJob() {

		if (jobPath.isEmpty())
			setInfoJobs();

		return jobPath.pop();
	}

	private void setInfoJobs(){
		//In case there is no subJob in the list, get the next Order and break it
		Order nextOrder = selection.take();
		if(nextOrder == null){
			logger.info("No more jobs!");
		}
		
		currentOrders.add(nextOrder);
		jobPath.addAll(nextOrder.toJobInfos());
		jobPath.add(new JobInfo("DropBox", selection.getDropLocation().getFirst()));
	}

	public LinkedList<Order> getCurrentOrders() {
		return currentOrders;
	}

	public void removeFromCurrentOrder(Order order) {
		currentOrders.remove(order);
	}
	
	public void cancelOrder(Order order){
		while(jobPath.getFirst().getJobCode() == order.getId())
			jobPath.removeFirst();
		currentOrders.remove(order);
	}
	
	
	

	
	private LinkedList<JobInfo> orderPath(LinkedList<JobInfo> path){
		
		LinkedList<JobInfo> aux = path;
		int jobNumb = path.size();
		int[][] itemToItem = new int[jobNumb][jobNumb];
		int[] itemToDrop = new int[jobNumb];
		int[] robotToItem = new int[jobNumb];
		int dist = Integer.MAX_VALUE;
		int index = 0;
		
		
		//Compute all the distances between the items
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
			robotToItem[i] = averageDistance(position, path.get(i).getPosition());
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
						value = d2;
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
				LinkedList<JobInfo> test = ress;
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
		
		return aux;
	}

	private int averageDistance(Point point, Point point2) {
		
		return Math.abs(point.x - point2.x) + Math.abs(point.y - point2.y);
	}


}
