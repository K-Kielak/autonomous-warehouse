package com.bestroboticsteam.communication;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class MyDataInputStream extends DataInputStream {
	/**
	 * Had to define new DataStreams so special objects can be received such as
	 * Strings and Points.
	 */
	
	private int attempt = 0;

	public MyDataInputStream(InputStream in) {
		super(in);
	}

	public String readString() throws IOException {
		try {
			int stringLength = this.readInt();
			byte[] string = new byte[stringLength];
			this.read(string);
			return new String(string);
		} catch (EOFException e) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
			}
//			attempt++
//			if (attempt > 200) {
//				throw new E
//			}
			return readString();
		}

	}

	public Point readPoint() throws IOException {
		try {
			boolean not_null = this.readInt() == 1;
			if (not_null) {
				return new Point(this.readInt(), this.readInt());
			} else {
				return null;
			}

		} catch (EOFException e) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
			}
			return readPoint();
		}
	}
}
