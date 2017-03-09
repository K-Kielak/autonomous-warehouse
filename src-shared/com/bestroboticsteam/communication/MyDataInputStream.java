package com.bestroboticsteam.communication;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MyDataInputStream extends DataInputStream{
	
	public MyDataInputStream(InputStream in) {
		super(in);
	}

	public String readString() throws IOException {
		int stringLength = this.readInt();
		byte[] string = new byte[stringLength];
		this.read(string);
		return new String(string);
	}
	
	public Point readPoint() throws IOException {
		return new Point(this.readInt(), this.readInt());
	}

}
