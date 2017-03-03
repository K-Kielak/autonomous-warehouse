package com.bestroboticsteam.communication;

import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface Communicatable {
	public void sendObject(DataOutputStream o);

	public Object receiveObject(DataInputStream o);
}
