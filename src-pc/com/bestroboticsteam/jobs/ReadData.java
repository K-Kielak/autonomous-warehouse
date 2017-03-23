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

import org.apache.log4j.Logger;

public class ReadData {

	final Logger logger = Logger.getLogger(ReadData.class);

	private Collection<Item> itemList = new ArrayList<Item>();
	private LinkedList<Order> orderList = new LinkedList<Order>();
	private LinkedList<Point> dropLocations = new LinkedList<Point>();
	private ArrayList<Order> trainingSet = new ArrayList<Order>();
	
	private int[][] rewardProb = new int[50][3];
	private int[][] weightProb = new int[50][3];

	public Collection<Item> readItemData(String path) {
		
		//First read the specification and than the positions
		readItemSpecification(path + "/items.csv");
		readItemLocation(path + "/locations.csv");
		readTrainingSet(path + "/training_jobs.csv");
		readCancellations(path + "/cancellations.csv");
		return itemList;
	}

	public LinkedList<Order> readOrderData(String path) {
		
		//First read the specifications and than the cancellations
		readOrderSpecification(path + "/jobs.csv");
		return orderList;
	}
	
	public float getNoWeightProb(int i){
		return weightProb[i][2]/weightProb[i][0];
	}
	
	public float getYesWeightProb(int i){
		return weightProb[i][1]/weightProb[i][0];
	}
	
	public float getYesRewardProb(int i){
		return rewardProb[i][1]/rewardProb[i][0];
	}
	
	public float getNoRewardProb(int i){
		return rewardProb[i][2]/rewardProb[i][0];
	}

	public LinkedList<Point> readDropData(String path) {
		readDropLocations(path + "/drops.csv");
		return dropLocations;
	}

	private void readDropLocations(String file) {
		try {
			BufferedReader reader = readAFile(file);

			String line;

			while ((line = reader.readLine()) != null && !line.isEmpty()) {
				Point p = new Point();
				p.x = Integer.parseInt(line.substring(0, line.indexOf(',')));
				p.y = Integer.parseInt(line.substring(line.indexOf(',') + 1));

				dropLocations.add(p);
			}
			reader.close();

		} catch (IOException e) {
			logger.error("I/O error: ", e);
		}

	}

	private void readOrderSpecification(String file) {
		try {
			BufferedReader reader = readAFile(file);

			String line;
			while ((line = reader.readLine()) != null) {

				int id = Integer.parseInt(line.substring(0, line.indexOf(',')));
				line = line.substring(line.indexOf(',') + 1);

				ConcurrentHashMap<Item, Integer> orderTable = new ConcurrentHashMap<Item, Integer>();

				String code;
				int quantity;
				int i;

				while ((i = line.indexOf(',')) > 0) {

					code = line.substring(0, i);
					line = line.substring(i + 1);

					if (line.indexOf(',') > 0) {
						quantity = Integer.parseInt(line.substring(0, line.indexOf(',')));
						line = line.substring(line.indexOf(',') + 1);
					} else
						quantity = Integer.parseInt(line);

					for (Item item : itemList) {
						if (item.getCode().equals(code)) {
							orderTable.putIfAbsent(item, quantity);
							break;
						}
					}
				}
				orderList.add(new Order(id, orderTable));
			}
			reader.close();
		} catch (IOException e) {
			logger.error("I/O error: ", e);
		}
	}

	public void readTrainingSet(String file){
		try {
			BufferedReader reader = readAFile(file);

			String line;
			while ((line = reader.readLine()) != null) {

				int id = Integer.parseInt(line.substring(0, line.indexOf(',')));
				line = line.substring(line.indexOf(',') + 1);

				ConcurrentHashMap<Item, Integer> orderTable = new ConcurrentHashMap<Item, Integer>();

				String code;
				int quantity;
				int i;

				while ((i = line.indexOf(',')) > 0) {

					code = line.substring(0, i);
					line = line.substring(i + 1);

					if (line.indexOf(',') > 0) {
						quantity = Integer.parseInt(line.substring(0, line.indexOf(',')));
						line = line.substring(line.indexOf(',') + 1);
					} else
						quantity = Integer.parseInt(line);

					for (Item item : itemList) {
						if (item.getCode().equals(code)) {
							orderTable.putIfAbsent(item, quantity);
							break;
						}
					}
				}
				trainingSet.add(new Order(id, orderTable));
			}
			reader.close();
		} catch (IOException e) {
		}
	}
	
	private void readCancellations(String file) {
		try {
			BufferedReader reader = readAFile(file);

			String line;

			while ((line = reader.readLine()) != null) {

				int codeOrder = Integer.parseInt(line.substring(0, line.indexOf(',')));
				int value = Integer.parseInt(line.substring(line.indexOf(',') + 1));

				for (Order o : trainingSet) {
					if (o.getId() == codeOrder){
						
						for(Item i: o.getOrderTable().keySet()){
							if(value == 1){
								i.incrementYesProbability(o.getQuantity(i));
								rewardProb[(int) (o.getTotalReward()/5)][1]++;
								rewardProb[(int) (o.getTotalReward()/5)][0]++;
								weightProb[(int) (o.getTotalWeight()/3)][1]++;
								weightProb[(int) (o.getTotalWeight()/3)][0]++;
							}else{
								i.incrementNoProbability(o.getQuantity(i));
								rewardProb[(int) (o.getTotalReward()/5)][2]++;
								rewardProb[(int) (o.getTotalReward()/5)][0]++;
								weightProb[(int) (o.getTotalWeight()/3)][2]++;
								weightProb[(int) (o.getTotalWeight()/3)][0]++;
							}
							i.incrementOccurrence(o.getQuantity(i));
						}
					}
				}

			}
			reader.close();
		} catch (IOException e) {
		}
	}

	private void readItemLocation(String file) {
		try {
			BufferedReader reader = readAFile(file);

			String line;
			while ((line = reader.readLine()) != null) {

				int first = line.indexOf(',');
				int second = line.lastIndexOf(',');

				Point p = new Point();
				p.x = Integer.parseInt(line.substring(0, first));
				p.y = Integer.parseInt(line.substring(first + 1, second));
				String itemCode = line.substring(second + 1);

				for (Item i : itemList) {
					if (i.getCode().equals(itemCode)) {
						i.setPosition(p);
						break;
					}
				}

			}
			reader.close();
		} catch (IOException e) {
			logger.error("I/O error: ", e);
		}

	}

	private void readItemSpecification(String file) {
		try {
			BufferedReader reader = readAFile(file);

			String line;
			while ((line = reader.readLine()) != null) {

				int first = line.indexOf(',');
				int second = line.lastIndexOf(',');

				String code = line.substring(0, first);
				String reward = line.substring(first + 1, second);
				String weight = line.substring(second + 1);

				itemList.add(new Item(code, Float.parseFloat(reward), Float.parseFloat(weight)));

			}
			reader.close();
		} catch (IOException e) {
			logger.error("I/O error : ", e);
		}

	}

	private BufferedReader readAFile(String filename) {
		/**
		 * Helper function to open files to remove duplicated code. Logs
		 * Exceptions to logger
		 */
		try {
			logger.info("Openning file");
			BufferedReader reader = new BufferedReader(new FileReader(filename));
			return reader;
		} catch (FileNotFoundException e) {
			logger.error("File not found: " + filename, e);
			return null;
		}

	}
}
