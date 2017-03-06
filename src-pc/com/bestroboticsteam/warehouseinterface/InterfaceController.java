package com.bestroboticsteam.warehouseinterface;

import com.bestroboticsteam.job.*;

public class InterfaceController extends Thread {
	private InterfaceView theView;
	private JobSelection theModel;

	public InterfaceController(JobSelection theModel) {
		this.theView = new InterfaceView();
		this.theModel = theModel;
	}

	public void getTenJobs() {
		//get input for jobsList
		//get the first ten jobs from JobSelection and output them to displayText in IView 
		for (int i = 0; i < 10; i++) {
			theView.displayText = theView.displayText + "/n " + i + "/n" + theModel.viewOrder(i);
			theView.list.setText(theView.displayText);
		}
	}

	public void run() {
		while (true) {
			//while running keep updating jobs - sleep for 1s between updates
			getTenJobs();
			InterfaceController.sleep(1000);
		}
	}

	public static void main(String[] args) {
		//I don't think this will work
		InterfaceController test = new InterfaceController(JobSelection);
		test.run();
	}

}