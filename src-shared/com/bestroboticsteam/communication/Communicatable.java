package com.bestroboticsteam.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public interface Communicatable {
	public void sendObject(DataOutputStream o) throws IOException;

	public Object receiveObject(DataInputStream o) throws IOException;
}
