package com.bestroboticsteam.communication;

import java.awt.Point;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MyDataOutputStream extends DataOutputStream {

	public MyDataOutputStream(OutputStream out) {
		super(out);
	}
	
	public void writeString(String s) throws IOException {
		this.writeInt(s.length());
		this.writeBytes(s);
	}
	
	public void writePoint(Point p) throws IOException {
		this.writeInt(p.x);
		this.writeInt(p.y);
	}

}
