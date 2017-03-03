package com.bestroboticsteam.jobs;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ReadData {
	
	//tell me if you prefer another type of collection
	private Collection<Item> itemList = new ArrayList<Item>();
	private Collection<Order> orderList = new ArrayList<Order>();
	private Collection<Point> dropLocations = new ArrayList<Point>();
	
	
	//###pls... first read items
	public Collection<Item> readItemData(String path){
		readItemSpecification(path + "/ItemSpecification.csv");
		readItemLocation(path + "/ItemLocation.csv");
		return itemList;
	}
	
	//###then read orders
	public Collection<Order> readOrderData(String path){
		//i'll finish these...
		readOrderSpecification(path + "/OrderSpecifictation.csv");
		readOrderCancelation(path + "/OderCancelation.csv");
		return orderList;
	}
	
	public Collection<Point> readDropData(String path){
		readDropLocations(path + "/DropLocations.csv");
		return dropLocations ;
	}
	
	

	private void readDropLocations(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line;
			
			while((line = reader.readLine()) != null){
				Point p = new Point();
				p.x = Integer.parseInt(line.substring(0, line.indexOf(',')));
				p.y = Integer.parseInt(line.substring(line.indexOf(',')));
				
				dropLocations.add(p);
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	private void readOrderSpecification(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line;
			while( (line = reader.readLine()) != null ){
				
				int id = Integer.parseInt(line.substring(0, line.indexOf(',')));
				line = line.substring(line.indexOf(','));
				
				ConcurrentMap<Item, Integer> order = new ConcurrentHashMap<Item, Integer>(); 
				Item item;
				
				int i;
				while((i = line.indexOf(',')) > 0){
					
					
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
	private void readOrderCancelation(String file){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private void readItemLocation(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line;
			while( (line = reader.readLine()) != null ){
				
				int first = line.indexOf(',');
				int second = line.lastIndexOf(',');
				
				Point p = new Point();
				p.x = Integer.parseInt(line.substring(0, first-1));
				p.y = Integer.parseInt(line.substring(first, second-1));
				String itemCode = line.substring(second);
				
				for(Item i: itemList){
					if(i.getCode().equals(itemCode)){
						i.setPosition(p);
						break;
					}
				}
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}

	private void readItemSpecification(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line;
			while( (line = reader.readLine()) != null ){
				
				int first = line.indexOf(',');
				int second = line.lastIndexOf(',');
				
				String code = line.substring(0, first-1);
				String reward = line.substring(first, second-1);
				String weight = line.substring(second);
				
				itemList.add(new Item(code, Float.parseFloat(reward), Float.parseFloat(weight)));
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
}
