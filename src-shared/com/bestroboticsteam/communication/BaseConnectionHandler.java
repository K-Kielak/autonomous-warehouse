package com.bestroboticsteam.communication;

import java.io.IOException;

import lejos.nxt.comm.NXTConnection;

public abstract class BaseConnectionHandler implements Runnable {

	protected NXTConnection connection = null;
	protected String protocol;

	public static String BLUETOOTH_PROTOCOL = "btspp://";
	public static String USB_PROTOCOL = "usb://";

	protected MyDataInputStream input;
	protected MyDataOutputStream output;

	protected String status = DISCONNECTED;

	public static final String CONNECTING = "Connecting";
	public static final String DISCONNECTED = "Disconnected";
	public static final String CONNECTED = "Connected";
	public static final String RETRYING = "Retrying";

	public void disconnect() throws ConnectionNotEstablishedException {
		this.checkEstablished();

		try {
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		connection.close();

		input = null;
		output = null;
		connection = null;

		this.status = BaseConnectionHandler.DISCONNECTED;
	}

	public void sendObject(Communicatable obj) throws ConnectionNotEstablishedException {
		checkEstablished();
		try {
			obj.sendObject(this.output);
			this.output.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Communicatable receiveObject(Communicatable obj) throws ConnectionNotEstablishedException {
		checkEstablished();
		try {
			return (Communicatable) obj.receiveObject(this.input);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void checkEstablished() throws ConnectionNotEstablishedException {
		if (!this.status.equals(BaseConnectionHandler.CONNECTED)) {
			throw new ConnectionNotEstablishedException();
		}
	}

	public String getStatus() {
		return this.status;
	}

}
