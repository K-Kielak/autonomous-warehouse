package com.bestroboticsteam.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.comm.NXTConnection;

public abstract class BaseBluetoothSocketHandler implements Runnable {
	protected boolean connectionEstablished = false;

	protected NXTConnection bluetooth;
	protected DataInputStream input;
	protected DataOutputStream output;

	public void disconnect() throws ConnectionNotEstablishedException {
		this.checkEstablished();

		try {
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bluetooth.close();
		// TODO Status
	}

	public void sendObject(Communicatable obj) throws ConnectionNotEstablishedException {
		checkEstablished();
		obj.sendObject(this.output);
	}

	public void receiveObject(Communicatable obj) throws ConnectionNotEstablishedException {
		obj.receiveObject(this.input);

	}

	private void checkEstablished() throws ConnectionNotEstablishedException {
		if (!connectionEstablished) {
			throw new ConnectionNotEstablishedException();
		}
	}

	// public String getStatus() {
	// return this.status; // TODO
	// }
}
