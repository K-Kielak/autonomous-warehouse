package com.bestroboticsteam.communication;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public class MyDataInputStream extends DataInputStream{
	
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
			return readString();
		}

	}
	
	public Point readPoint() throws IOException {
		try {
			return new Point(this.readInt(), this.readInt());		
		} catch (EOFException e) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e1) {
			}
			return readPoint();
		}
	}	
}
