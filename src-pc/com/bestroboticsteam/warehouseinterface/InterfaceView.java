package com.bestroboticsteam.warehouseinterface;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import rp.robotics.visualisation.GridMapVisualisation;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import org.apache.log4j.Logger;

import com.bestroboticsteam.robotsmanagement.RobotsManager;

public class InterfaceView extends JFrame {
	final static Logger logger = Logger.getLogger(InterfaceView.class);

	// Go on right hand panel - jobs
	public JPanel jobInProgPanel = new JPanel();
	public JPanel jobListPanel = new JPanel();
	public JPanel jobFinishedPanel = new JPanel();
	public JPanel robotStatus = new JPanel();
	public JTextArea statusText = new JTextArea();
	public JTextArea fishedJobText = new JTextArea();
	// will display list of orders -> from JobSelection class via interfaceController
	public JTextArea nextJobText = new JTextArea();
	// will display orders in progress -> from jobAssignment via iC
	public JTextArea currentJobText = new JTextArea();
	// create visualisation of graph
	GridMapVisualisation mapVis;
	//cancellation stuff
	public JTextArea cancelTextU = new JTextArea();
    public JButton cancelUpcoming = new JButton();
	public JTextArea cancelTextC = new JTextArea();
    public JButton cancelCurrent = new JButton();
	public JLabel reward = new JLabel();
	public InterfaceView(RobotsManager robots) {
		mapVis = CreateSimRobots.robots(robots);
		this.setTitle("Warehouse Management Interface");
		this.setSize(1300, 600);// set size of frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// right hand panel - jobs
		JPanel jobPanel = new JPanel();
		JLabel toDoLabel = new JLabel("Job List");
		JLabel doingLabel = new JLabel("Jobs in Progress");
		JLabel finLabel = new JLabel("Jobs recently completed");
		JLabel commLabel = new JLabel("Robot Status");

		// list of jobs in progress
		toDoLabel.setPreferredSize(new Dimension(150, 20));
		toDoLabel.setOpaque(true);
		currentJobText.setPreferredSize(new Dimension(220, 150));
		currentJobText.setOpaque(true);
		currentJobText.setEditable(false);
		currentJobText.setBackground(null);
		// list of first 10 jobs
		doingLabel.setPreferredSize(new Dimension(150, 20));
		doingLabel.setOpaque(true);
		nextJobText.setPreferredSize(new Dimension(220, 150));
		nextJobText.setOpaque(true);
		nextJobText.setEditable(false);
		nextJobText.setBackground(null);
		
		jobInProgPanel.setPreferredSize(new Dimension(350, 200));
		jobListPanel.setPreferredSize(new Dimension(350, 200));
		jobFinishedPanel.setPreferredSize(new Dimension(350, 200));	
		robotStatus.setPreferredSize(new Dimension(350, 100));
		
		cancelUpcoming.setPreferredSize(new Dimension(30,20));
		cancelTextU.setPreferredSize(new Dimension(50, 30));
		cancelTextU.setOpaque(true);
		cancelTextU.setBorder(BorderFactory.createLineBorder(Color.black));
		cancelCurrent.setPreferredSize(new Dimension(30,20));
		cancelTextC.setPreferredSize(new Dimension(50, 30));
		cancelTextC.setOpaque(true);
		cancelTextC.setBorder(BorderFactory.createLineBorder(Color.black));
		
		fishedJobText.setPreferredSize(new Dimension(300, 150));
		fishedJobText.setOpaque(true);
		fishedJobText.setEditable(false);
		fishedJobText.setBorder(BorderFactory.createLineBorder(Color.green));
		fishedJobText.setBackground(null);
		
		statusText.setPreferredSize(new Dimension(300, 60));
		statusText.setOpaque(true);
		statusText.setEditable(false);
		statusText.setBorder(BorderFactory.createLineBorder(Color.green));
		statusText.setBackground(null);
		
		jobInProgPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		jobListPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		jobFinishedPanel.setBorder(BorderFactory.createLineBorder(Color.blue));
		nextJobText.setBorder(BorderFactory.createLineBorder(Color.green));
		currentJobText.setBorder(BorderFactory.createLineBorder(Color.green));
		robotStatus.setBorder(BorderFactory.createLineBorder(Color.blue));
		
		jobListPanel.add(toDoLabel);
		jobListPanel.add(nextJobText);
		jobListPanel.add(cancelTextU);
		jobListPanel.add(cancelUpcoming);
		jobInProgPanel.add(doingLabel);
		jobInProgPanel.add(currentJobText);
		jobInProgPanel.add(cancelTextC);
		jobInProgPanel.add(cancelCurrent);
		jobFinishedPanel.add(finLabel);
		jobFinishedPanel.add(fishedJobText);
		jobFinishedPanel.add(reward);
		jobPanel.add(jobListPanel);
		jobPanel.add(jobInProgPanel);
		jobPanel.add(jobFinishedPanel);
		robotStatus.add(commLabel);
		robotStatus.add(statusText);
		jobPanel.add(robotStatus);
		jobListPanel.setVisible(true);
		jobInProgPanel.setVisible(true);
		jobPanel.setVisible(true);

		// create split pane and add the map and job panels to it respectively
		JSplitPane split = new JSplitPane();
		split.setBackground(Color.white);
		split.setSize(1000, 600);
		split.setDividerLocation(500);
		split.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		split.setLeftComponent(mapVis);
		split.setRightComponent(jobPanel);

		// add split pane to frame
		this.add(split);
		this.setVisible(true);
	}
	
	// job list methods
	public void setJobList(String jobs) {
		String newline = "\n";
		String[] parts = jobs.split(" : ");
		for (int i = 0; i < parts.length; i++) {
			nextJobText.append(parts[i] + newline);
		}
	}

	public void addOneToJobList(String job) {
		String newline = "\n";
		nextJobText.append(job + newline);
	}

	public void emptyJobList() {
		nextJobText.setText("");
	}

	public String getJobList() {
		return nextJobText.getText();
	}

	// job in progress list methods
	public void setInProgList(String jobProgText){
		emptyProgList();
		String newline = "\n";
		String[] parts = jobProgText.split(" : ");
		for (int i = 0; i < parts.length; i++) {
			currentJobText.append(parts[i] + newline);
		}
	}

	public void emptyProgList() {
		currentJobText.setText("");
	}

	public String getInProgList() {
		return currentJobText.getText();
	}
	
	//finished Jobs
	public void setFinishedList(String jobsDone){
		fishedJobText.setText("");
		String newline = "\n";
		String[] parts = jobsDone.split(" : ");
		for (int i = 0; i < parts.length; i++) {
			fishedJobText.append(parts[i] + newline);
		}
	}
	
	public String getFinishedList(){
		return fishedJobText.getText();
	}
	
	//cancellation buttons
	 public void addCancelListener(ActionListener listen){
		 cancelUpcoming.addActionListener(listen);
		 cancelCurrent.addActionListener(listen);
	 }

	public void setStatusText(String robotInfo) {
		statusText.setText("");
		String newline = "\n";
		String[] parts = robotInfo.split(" : ");
		for (int i = 0; i < parts.length; i++) {
			statusText.append(parts[i] + newline);
		}
	}

	public void setReward(float addReward) {
		reward.setText(Float.toString(addReward));
	}
}