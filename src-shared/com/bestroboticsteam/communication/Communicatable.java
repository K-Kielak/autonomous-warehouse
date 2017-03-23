package com.bestroboticsteam.communication;

import java.io.IOException;

public interface Communicatable {
	/*
	 * 
	 * All object that need to be send must extend this interface.
	 * 
	 */
	public void sendObject(MyDataOutputStream o) throws IOException;

	public Object receiveObject(MyDataInputStream i) throws IOException;
}
