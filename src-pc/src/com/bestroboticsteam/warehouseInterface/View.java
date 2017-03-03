package com.bestroboticsteam.warehouseInterface;

import java.awt.Color;
import java.awt.Dimension;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.LineMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.visualisation.GridMapVisualisation;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;


public class View extends JFrame {

	public View() {
		this.setTitle("Warehouse Management Interface");
		this.setSize(900, 500);//set size of frame
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		//left hand panel - map
	//	GridMap gridMap = MapUtils.createMarkingWarehouseMap();
	//	GridMapVisualisation mapVis = new GridMapVisualisation(gridMap, gridMap, 100f);
		//Test
		GridMap map = MapUtils.createRealWarehouse();
		MapBasedSimulation sim = new MapBasedSimulation(map);
		
		GridMapVisualisation mapVis = new GridMapVisualisation(map, sim.getMap());
		
		//right hand panel - jobs
		JPanel jobPanel = new JPanel(); 
		JPanel jobInProgPanel = new JPanel();
		JPanel jobListPanel = new JPanel();
		JLabel toDoLabel = new JLabel("Job List");
		JLabel doingLabel = new JLabel("Jobs in Progress");
		
		toDoLabel.setPreferredSize(new Dimension(150, 20));
		toDoLabel.setOpaque(true);
		doingLabel.setPreferredSize(new Dimension(150, 20));
		doingLabel.setOpaque(true);
		
		jobInProgPanel.setPreferredSize(new Dimension(180, 200));
		jobInProgPanel.setBorder(BorderFactory.createLineBorder(Color.orange));
		jobListPanel.setPreferredSize(new Dimension(180, 200));
		jobListPanel.setBorder(BorderFactory.createLineBorder(Color.green));
		
		jobInProgPanel.add(doingLabel);
		jobListPanel.add(toDoLabel);
		jobPanel.add(jobListPanel);
		jobPanel.add(jobInProgPanel);
		jobListPanel.setVisible(true);
		jobInProgPanel.setVisible(true);
		jobPanel.setVisible(true);
		
		//create split pane and add the map and job panels to it respectively
		JSplitPane split = new JSplitPane();
		split.setSize(700, 300);
		split.setDividerLocation(500);
		split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(mapVis);
		split.setRightComponent(jobPanel);

		this.add(split);

	}

	public static void main(String[] args) {
		View test = new View();

	}

}
