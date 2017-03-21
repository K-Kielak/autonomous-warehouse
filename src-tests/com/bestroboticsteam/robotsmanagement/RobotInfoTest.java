package com.bestroboticsteam.robotsmanagement;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.LinkedList;

import org.junit.Test;

import com.bestroboticsteam.jobs.JobInfo;

public class RobotInfoTest {

	@Test
	public void moveTest() {
		RobotInfo info = new RobotInfo("xd", new Point(0, 0), Direction.FORWARD, 20);
		LinkedList<Point> path = new LinkedList<Point>();
		path.add(new Point(1, 0)); path.add(new Point(2, 0)); path.add(new Point(3, 0)); path.add(new Point(3, 1));
		path.add(new Point(3, 0)); path.add(new Point(3, 0));
		JobInfo job = new JobInfo("", new Point(11, 6));
		info.setCurrentJob(job);
		assertEquals(Direction.RIGHT, info.move());
		assertEquals(Direction.FORWARD, info.move());
		assertEquals(Direction.FORWARD, info.move());
		assertEquals(Direction.LEFT, info.move());
		assertEquals(Direction.BACKWARD, info.move());
		assertNull(info.move());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void wrongPathTest(){
		RobotInfo info = new RobotInfo("xd", new Point(0, 0), Direction.FORWARD, 20);
		LinkedList<Point> path = new LinkedList<Point>();
		path.add(new Point(1, 0)); path.add(new Point(2, 1));
		JobInfo job = new JobInfo("", new Point(11, 6));
		info.setCurrentJob(job);
		assertEquals(Direction.RIGHT, info.move());
		info.move();
	}

}
