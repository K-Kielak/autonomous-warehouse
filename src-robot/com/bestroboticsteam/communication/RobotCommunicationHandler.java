package com.bestroboticsteam.communication;

import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.USB;

public class RobotCommunicationHandler extends BaseConnectionHandler {
	private boolean bluetooth;

	public RobotCommunicationHandler() {
		this(true);
	}

	public RobotCommunicationHandler(boolean bluetooth) {
		this.bluetooth = bluetooth;
	}

	@Override
	public void run() {
		this.status = CONNECTING;

		if (bluetooth) {
			this.connection = Bluetooth.waitForConnection();
		} else {
			this.connection = USB.waitForConnection();
		}
		// System.out.println("Connection" + this.connection);

		this.status = CONNECTED;

		input = new MyDataInputStream(this.connection.openDataInputStream());
		output = new MyDataOutputStream(this.connection.openDataOutputStream());
		System.out.println(input + " # " + output);
	}

}
