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
		path.add(new Point(3, 0));
		info.setCurrentPath(path);
		assertEquals(Direction.RIGHT, info.move());
		assertEquals(Direction.FORWARD, info.move());
		assertEquals(Direction.FORWARD, info.move());
		assertEquals(Direction.LEFT, info.move());
		assertEquals(Direction.BACKWARD, info.move());
		assertNull(info.move());
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void wrongPathTest(){
		RobotInfo info = new RobotInfo("xd", new Point(0, 0), Direction.FORWARD, 20);
		LinkedList<Point> path = new LinkedList<Point>();
		path.add(new Point(1, 0)); path.add(new Point(2, 1));
		info.setCurrentPath(path);
		assertEquals(Direction.RIGHT, info.move());
		info.move();
	}
	
	@Test
	public void pickAllTest(){
		RobotInfo info = new RobotInfo("xd", new Point(0, 0), Direction.FORWARD, 20);
		JobInfo job = new JobInfo("xd", new Point(0, 0), 13, 21, 10f);
		info.setCurrentJob(job);
		assertEquals(0f, info.getCurrentLoad(), 0);
		info.pickAll();
		assertEquals(130f, info.getCurrentLoad(), 0);
		job = new JobInfo("xd", new Point(0, 0), 2, 21, 4.5f);
		info.setCurrentJob(job);
		assertEquals(130f, info.getCurrentLoad(), 0);
		info.pickAll();
		assertEquals(139f, info.getCurrentLoad(), 0);
		job = new JobInfo("DropBox", new Point(0, 0), 123);
		info.setCurrentJob(job);
		assertEquals(139f, info.getCurrentLoad(), 0);
		info.pickAll();
		assertEquals(0f, info.getCurrentLoad(), 0);
		job = new JobInfo("xd", new Point(0, 0), 13, 21, 10f);
		info.setCurrentJob(job);
		assertEquals(0f, info.getCurrentLoad(), 0);
		info.pickAll();
		assertEquals(130f, info.getCurrentLoad(), 0);
	}

}
