package com.bestroboticsteam.jobs;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.bestroboticsteam.jobs.JobInfo;

public class Order implements Comparable<Order> {

	// do you prefer another type of map?
	private ConcurrentMap<Item, Integer> orderTable;
	private int id;
	private int cancelationNumb;
	private float totalReward = 0f;

	public Order(int _id, ConcurrentMap<Item, Integer> ot) {
		orderTable = ot;
		id = _id;
		cancelationNumb = 0;
		setTotalReward();
	}

	public void setCancelation(int i) {
		cancelationNumb = i; // ??
	}

	public int getCancelationNumb() {
		return cancelationNumb;
	}

	public int getId() {
		return id;
	}

	public int getQuantity(Item j) {
		return orderTable.get(j);
	}

	LinkedList<JobInfo> toJobInfos() { // ? Made package private

		LinkedList<JobInfo> list = new LinkedList<JobInfo>();

		for (Item i : orderTable.keySet()) {
			JobInfo info = new JobInfo(i.getCode(), i.getPosition(), orderTable.get(i), id);
			list.add(info);
		}
		return null;
	}

	public ConcurrentMap<Item, Integer> getOrderTable() {
		return orderTable;
	}

	public float getTotalReward() {
		return totalReward;
	}

	private void setTotalReward() { // ?
		for (Item e : orderTable.keySet()) {
			totalReward += e.getReward() * orderTable.get(e);
		}
	}

	@Override
	public int compareTo(Order compareOrder) {
		float compareReward = compareOrder.getTotalReward();

		if (this.totalReward - compareReward > 0)
			return 1;
		else if (this.totalReward - compareReward == 0)
			return 0;
		else
			return -1;
	}
}
