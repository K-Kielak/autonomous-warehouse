package com.bestroboticsteam.communication;

import static com.bestroboticsteam.communication.MacAddresses.*;

public class Test_Bluetooth {

	public static void main(String[] args) {
		PCBluetoothHandler b = new PCBluetoothHandler(ROBOT_1_NAME);

		(new Thread(b)).start(); // Connects

		for (int i = 0; i < 100; i++) {
		}
	}
}
