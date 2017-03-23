package com.bestroboticsteam.communication;

import java.awt.Point;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MyDataOutputStream extends DataOutputStream {
	/**
	 * Had to define new DataStreams so special objects can be send such as
	 * Strings and Points.
	 */

	public MyDataOutputStream(OutputStream out) {
		super(out);
	}

	public void writeString(String s) throws IOException {
		this.writeInt(s.length());
		this.writeBytes(s);
	}

	public void writePoint(Point p) throws IOException {
		if (p == null) {
			this.writeInt(0);
		} else {
			this.writeInt(1);
			this.writeInt(p.x);
			this.writeInt(p.y);
		}

	}

}
