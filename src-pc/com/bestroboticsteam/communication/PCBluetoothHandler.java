package com.bestroboticsteam.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.apache.log4j.Logger;

import com.bestroboticsteam.communication.BaseBluetoothSocketHandler;

import lejos.pc.comm.NXTComm;
import lejos.pc.comm.NXTCommException;
import lejos.pc.comm.NXTCommFactory;
import lejos.pc.comm.NXTInfo;

public class PCBluetoothHandler extends BaseBluetoothSocketHandler implements Runnable {
	private String robotName;
	private String status;
	private NXTComm nxtComm;

	final static Logger logger = Logger.getLogger(PCBluetoothHandler.class);

	public PCBluetoothHandler(String robotName) {
		this.robotName = robotName;
		this.status = "";
	}

	@Override
	public void run() {
		logger.info("Attempting connection to: " + this.robotName);
		try {
			nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
			NXTInfo[] availableDevices = nxtComm.search(robotName);
			if (availableDevices.length > 1) {
				logger.warn("Got more than one possible device. Connecting to " + availableDevices[0].name);
			}
			boolean sucessful = nxtComm.open(availableDevices[0]);
			if (!sucessful) {
				logger.error("Error connecting to " + availableDevices[0].name);
			}
			input = new DataInputStream(nxtComm.getInputStream());
			output = new DataOutputStream(nxtComm.getOutputStream());

		} catch (NXTCommException e) {
			logger.error("Couldn't establish a connection");
		}
	}

	@Override
	public void sendObject(Communicatable obj) {
		// TODO Auto-generated method stub

	}

	public String getStatus() {
		return this.status;
	}

	@Override
	public void receiveObject(Communicatable obj) throws ConnectionNotEstablishedException {
		// TODO Auto-generated method stub
		
	}

}
