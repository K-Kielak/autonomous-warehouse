package com.bestroboticsteam.communication;

import java.io.IOException;

public interface Communicatable {
	public void sendObject(MyDataOutputStream o) throws IOException;

	public Object receiveObject(MyDataInputStream i) throws IOException;
}
