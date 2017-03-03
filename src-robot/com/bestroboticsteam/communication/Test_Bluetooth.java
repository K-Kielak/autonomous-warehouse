package com.bestroboticsteam.communication;

import lejos.nxt.Button;
import lejos.nxt.ButtonListener;

public class Test_Bluetooth {

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

		RobotBluetoothHandler b = new RobotBluetoothHandler();
		(new Thread(b)).start();
		while (true) {
			// LCD.drawString(b.getStatus(), 0, 0);
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
