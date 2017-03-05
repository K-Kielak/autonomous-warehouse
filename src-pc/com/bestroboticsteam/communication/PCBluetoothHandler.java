package com.bestroboticsteam.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import org.apache.log4j.Logger;

import com.bestroboticsteam.communication.BaseBluetoothSocketHandler;

import lejos.pc.comm.NXTCommLogListener;
import lejos.pc.comm.NXTConnector;

public class PCBluetoothHandler extends BaseBluetoothSocketHandler {
	private String robotName;

	final static Logger logger = Logger.getLogger(PCBluetoothHandler.class);

	public PCBluetoothHandler(String robotName) {
		this.robotName = robotName;
	}

	@Override
	public void run() {
		logger.info("Attempting connection to: " + this.robotName);

		NXTConnector conn = new NXTConnector();

		conn.addLogListener(new NXTCommLogListener() {

			public void logEvent(String message) {
				logger.debug("Bluetooth Log Event: " + message);
			}

			public void logEvent(Throwable throwable) {
				logger.debug("Bluetooth Throwable: ", throwable);
			}

		});

		boolean sucessful = conn.connectTo("btspp://" + this.robotName);

		if (!sucessful) {
			logger.error("Error connecting via bluetooth");
			return; // TODO Retry
		}
		input = new DataInputStream(conn.getInputStream());
		output = new DataOutputStream(conn.getOutputStream());

	}

}
