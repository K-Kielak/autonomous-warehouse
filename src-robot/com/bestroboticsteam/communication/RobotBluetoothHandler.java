package com.bestroboticsteam.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.bestroboticsteam.communication.Communicatable;

import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;

public class RobotBluetoothHandler extends BaseBluetoothSocketHandler {
	
	private BTConnection bluetooth;
	private DataInputStream input;
	private DataOutputStream output;
	private String status = "";
	
	private boolean connectionEstablished = false;

	@Override
	public void run() {
		status  = "Connecting";
		bluetooth = Bluetooth.waitForConnection();
		input = bluetooth.openDataInputStream();
        output = bluetooth.openDataOutputStream();
        connectionEstablished = true;
	}

	
	@Override
	public Communicatable receiveObject(Class<Communicatable> objClass) throws ConnectionNotEstablishedException {
		return null;	
	}

	
	public synchronized String getStatus() {
		return this.status;
	}
	
}	
	
	
	
	
	
	
	
	
	
	
	
	
	
//	private String deviceName;
//	
//
//	public RobotBluetoothHandler(String deviceName) {
//		this.deviceName = deviceName;
//	}
//	
//	public void connect() {
//		RemoteDevice connection = Bluetooth.getKnownDevice(deviceName);
//		if (connection == null){
//			logger.error("No Bluetooth device name: " + deviceName);
//			return;
//		}
//		logger.info("Connecting via Bluetooth to: " + deviceName);
//		BTConnection bt = Bluetooth.connect(connection);
//		
//		if (bt == null) {
//			logger.error("Error connecting to device: "+ deviceName);
//			return;
//		}
//	}
//	
//	@Override
//	public void run() {
//		
//	}
//	
//	public synchronized void sendObject(Communicatable obj) {
//		// Add to queue?
//	}
//	
//	public static String[] getAvailableDevices() {
//		return null;
////		NXTComm nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
////		nxtComm.
//	}
//}
