package com.bestroboticsteam.warehouseinterface;

import java.awt.Color;
import java.awt.Dimension;
import rp.robotics.mapping.GridMap;
import rp.robotics.mapping.MapUtils;
import rp.robotics.simulation.MapBasedSimulation;
import rp.robotics.visualisation.GridMapVisualisation;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class InterfaceView extends JFrame {
	//displayText will display list of orders -> from JobSelection class via interfacecontroller
	public String displayText = "";
	public JLabel list = new JLabel();

	public InterfaceView() {

		this.setTitle("Warehouse Management Interface");
		this.setSize(900, 500);// set size of frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// left hand panel - map
		GridMap map = MapUtils.createRealWarehouse();
		MapBasedSimulation sim = new MapBasedSimulation(map);
		//create visualisation of graph
		GridMapVisualisation mapVis = new GridMapVisualisation(map, sim.getMap());

		// right hand panel - jobs
		JPanel jobPanel = new JPanel();
		JPanel jobInProgPanel = new JPanel();
		JPanel jobListPanel = new JPanel();
		JLabel toDoLabel = new JLabel("Job List");
		JLabel doingLabel = new JLabel("Jobs in Progress");
		
		//list of jobs in progress 
		toDoLabel.setPreferredSize(new Dimension(150, 20));
		toDoLabel.setOpaque(true);
		//list of first 10 jobs
		doingLabel.setPreferredSize(new Dimension(150, 20));
		doingLabel.setOpaque(true);
		list.setPreferredSize(new Dimension(150, 150));
		list.setOpaque(true);

		jobInProgPanel.setPreferredSize(new Dimension(180, 200));
		//this is just for testing - ignore it
		jobInProgPanel.setBorder(BorderFactory.createLineBorder(Color.orange));
		jobListPanel.setPreferredSize(new Dimension(180, 200));
		//this is just for testing - ignore it
		jobListPanel.setBorder(BorderFactory.createLineBorder(Color.green));
		//this is just for testing - ignore it
		list.setBorder(BorderFactory.createLineBorder(Color.pink));
		
		jobListPanel.add(toDoLabel);
		jobListPanel.add(list);
		jobInProgPanel.add(doingLabel);
		jobPanel.add(jobListPanel);
		jobPanel.add(jobInProgPanel);
		jobListPanel.setVisible(true);
		jobInProgPanel.setVisible(true);
		jobPanel.setVisible(true);

		// create split pane and add the map and job panels to it respectively
		JSplitPane split = new JSplitPane();
		split.setSize(700, 300);
		split.setDividerLocation(500);
		split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(mapVis);
		split.setRightComponent(jobPanel);
		
		//add split pane to frame
		this.add(split);
		this.setVisible(true);

	}
}
