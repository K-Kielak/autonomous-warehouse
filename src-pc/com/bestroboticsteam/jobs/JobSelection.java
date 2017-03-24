package com.bestroboticsteam.jobs;

import java.awt.Point;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedList;

import org.apache.log4j.Logger;

public class JobSelection {

	private LinkedList<Order> list;
	private Collection<Item> itemList;
	private LinkedList<Point> dropLocation;
	private ReadData reader;
	
	final Logger logger = Logger.getLogger(JobSelection.class);

	public JobSelection(String path) {

		// Read all the data
		
		this.reader = new ReadData();
		itemList = reader.readItemData(path);
		dropLocation = reader.readDropData(path);

		//Create a LinkedList of Orders and sort it
		list = reader.readOrderData(path);
		setPrediction();
		setList();
	}

	public synchronized LinkedList<Point> getDropLocation() {
		return dropLocation;
	}

	public synchronized Collection<Item> getItemList() {
		return itemList;
	}

	public synchronized Order take() {
		if(list.isEmpty())
			return null;
		return list.pop();
	}
	
	public void cancelOrder(int order){
		for(Order element: list){
			if(element.getId() == order){
				list.remove(element);
				break;
			}
		}
	}
	
	public synchronized Order viewOrder(int i) {
		if(i >= list.size()){
			return null;
		}
		
		return list.get(i);

	}

	private synchronized void setList() {

		Comparator<Order> comparator = new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				return o1.compareTo(o2);
			}
		};
		
		logger.info("Sorting list of orders.");
		list.sort(comparator);
	}
	
	private void setPrediction() {
		
		logger.info("Computing the predictions.");
		
		for(Order o: list){
			float yes = 0.5f;
			float no = 0.5f;
			
			for(Item i: o.getOrderTable().keySet()){
				
				if(i.getYesProbability(o.getQuantity(i)) != 0)
					yes = yes * i.getYesProbability(o.getQuantity(i));
				else
					yes = yes * 0.001f;
				
				if(i.getNoProbability(o.getQuantity(i)) != 0)
					no = no * i.getNoProbability(o.getQuantity(i));
				else
					no = no * 0.001f;
				
				if(i.getNameNoProb() != 0)
					no = no * i.getNameNoProb();
				else
					no = no * 0.001f;
				
				if(i.getNameYesProb() != 0)
					yes = yes * i.getNameYesProb();
				else
					yes = yes * 0.001f;
			}
			
			if(reader.getNoRewardProb((int) (o.getTotalReward()/5)) != 0)
				no = no * reader.getNoRewardProb((int) (o.getTotalReward()/10));
			else
				no = no * 0.001f;
			
			if(reader.getYesRewardProb((int) (o.getTotalReward()/5)) != 0)
				yes = yes * reader.getYesRewardProb((int) (o.getTotalReward()/10));
			else
				yes = yes * 0.001f;
			
			if(reader.getYesWeightProb((int) (o.getTotalWeight()/3)) != 0)
				yes = yes * reader.getYesWeightProb((int) (o.getTotalWeight()/10));
			else
				yes = yes * 0.001f;
			
			if(reader.getNoWeightProb((int) (o.getTotalWeight()/3)) != 0)
				no = no * reader.getNoWeightProb((int) (o.getTotalWeight()/10));
			else
				no = no * 0.001f;
			
			if(yes > no)
				o.setPrediction(true);
			else 
				o.setPrediction(false);
		}
		
	}
}
