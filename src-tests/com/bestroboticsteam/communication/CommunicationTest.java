package com.bestroboticsteam.communication;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;

public class CommunicationTest {
	/*
	 * To Run these tests, the robot must be connected via USB, turned ON and
	 * running JUnitRobot.java program
	 * 
	 */

	@Test(expected = ConnectionNotEstablishedException.class)
	public void testConnectionNotEstablished() throws ConnectionNotEstablishedException {
		PCConnectionHandler p = new PCConnectionHandler("");
		p.disconnect();
	}

	@Test(expected = ConnectionNotEstablishedException.class)
	public void testcheckEstablished() throws ConnectionNotEstablishedException {
		PCConnectionHandler p = new PCConnectionHandler("");
		p.checkEstablished();
		p.disconnect();
	}

	// Status
	@Test()
	public void testcheckStatus() throws InterruptedException {
		PCConnectionHandler p = new PCConnectionHandler("");
		assertEquals(p.getStatus(), PCConnectionHandler.DISCONNECTED);

		Thread t = new Thread(p);
		t.start();
		Thread.sleep(100); // Wait a bit so the other thread can run
		// Probably not the best option
		assertEquals(p.getStatus(), PCConnectionHandler.CONNECTING);
		try {
			p.disconnect();
		} catch (ConnectionNotEstablishedException e) {
		}
	}

	@Test()
	public void testCorrectProtocol() {
		PCConnectionHandler p = new PCConnectionHandler("");
		assertEquals(p.protocol, PCConnectionHandler.BLUETOOTH_PROTOCOL);

		PCConnectionHandler bluetooth = new PCConnectionHandler("", true);
		assertEquals(bluetooth.protocol, PCConnectionHandler.BLUETOOTH_PROTOCOL);

		PCConnectionHandler usb = new PCConnectionHandler("", false);
		assertNotEquals(usb.protocol, PCConnectionHandler.BLUETOOTH_PROTOCOL);
		assertEquals(usb.protocol, PCConnectionHandler.USB_PROTOCOL);
		try {
			p.disconnect();
		} catch (ConnectionNotEstablishedException e) {
		}
		try {
			bluetooth.disconnect();
		} catch (ConnectionNotEstablishedException e) {
		}
		try {
			usb.disconnect();
		} catch (ConnectionNotEstablishedException e) {
		}

	}

	@Ignore()
	public void testSendReceiveDummyObjectUSB() throws InterruptedException {
		DummyObject d = new DummyObject();

		PCConnectionHandler usb = new PCConnectionHandler(RobotNames.ROBOT_1_NAME, false);
		usb.run(); // Make sure we are connected so we run in main thread.

		assertEquals(usb.getStatus(), BaseConnectionHandler.CONNECTED);

		for (int i = 0; i < 5; i++) {
			try {
				int a = d.getInfo();
				System.out.println(usb.getStatus());
				usb.sendObject(d);
				Thread.sleep(10);
				d = (DummyObject) usb.receiveObject(d);
				assertEquals(a, d.getInfo());

			} catch (ConnectionNotEstablishedException e) {
			}
		}
	}

	@Test()
	public void testSendReceiveDummyObjectBluetooth() throws InterruptedException {
		DummyObject d = new DummyObject();

		PCConnectionHandler bluetooth = new PCConnectionHandler(RobotNames.ROBOT_1_NAME, true);
		bluetooth.run(); // Make sure we are connected so we run in main thread.

		assertEquals(bluetooth.getStatus(), BaseConnectionHandler.CONNECTED);

		for (int i = 0; i < 5; i++) {
			try {
				int a = d.getInfo();
				System.out.println(bluetooth.getStatus());
				bluetooth.sendObject(d);
				Thread.sleep(10);
				d = (DummyObject) bluetooth.receiveObject(d);
				assertEquals(a, d.getInfo());

			} catch (ConnectionNotEstablishedException e) {
			}
		}
	}
}
