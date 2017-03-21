package com.bestroboticsteam.jobs;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentMap;

import com.bestroboticsteam.jobs.JobInfo;

public class Order implements Comparable<Order> {

	// do you prefer another type of map?
	private ConcurrentMap<Item, Integer> orderTable;
	private int id;
	private boolean prediction;

	public Order(int _id, ConcurrentMap<Item, Integer> ot) {
		orderTable = ot;
		id = _id;
	}

	public void setPrediction(boolean x){
		prediction = x;
	}
	
	public boolean getPrediction(){
		return prediction;
	}

	public int getId() {
		return id;
	}

	public int getQuantity(Item j) {
		return orderTable.get(j);
	}

	public LinkedList<JobInfo> toJobInfos() { 
		
		//break the order into different JobInfos
		
		LinkedList<JobInfo> list = new LinkedList<JobInfo>();

		for (Item i : orderTable.keySet()) {
			JobInfo info = new JobInfo(i.getCode(), i.getPosition(), orderTable.get(i), id, i.getWeight());
			list.add(info);
		}
		
		return list;
	}

	public ConcurrentMap<Item, Integer> getOrderTable() {
		return orderTable;
	}

	public float getTotalReward() {
		float reward = 0f;
		
		for (Item e : orderTable.keySet()) {
			reward += e.getReward();
		}
		
		return reward;
	}
	
	private float getOverallReward() {
		float reward = 0f;
		
		for (Item e : orderTable.keySet()) {
			reward += (e.getReward() * orderTable.get(e))/(e.getWeight()*orderTable.get(e));
		}
		
		return reward;
	}
	
	@Override
	public boolean equals(Object o){
		return (this.id == ((Order) o).getId());
	}

	@Override
	public int compareTo(Order compareOrder) {
		//used to sort the LinkedList by reward
		float compareReward = compareOrder.getOverallReward();

		if(this.prediction == true){
			if (compareOrder.getPrediction() == true)
				if (this.getOverallReward() - compareReward < 0)
					return 1;
				else if (this.getOverallReward() - compareReward == 0)
					return 0;
				else
					return -1;
			else return 1;
		} else{
			if (compareOrder.getPrediction() == false)
				if (this.getOverallReward() - compareReward < 0)
					return 1;
				else if (this.getOverallReward() - compareReward == 0)
					return 0;
				else
					return -1;
			else return -1;
		}
	}
	
	@Override
	public String toString(){
		//toString method -> used in InterfaceController
		return "Job ID: " + getId() + " " + "Job Reward: " + getTotalReward();
		
	}
}
