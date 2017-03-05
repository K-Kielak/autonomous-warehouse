package com.bestroboticsteam.communication;

import lejos.nxt.Button;
import rp.systems.RobotProgrammingDemo;
import rp.util.Rate;

import static com.bestroboticsteam.communication.BaseConnectionHandler.CONNECTED;

public class JUnitRobot extends RobotProgrammingDemo {
	private BaseConnectionHandler usb;
	private BaseConnectionHandler bluetooth;

	public JUnitRobot() {
		usb = new RobotCommunicationHandler(false); // We need to out false if we want to communicate via USB
		bluetooth = new RobotCommunicationHandler();
	}

	@Override
	public void run() {
		(new Thread(usb)).start();
		(new Thread(bluetooth)).start();

		DummyObject d = new DummyObject();

		Rate r = new Rate(10);

		while (m_run) {
			if (usb.getStatus().equals(CONNECTED)) {
				try {
					usb.receiveObject(d); // Fills d with object contents
					usb.sendObject(d);
				} catch (ConnectionNotEstablishedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			if (bluetooth.getStatus().equals(CONNECTED)) {
				try {
					bluetooth.receiveObject(d);
					bluetooth.sendObject(d);
				} catch (ConnectionNotEstablishedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			r.sleep();
		}
		try {
			usb.disconnect();
			bluetooth.disconnect();
		} catch (ConnectionNotEstablishedException e) {
			
		}
	}

	public static void main(String[] args) {
		(new JUnitRobot()).run();
		Button.waitForAnyPress();
	}

}
