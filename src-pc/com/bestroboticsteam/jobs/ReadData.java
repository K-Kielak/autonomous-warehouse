package com.bestroboticsteam.jobs;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class ReadData {
	
	
	private Collection<Item> itemList = new ArrayList<Item>();
	private LinkedList<Order> orderList = new LinkedList<Order>();
	private LinkedList<Point> dropLocations = new LinkedList<Point>();
	
	
	
	public Collection<Item> readItemData(String path){
		readItemSpecification(path + "/ItemSpecification.csv");
		readItemLocation(path + "/ItemLocation.csv");
		return itemList;
	}
	
	
	public LinkedList<Order> readOrderData(String path){
		//i'll finish these...
		readOrderSpecification(path + "/OrderSpecifictation.csv");
		readOrderCancelation(path + "/OderCancelation.csv");
		return orderList;
	}
	
	public LinkedList<Point> readDropData(String path){
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
				p.y = Integer.parseInt(line.substring(line.indexOf(',')+1));
				
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
				line = line.substring(line.indexOf(',')+1);
				
				ConcurrentHashMap<Item, Integer> orderTable = new ConcurrentHashMap<Item, Integer>(); 
				
				String code;
				int quantity;
				int i;
				
				while((i = line.indexOf(',')) > 0){
					
					code = line.substring(0,i);
					line = line.substring(i+1);
					
					if(line.indexOf(',') > 0){
						quantity = Integer.parseInt(line.substring(0, line.indexOf(',')));
						line = line.substring(i+1);
					}else
						quantity = Integer.parseInt(line);
					
					for(Item item: itemList){
						if(item.getCode().equals(code)){
							orderTable.putIfAbsent(item, quantity);
							break;
						}
					}
				}
				orderList.add(new Order(id,orderTable));
				
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
	}
	
	//i don't understand the file cancellation... is it for learning machine or just the list with the job that will be canceled
	//the functionality of this one does not affect the project..
	private void readOrderCancelation(String file){
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line;
			
			while((line = reader.readLine()) != null){
				
				int codeOrder = Integer.parseInt(line.substring(0, line.indexOf(',')));
				int value = Integer.parseInt(line.substring(line.indexOf(',')+1));
				
				for(Order o: orderList){
					if(o.getId() == codeOrder)
						o.setCancelation(value);
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

	private void readItemLocation(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			String line;
			while( (line = reader.readLine()) != null ){
				
				int first = line.indexOf(',');
				int second = line.lastIndexOf(',');
				
				Point p = new Point();
				p.x = Integer.parseInt(line.substring(0, first));
				p.y = Integer.parseInt(line.substring(first+1, second));
				String itemCode = line.substring(second+1);
				
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
				
				String code = line.substring(0, first);
				String reward = line.substring(first+1, second);
				String weight = line.substring(second+1);
				
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
