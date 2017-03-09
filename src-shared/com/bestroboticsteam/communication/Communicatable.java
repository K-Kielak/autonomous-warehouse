package com.bestroboticsteam.communication;

import java.awt.Point;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Communicatable {
	public void sendObject(DataOutputStream o) throws IOException;

	public Object receiveObject(DataInputStream i) throws IOException;

	// String
	default String readString(DataInputStream i) throws IOException {
		int stringLength = i.readInt();
		byte[] string = new byte[stringLength];
		i.read(string);
		return new String(string);
	}

	default void writeString(DataOutputStream o, String s) throws IOException {
		o.writeInt(s.length());
		o.writeBytes(s);
	}

	// Point
	default Point readPoint(DataInputStream i) throws IOException {
		return new Point(i.readInt(), i.readInt());
	}

	default void writePoint(DataOutputStream o, Point p) throws IOException {
		o.writeInt(p.x);
		o.writeInt(p.y);
	}
}
