package com.bestroboticsteam.jobs;

import java.awt.Point;
import java.io.IOException;

import com.bestroboticsteam.communication.Communicatable;
import com.bestroboticsteam.communication.MyDataInputStream;
import com.bestroboticsteam.communication.MyDataOutputStream;

public class JobInfo implements Communicatable {

	private String itemCode;
	private Point position;
	private int quantity;
	private int jobCode;
	private float weight;
	
	//THIS IS FOR ASSIGNMENT! don't touch it :D
	private int cost;

	public JobInfo(String code, Point position, int _quantity, int _jobCode, float weight) {

		this.itemCode = code;
		this.position = position;
		this.quantity = _quantity;
		this.jobCode = _jobCode;
		this.weight = weight;
	}
	
	public JobInfo(String code, Point position) {
		this.itemCode = code;
		this.position = position;
		this.quantity = 1;
		this.jobCode = 0;
	}
	
	public JobInfo() {
		this.itemCode = "";
		this.position = new Point();
		this.quantity = 0;
		this.jobCode = 0;
	}
	

	public void setCost(int cost){
		this.cost = cost;
	}
	
	public int getCost(){
		return cost;
	}

	public boolean isGoingToDropPoint() {
		return itemCode.equals("DropBox");

	}

	public String getItem() {
		return itemCode;
	}

	public Point getPosition() {
		return position;
	}
	
	public float getWeight(){
		return this.weight;
	}

	public int getQuantity() {
		return quantity;
	}
	
	public void pickAll(){
		quantity = 0;
	}

	public int getJobCode() {
		return jobCode;
	}
	
	@Override
	public boolean equals(Object info){
		JobInfo j = (JobInfo)info;
		
		if(this.itemCode.equals(j.getItem()) && this.jobCode == j.getJobCode())
			return true;
		else return false;
	}

	public void sendObject(MyDataOutputStream o) throws IOException {
		o.writeString(this.itemCode);
		o.writePoint(this.position);
		o.writeInt(this.quantity);
		o.writeInt(jobCode);

	}

	@Override
	public Object receiveObject(MyDataInputStream i) throws IOException {
		this.itemCode = i.readString();
		this.position = i.readPoint();
		this.quantity = i.readInt();
		this.jobCode = i.readInt();
		return this;
	}
}
