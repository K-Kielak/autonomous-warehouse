package com.bestroboticsteam.jobs;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.bestroboticsteam.communication.Communicatable;

public class JobInfo implements Communicatable {

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

	@Override
	public void sendObject(DataOutputStream o) throws IOException {
		this.writeString(o, this.itemCode);
		this.writePoint(o, this.position);
		o.writeInt(this.quantity);
		o.writeInt(jobCode);
		
	}

	@Override
	public Object receiveObject(DataInputStream i) throws IOException {
		this.itemCode = this.readString(i);
		this.position = this.readPoint(i);
		this.quantity = i.readInt();
		this.jobCode = i.readInt();
		return this;
	}

}
