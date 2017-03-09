package com.bestroboticsteam.pathfinding;

import java.awt.Point;

public class TimePoint {

	private Point location;

	private int time;

	public TimePoint(Point location, int time) {
		this.location = location;
		this.time = time;
	}

	public int getTime() {
		return time;
	}

	public Point getLocation() {
		return location;
	}

	// Needed to store in Map
	@Override
	public int hashCode() {
		return location.hashCode() + 31 * ((Integer) time).hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o == null || !(o instanceof TimePoint)) {
			return false;
		}
		TimePoint tp = (TimePoint) o;
		return tp.time == this.time && tp.location.equals(this.location);
	}

}
