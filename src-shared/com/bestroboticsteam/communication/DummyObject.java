package com.bestroboticsteam.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class DummyObject implements Communicatable {
	private int num;

	public DummyObject() {
		this.num = 5;
	}

	@Override
	public void sendObject(DataOutputStream o) throws IOException {
		System.out.println("Writing: " + this.num);
		o.writeInt(this.num);
	}

	@Override
	public Object receiveObject(DataInputStream o) throws IOException {

		System.out.println("Reading");
		this.num = o.readInt();
		System.out.println("Read: " + this.num);
		return this;
	}

	public int getInfo() {
		return this.num;
	}

}