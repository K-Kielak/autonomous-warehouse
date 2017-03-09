package com.bestroboticsteam.pathfinding;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.bestroboticsteam.pathfinding.AStar;

import rp.util.Pair;

public class AStarTest {

	@Test
	public void testSingleGetPath(){
		List<Point> path;
		path = AStar.singleGetPath(Pair.makePair(new Point(0, 3), new Point(0, 2)));
		List<Point> actualPath = new LinkedList<Point>();
		actualPath.add(new Point(0, 2));
		assertEquals(path, actualPath);
		

		path = AStar.singleGetPath(Pair.makePair(new Point(0, 3), new Point(2, 3)));
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
		
		path = AStar.singleGetPath(Pair.makePair(new Point(0, 3), new Point(1, 3)));
		//Should return null when no path is found
		assertNull(path);
		
		path = AStar.singleGetPath(Pair.makePair(new Point(1, 6), new Point(5, 5)));
		actualPath = new LinkedList<Point>();
		actualPath.add(new Point(2, 6));
		actualPath.add(new Point(3, 6));
		actualPath.add(new Point(4, 6));
		actualPath.add(new Point(5, 6));
		actualPath.add(new Point(5, 5));
		assertEquals(path, actualPath);
		
		path = AStar.singleGetPath(Pair.makePair(new Point(-1, 6), new Point(5, 5)));
		//Should return null when no path is found
		assertNull(path);
		
		path = AStar.singleGetPath(Pair.makePair(new Point(1, 6), new Point(-5, 5)));
		//Should return null when no path is found
		assertNull(path);
	}
	
}
