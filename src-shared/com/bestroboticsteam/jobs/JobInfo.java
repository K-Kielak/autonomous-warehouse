package com.bestroboticsteam.jobs;

import java.awt.Point;

public class JobInfo {
	// Transmittable

	private String itemCode;
	private Point position;
	private int quantity;
	private int jobCode;

	public JobInfo(String code, Point position, int _quantity, int _jobCode) {

		this.itemCode = code;
		this.position = position;
		this.quantity = _quantity;
		this.jobCode = _jobCode;
	}

	public JobInfo(String code, Point position) {
		this.itemCode = code;
		this.position = position;
		this.quantity = 0;
		this.jobCode = 0;
	}

	public String getItem() {
		return itemCode;
	}

	public Point getPosition() {
		return position;
	}

	public int getQuantity() {
		return quantity;
	}

	public int getJobCode() {
		return jobCode;
	}

}
