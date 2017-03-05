package com.bestroboticsteam.communication;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

public class RobotBluetoothHandler extends BaseBluetoothSocketHandler {

	private BTConnection bluetooth;;

	@Override
	public void run() {
		System.out.println("Waiting....");
		bluetooth = Bluetooth.waitForConnection();
		System.out.println("Connection Established");
		System.out.println(bluetooth);
		input = bluetooth.openDataInputStream();
		output = bluetooth.openDataOutputStream();
		connectionEstablished = true;
		// TODO Status
	}
}
