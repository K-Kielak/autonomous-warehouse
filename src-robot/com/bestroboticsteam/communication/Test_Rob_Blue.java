package com.bestroboticsteam.communication;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

/*
 * Example on how to start a connection on a robot
 */

public class Test_Rob_Blue {

	public static void main(String[] args) {

		Button.ESCAPE.addButtonListener(new ButtonListener() {

			@Override
			public void buttonReleased(Button b) {
			}

			@Override
			public void buttonPressed(Button b) {
				System.exit(0);
			}
		});
		
		(new Thread(new RobotCommunicationHandler())).start();
		Button.waitForAnyPress();
	}
}
