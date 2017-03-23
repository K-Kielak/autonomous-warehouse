package com.bestroboticsteam.communication;

import java.io.IOException;

public class DummyObject implements Communicatable {
	/**
	 * 
	 * Used in the tests to test sending and receiving and to illustrate the use
	 * of sendObject and receiveObject
	 * 
	 */
	private int num;

	public DummyObject() {
		this.num = 5;
	}

	@Override
	public void sendObject(MyDataOutputStream o) throws IOException {
		System.out.println("Writing: " + this.num);
		o.writeInt(this.num);
	}

	@Override
	public Object receiveObject(MyDataInputStream o) throws IOException {

		System.out.println("Reading");
		this.num = o.readInt();
		System.out.println("Read: " + this.num);
		return this;
	}

	public int getInfo() {
		return this.num;
	}

}
