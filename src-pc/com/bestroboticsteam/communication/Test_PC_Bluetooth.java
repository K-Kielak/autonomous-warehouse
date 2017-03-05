package com.bestroboticsteam.communication;

import static com.bestroboticsteam.communication.MacAddresses.*;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTConnector;

public class Test_PC_Bluetooth {

	public static void main(String[] args) {
		System.out.println("Connecting...");
		 (new PCBluetoothHandler(ROBOT_1_NAME)).run();
	}
}
