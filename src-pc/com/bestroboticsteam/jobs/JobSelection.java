package com.bestroboticsteam.jobs;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

public class JobSelection {

	private LinkedList<Order> list;
	private Collection<Item> itemList;
	private LinkedList<Point> dropLocation;
	
	final Logger logger = Logger.getLogger(JobSelection.class);

	public JobSelection(String path) {

		// Read all the data
		
		ReadData reader = new ReadData();
		itemList = reader.readItemData(path);
		dropLocation = reader.readDropData(path);

		//Create a LinkedList of Orders and sort it
		list = reader.readOrderData(path);
		setList(list);
	}

	public synchronized LinkedList<Point> getDropLocation() {
		return dropLocation;
	}

	public synchronized Collection<Item> getItemList() {
		return itemList;
	}

	public synchronized Order take() {
		return list.pop();
	}

	public synchronized Order viewOrder(int i) {
		if(i >= list.size()){
			logger.error("Null in Order collection!");
			return null;
		}
		
		return list.get(i);

	}
	
	public void cancelOrder(String order){
		for(Order element: list){
			if(Float.toString(element.getId()).equals(order)){
				list.remove(element);
				break;
			}
		}
	}

	private synchronized void setList(Collection<Order> orderList) {

		Comparator<Order> comparator = new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				return o1.compareTo(o2);
			}
		};
		
		logger.info("Sorting list.");
		list.sort(comparator);
	}

}
