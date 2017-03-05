package com.bestroboticsteam.communication;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;
import lejos.nxt.LCD;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

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
		
		(new Thread(new RobotBluetoothHandler())).start();
		Button.waitForAnyPress();
	}
}
