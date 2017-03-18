package com.bestroboticsteam.jobs;
import java.awt.Point;
import java.util.LinkedList;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.log4j.Logger;
import com.bestroboticsteam.jobs.JobInfo;

public class JobAssignment extends Thread {

	private final JobSelection selection;
	private Point position = new Point(0, 0);
	private final float MAX_WEIGHT = 50f;
	private float weight = 0f;
	private Order currentOrder = null;
	
	final Logger logger = Logger.getLogger(JobAssignment.class);
	//jobPath will store a collections of subJobs(resulted from breaking an Order) 
	private BlockingQueue<JobInfo> jobPath = new LinkedBlockingQueue<JobInfo>();
	private LinkedList<Order> assignedOrders = new LinkedList<Order>();
	private LinkedList<Order> finishedOrders = new LinkedList<Order>();
	private Thread thread;
	
	public JobAssignment(JobSelection selection) {
		this.selection = selection;
		
		this.thread = new Thread(){
			
			@Override
			public void run(){
				Order nextOrder;
				
				while((nextOrder = selection.take()) != null){
					assignedOrders.add(nextOrder);
					jobPath.addAll(orderPath(nextOrder.toJobInfos()));
				}
			}
		};
		
		thread.start();
	}
	
	public synchronized JobInfo getNextJob() {

		while(true){
			try {
				
				JobInfo job = jobPath.take();
				
				if(currentOrder == null){
					currentOrder = assignedOrders.getFirst();
				}else if (currentOrder.getId() != job.getJobCode()){
					for(Order o: assignedOrders){
						if(job.getJobCode() == currentOrder.getId()){
							finishedOrders.add(currentOrder);
						}
						if(job.getJobCode() == o.getId()){
							currentOrder = o;
						}
					}
				}
				
				
				return job;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	
	public Order viewFinishedOrder(int index){
		return finishedOrders.get(index);
	}

	public LinkedList<Order> getCurrentOrders(){
		LinkedList<Order> currentOrders = new LinkedList<>();
		currentOrders.add(currentOrder);
		return currentOrders;
	}
	
	public synchronized boolean isCurrentJob(int order){
		
		if(currentOrder.getId() == order)
				return true;
		return false;
	}
	
	
	public synchronized void cancelOrder(int order){
		while(jobPath.peek().getJobCode() == order)
			jobPath.remove();
		
		currentOrder = null;
		
	}
	
	private LinkedList<JobInfo> orderPath(LinkedList<JobInfo> path){
		
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
		for(int i = 0; i < ress.size(); i++){
			float value = ress.get(i).getWeight()*ress.get(i).getQuantity();
			if(this.weight + value == this.MAX_WEIGHT){
				
				ress.add(i++, new JobInfo("DropBox", this.getDrop(ress.get(i))));
				this.weight = 0f;
				
			}else if(this.weight + value > this.MAX_WEIGHT){
				
				int quantity = (int)(value/(this.MAX_WEIGHT-weight));
				
				JobInfo info = ress.get(i);
				
				ress.remove(i);
				
				ress.add(i, new JobInfo(info.getItem(), info.getPosition(), quantity, info.getJobCode(), info.getWeight()));
				
				ress.add(++i, new JobInfo("DropBox", this.getDrop(ress.get(i-1))));
				
				ress.add(++i, new JobInfo(info.getItem(), info.getPosition(), info.getQuantity() - quantity, info.getJobCode(), info.getWeight()));
				
				this.weight = 0f;
			
			} else {
				weight += value;
			}
		}
		
		position.setLocation(ress.getLast().getPosition());
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