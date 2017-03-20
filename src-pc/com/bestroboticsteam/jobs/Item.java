package com.bestroboticsteam.jobs;

import java.awt.Point;

public class Item {
	// Transmittable maybe

	private final String code;

	private final float weight;
	private final float reward;
	private Point position;
	private int[][] probability = new int[50][3];
	
	public Item(String _code, float _reward, float _weight) {
		this.code = _code;
		this.weight = _weight;
		this.reward = _reward;
	}
	
	public void incrementYesProbability(int quantity){
		probability[quantity][1]++;
	}
	
	public void incrementNoProbability(int quantity){
		probability[quantity][2]++;
	}
	
	public float getYesProbability(int quantity){
		return (float)probability[quantity][1]/probability[quantity][0];
	}

	public float getNoProbability(int quantity){
		return (float)probability[quantity][2]/probability[quantity][0];
	}
	
	public void incrementOccurrence(int quantity){
		probability[quantity][0]++;
	}
	
	public int getOccurrence(int quantity){
		return probability[quantity][0];
	}

	public void setPosition(Point p) {
		this.position = p;
	}

	public String getCode() {
		return code;
	}

	public Point getPosition() {
		return position;
	}

	public float getWeight() {
		return weight;
	}

	public float getReward() {
		return reward;
	}

	public int hashCode() {
		return code.hashCode();
	}

	public boolean equals(Object i) {
		return (this.code.equals(((Item) i).getCode()));
	}
}
