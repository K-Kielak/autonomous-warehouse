package com.bestroboticsteam.jobs;

import static org.junit.Assert.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.BeforeClass;
import org.junit.Test;

public class ReadingTest {
	
	private static ArrayList<Item> items;
	private static LinkedList<Order> orders;
	private static LinkedList<Point> points;

	@BeforeClass
	public static void ReadData(){
		ReadData reader = new ReadData();
		items = (ArrayList<Item>) reader.readItemData("../central-system/assets/test");
		orders = reader.readOrderData("../central-system/assets/test");
		points = reader.readDropData("../central-system/assets/test");
	}
	
	@Test
	public void TestReadItem1(){
		
		ArrayList<Item> itemCheck = new ArrayList<Item>();
		
		itemCheck.add(new Item("a", 1.0f, 2.0f));
		itemCheck.add(new Item("b", 3253.3231f, 23.43f));
		itemCheck.add(new Item("c", 0.11111f, 1f));
		itemCheck.add(new Item("d", 10f, 23.3f));
		itemCheck.add(new Item("f", 20000f, 0.1f));
		
		assertArrayEquals(items.toArray(), itemCheck.toArray());
		
		assertEquals(items.get(0).getReward(), itemCheck.get(0).getReward(), 0.0001f);
		assertEquals(items.get(0).getWeight(), itemCheck.get(0).getWeight(), 0.0001f);
		
		assertEquals(items.get(1).getReward(), itemCheck.get(1).getReward(), 0.0001f);
		assertEquals(items.get(1).getWeight(), itemCheck.get(1).getWeight(), 0.0001f);
		
		assertEquals(items.get(2).getReward(), itemCheck.get(2).getReward(), 0.0001f);
		assertEquals(items.get(2).getWeight(), itemCheck.get(2).getWeight(), 0.0001f);
		
		assertEquals(items.get(3).getReward(), itemCheck.get(3).getReward(), 0.0001f);
		assertEquals(items.get(3).getWeight(), itemCheck.get(3).getWeight(), 0.0001f);
		
		assertEquals(items.get(4).getReward(), itemCheck.get(4).getReward(), 0.0001f);
		assertEquals(items.get(4).getWeight(), itemCheck.get(4).getWeight(), 0.0001f);
	}
	
	@Test
	public void TestReadItem2(){
		
		assertEquals(items.get(0).getPosition().x, 1);
		assertEquals(items.get(0).getPosition().y, 1);
		assertEquals(items.get(1).getPosition().x, 2);
		assertEquals(items.get(1).getPosition().y, 1);
		assertEquals(items.get(2).getPosition().x, 3);
		assertEquals(items.get(2).getPosition().y, 2);
		assertEquals(items.get(3).getPosition().x, 2);
		assertEquals(items.get(3).getPosition().y, 1);
		assertEquals(items.get(4).getPosition().x, 2);
		assertEquals(items.get(4).getPosition().y, 1);
	}

	@Test
	public void TestReadOrder1(){
		
		LinkedList<Order> orderCheck = new LinkedList<Order>();
		
		ConcurrentHashMap<Item, Integer> orderTable = new ConcurrentHashMap<Item, Integer>();
		
		orderTable.put(items.get(0), 1);
		orderTable.put(items.get(1), 2);
		orderTable.put(items.get(2), 20);
		
		Order order = new Order(1001, orderTable);
		
		assertEquals(order,orders.get(0));
		
		assertEquals(orders.get(0).getQuantity(items.get(0)), 1);
		assertEquals(orders.get(0).getQuantity(items.get(1)), 2);
		assertEquals(orders.get(0).getQuantity(items.get(2)), 20);
		
		
		orderTable.clear();
		
		orderTable.put(items.get(0), 5);
		orderTable.put(items.get(1), 3);
		
		order = new Order(1002, orderTable);
		
		assertEquals(order, orders.get(1));
		
		assertEquals(orders.get(1).getQuantity(items.get(0)), 5);
		assertEquals(orders.get(1).getQuantity(items.get(1)), 3);

		
		orderTable.clear();
		
		orderTable.put(items.get(2), 3);
		orderTable.put(items.get(3), 1);
		
		order = new Order(1003, orderTable);
		
		assertEquals(order, orders.get(2));
		
		assertEquals(orders.get(2).getQuantity(items.get(2)), 3);
		assertEquals(orders.get(2).getQuantity(items.get(3)), 1);
		
	}
	
	@Test
	public void TestReadOrder2(){
		
		assertEquals(orders.get(0).getCancelationNumb(),0);
		assertEquals(orders.get(1).getCancelationNumb(),1);
		assertEquals(orders.get(2).getCancelationNumb(),0);
	
	}
	
	@Test
	public void TestReadDropPoint(){
		
		assertEquals(points.get(0).x, 7);
		assertEquals(points.get(0).y, 6);
		
		assertEquals(points.get(1).x, 5);
		assertEquals(points.get(1).y, 3);
		
	}
}
