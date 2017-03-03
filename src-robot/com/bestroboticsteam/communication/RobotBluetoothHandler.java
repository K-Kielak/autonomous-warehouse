package com.bestroboticsteam.communication;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class RobotBluetoothHandler extends BaseBluetoothSocketHandler {

	private BTConnection bluetooth;;

	@Override
	public void run() {
		bluetooth = Bluetooth.waitForConnection();
		input = bluetooth.openDataInputStream();
		output = bluetooth.openDataOutputStream();
		connectionEstablished = true;
		// TODO Status
	}
}
