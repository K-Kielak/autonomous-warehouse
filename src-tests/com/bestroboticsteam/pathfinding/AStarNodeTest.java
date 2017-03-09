package com.bestroboticsteam.pathfinding;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Test;

public class AStarNodeTest {
	
	@Test
	public void TestConstructor(){
		AStarNode node = new AStarNode(new Point(1,2), new AStarNode(true), 2, 1, 1);
		assertEquals(node.location, new Point(1,2));
		assertTrue(node.parentNode.isStart);
		assertEquals(node.fCost, 2);
		assertEquals(node.gCost, 1);
		assertEquals(node.hCost, 1);
	}
	
}
