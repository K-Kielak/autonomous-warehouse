package com.bestroboticsteam.warehouseinterface;

import com.bestroboticsteam.job.*;

public class Controller {
	private View theView;
	private JobSelection theModel;
	
	public Controller(View theView, JobSelection theModel){
		this.theView = theView;
		this.theModel = theModel;
	}
	
	public void getTenJobs(){
		for (int i = 0; i < 10; i++){
			theView.displayText = theView.displayText + "/n " + i + "/n" + theModel.viewOrder(i);
			theView.list.setText(theView.displayText);
		}
	}
	
	

}