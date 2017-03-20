package com.bestroboticsteam.pathfinding;

import static org.junit.Assert.*;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import org.apache.log4j.Logger;

import com.bestroboticsteam.jobs.JobInfo;
import com.bestroboticsteam.pathfinding.AStar;
import com.bestroboticsteam.robotsmanagement.Direction;
import com.bestroboticsteam.robotsmanagement.RobotInfo;

import rp.util.Pair;

public class AStarTest {

	@BeforeClass
	public static void removeDelay() {
		// The first time these objects are created there is a small delay
		// (~100ms in total)
		// so they are first declared here to get accurate test times.
		new AStar();
		new Rectangle();
		Logger logger = Logger.getLogger(AStar.class);
	}


	@Test
	public void testSingleGetPath() {
		List<Point> path;
		path = AStar.singleGetPath(new Point(0, 3), new Point(0, 2));
		List<Point> actualPath = new LinkedList<Point>();
		actualPath.add(new Point(0, 2));
		assertEquals(path, actualPath);

		path = AStar.singleGetPath(new Point(0, 3), new Point(2, 3));
		actualPath = new LinkedList<Point>();
		actualPath.add(new Point(0, 2));
		actualPath.add(new Point(0, 1));
		actualPath.add(new Point(0, 0));
		actualPath.add(new Point(1, 0));
		actualPath.add(new Point(2, 0));
		actualPath.add(new Point(2, 1));
		actualPath.add(new Point(2, 2));
		actualPath.add(new Point(2, 3));
		assertEquals(path, actualPath);

		path = AStar.singleGetPath(new Point(0, 3), new Point(1, 3));
		// Should return null when no path is found
		assertNull(path);

		path = AStar.singleGetPath(new Point(1, 6), new Point(5, 5));
		actualPath = new LinkedList<Point>();
		actualPath.add(new Point(2, 6));
		actualPath.add(new Point(3, 6));
		actualPath.add(new Point(4, 6));
		actualPath.add(new Point(5, 6));
		actualPath.add(new Point(5, 5));
		assertEquals(path, actualPath);

		path = AStar.singleGetPath(new Point(-1, 6), new Point(5, 5));
		// Should return null when no path is found
		assertNull(path);

		path = AStar.singleGetPath(new Point(1, 6), new Point(-5, 5));
		// Should return null when no path is found
		assertNull(path);
	}

	@Test
	public void testMultiGetPath() {

		AStar aStar = new AStar();

		new Rectangle();

		LinkedList<Point> path;
		Pair<Point, Point> locationDetinationPair = Pair.makePair(new Point(0, 3), new Point(0, 2));
		RobotInfo robotInfo = new RobotInfo("John CENA", new Point(0, 5), Direction.RIGHT, 20);
		path = AStar.multiGetPath(locationDetinationPair, new RobotInfo[] { robotInfo });
		Point[] actualPath = { new Point(0, 2) };
		assertArrayEquals(path.toArray(new Point[path.size()]), actualPath);

		locationDetinationPair = Pair.makePair(new Point(-1, 3), new Point(1, 2));
		path = AStar.multiGetPath(locationDetinationPair, new RobotInfo[] {});
		assertNull(path);

		Pair[] locationDetinationPairs = new Pair[] { Pair.makePair(new Point(0, 3), new Point(0, 1)),
				Pair.makePair(new Point(0, 1), new Point(0, 3)) };
		RobotInfo[] robots = new RobotInfo[] { new RobotInfo("John Cena", new Point(0, 3), Direction.BACKWARD, 20) , new RobotInfo("John Cena2", new Point(0, 1), Direction.BACKWARD, 20) };

		LinkedList<Point>[] paths  = (LinkedList<Point>[]) new LinkedList<?>[2];
		Point[][] actualPaths = new Point[][] { { new Point(0, 2), new Point(0, 1) },
			{ new Point(0, 0), new Point(1, 0), new Point(2, 0), new Point(2, 1), new Point(2, 2), new Point(2, 3),
					new Point(2, 4), new Point(2, 5), new Point(2, 6), new Point(1, 6), new Point(0, 6),
					new Point(0, 5), new Point(0, 4), new Point(0, 3), } };
		for (int i = 0; i < locationDetinationPairs.length; i++) {
			path = AStar.multiGetPath(locationDetinationPairs[i], robots);
			robots[i].setCurrentJob(new JobInfo("", new Point(0, 0)), path);
			paths[i] = path;
			assertArrayEquals(path.toArray(new Point[path.size()]), actualPaths[i]);
		}

		
		
		locationDetinationPairs = new Pair[] { Pair.makePair(new Point(5, 0), new Point(2, 0)),
				Pair.makePair(new Point(3, 2), new Point(5, 0)) };
		robots = new RobotInfo[] { new RobotInfo("John Cena", new Point(5, 0), Direction.BACKWARD, 40) , new RobotInfo("John Cena2", new Point(3, 2), Direction.BACKWARD, 10) };

		paths  = (LinkedList<Point>[]) new LinkedList<?>[2];
		actualPaths = new Point[][] { { new Point(4, 0), new Point(3, 0), new Point(2, 0) },
			{ new Point(2, 2), new Point(2, 1), new Point(3, 1), new Point(3, 0), new Point(4, 0), new Point(5, 0) } };
		for (int i = 0; i < locationDetinationPairs.length; i++) {
			path = AStar.multiGetPath(locationDetinationPairs[i], robots);
			robots[i].setCurrentJob(new JobInfo("", new Point(0, 0)), path);
			paths[i] = path;
			assertArrayEquals(path.toArray(new Point[path.size()]), actualPaths[i]);
		}

		
		System.out.println("start");
		locationDetinationPairs = new Pair[] { Pair.makePair(new Point(5, 0), new Point(3, 2)),
				Pair.makePair(new Point(0, 0), new Point(5, 0)) };
		robots = new RobotInfo[] { new RobotInfo("John Cena", new Point(5, 0), Direction.BACKWARD, 10) , new RobotInfo("John Cena2", new Point(3, 2), Direction.BACKWARD, 20) };

		paths  = (LinkedList<Point>[]) new LinkedList<?>[2];
		actualPaths = new Point[][] { { new Point(4, 0), new Point(3, 0), new Point(3, 1), new Point(3, 2) },
			{ new Point(1, 0), new Point(2, 0), new Point(2, 1), new Point(2, 0), new Point(3, 0), new Point(4, 0), new Point(5, 0) } };
		for (int i = 0; i < locationDetinationPairs.length; i++) {
			path = AStar.multiGetPath(locationDetinationPairs[i], robots);
			robots[i].setCurrentJob(new JobInfo("", new Point(0, 0)), path);
			paths[i] = path;
			assertArrayEquals(path.toArray(new Point[path.size()]), actualPaths[i]);
		}

		
	}

}
