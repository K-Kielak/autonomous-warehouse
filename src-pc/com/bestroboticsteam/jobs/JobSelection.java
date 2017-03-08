package com.bestroboticsteam.jobs;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class JobSelection {
	
	private LinkedList<Order> list;
	private Collection<Item> itemList;
	private LinkedList<Point> dropLocation;
	
	public JobSelection(String path){
		
		ReadData reader = new ReadData();
		itemList = reader.readItemData(path);
		dropLocation = reader.readDropData(path);
		
		list = reader.readOrderData(path);
		setList(list);
	}
	
	public LinkedList<Point> getDropLocation(){
		return dropLocation;
	}
	
	public Collection<Item> getItemList(){
		return itemList;
	}

	public Order take(){

		while (true) {
			list.getFirst();	
			list.removeFirst();
		}
	}
	
	public Order viewOrder(int i){
		return list.get(i);
		
	}
	
	private void setList(Collection<Order> orderList) {
		
		Comparator<Order> comparator = new Comparator<Order>() {
			   @Override
			   public int compare(Order o1, Order o2) {
			       return o1.compareTo(o2);
			   }
			};
		
		list.sort(comparator);
	}

}
