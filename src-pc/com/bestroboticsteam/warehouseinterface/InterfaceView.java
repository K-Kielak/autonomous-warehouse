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
import javax.swing.JTextArea;

import org.apache.log4j.Logger;

import com.bestroboticsteam.jobs.Order;

public class InterfaceView extends JFrame {
	final static Logger logger = Logger.getLogger(InterfaceView.class);
	// Go on right hand panel - jobs
	public JPanel jobInProgPanel = new JPanel();
	public JPanel jobListPanel = new JPanel();
	// left hand panel - map
	public GridMap map = MapUtils.createRealWarehouse();
	public MapBasedSimulation sim = new MapBasedSimulation(map);
	// create visualisation of graph
	public GridMapVisualisation mapVis = new GridMapVisualisation(map, sim.getMap());
	
	// jobListText will display list of orders -> from JobSelection class via interfaceController
	public String jobListText = "";
	public JTextArea text = new JTextArea();
	
	// jobProgText will display orders in progress (not yet implemented)
	public String jobProgText = "";
	public JTextArea text2 = new JTextArea();

	public InterfaceView() {
		this.setTitle("Warehouse Management Interface");
		this.setSize(1200, 500);// set size of frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// right hand panel - jobs
		JPanel jobPanel = new JPanel();
		JLabel toDoLabel = new JLabel("Job List");
		JLabel doingLabel = new JLabel("Jobs in Progress");
		
		// list of jobs in progress
		toDoLabel.setPreferredSize(new Dimension(150, 20));
		toDoLabel.setOpaque(true);
		text2.setPreferredSize(new Dimension(270, 350));
		text2.setOpaque(true);
		
		// list of first 10 jobs
		doingLabel.setPreferredSize(new Dimension(150, 20));
		doingLabel.setOpaque(true);
		text.setPreferredSize(new Dimension(270, 350));
		text.setOpaque(true);
		text.setEditable(false);
		
		jobInProgPanel.setPreferredSize(new Dimension(300, 400));
		jobListPanel.setPreferredSize(new Dimension(300, 400));
		
		// this is just for testing - ignore it
		jobInProgPanel.setBorder(BorderFactory.createLineBorder(Color.orange));
		jobListPanel.setBorder(BorderFactory.createLineBorder(Color.green));
		text.setBorder(BorderFactory.createLineBorder(Color.pink));
		text2.setBorder(BorderFactory.createLineBorder(Color.red));
		
		jobListPanel.add(toDoLabel);
		jobListPanel.add(text);
		jobInProgPanel.add(doingLabel);
		jobInProgPanel.add(text2);
		jobPanel.add(jobListPanel);
		jobPanel.add(jobInProgPanel);
		jobListPanel.setVisible(true);
		jobInProgPanel.setVisible(true);
		jobPanel.setVisible(true);
		
		// create split pane and add the map and job panels to it respectively
		JSplitPane split = new JSplitPane();
		split.setSize(1000, 300);
		split.setDividerLocation(500);
		split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(mapVis);
		split.setRightComponent(jobPanel);
		
		// add split pane to frame
		this.add(split);
		this.setVisible(true);
	}

	// set method for job list
	public void setJobList(String jobs) {
		logger.debug("Input fron IC " + jobs);
		String newline = "\n";
		String[] parts = jobs.split(" : ");
		for (int i = 0; i < parts.length; i++){
			logger.debug("order adding to list: " + parts[i]);
			text.append(parts[i] + newline);
		}
	}

	public void emptyJobList() {
		jobListText = "";
		text.setText(jobListText);
	}

	// get method for job list
	public String getJobList() {
		return jobListText;
	}

	// set method for job in progress list
	public void setInProgList(String jobProgText) {
		this.jobProgText = jobProgText;
	}

	// get method for job in progress list
	public String getInProgList() {
		return jobProgText;
	}
}