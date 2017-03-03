package com.bestroboticsteam.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import lejos.nxt.comm.NXTConnection;

public abstract class BaseBluetoothSocketHandler implements Runnable {
	protected boolean connectedEstabilsied = false;
	
	protected NXTConnection bluetooth;
	protected DataInputStream input;
	protected DataOutputStream output;
	
	public abstract void receiveObject(Communicatable obj) throws ConnectionNotEstablishedException;
	
	public void disconnect() throws ConnectionNotEstablishedException{
		this.checkEstablished();
		
		try {
			input.close();
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		bluetooth.close();
		status = "Disconnected";
	}

	@Override
	public void sendObject(Communicatable obj) throws ConnectionNotEstablishedException{
		checkEstablished();
		obj.sendObject(this.output);
	}
	
	
	
	private void checkEstablished() throws ConnectionNotEstablishedException {
		if (!connectionEstablished) {
			throw new ConnectionNotEstablishedException();
		}
	}
	
	
}
