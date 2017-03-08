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

	public JobSelection(String path) {

		ReadData reader = new ReadData();
		itemList = reader.readItemData(path);
		dropLocation = reader.readDropData(path);

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
		return list.get(i);

	}

	private synchronized void setList(Collection<Order> orderList) {

		Comparator<Order> comparator = new Comparator<Order>() {
			@Override
			public int compare(Order o1, Order o2) {
				return o1.compareTo(o2);
			}
		};

		list.sort(comparator);
	}

}
