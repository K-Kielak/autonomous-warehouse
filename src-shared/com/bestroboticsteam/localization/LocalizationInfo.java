package com.bestroboticsteam.localization;

import java.io.IOException;

import com.bestroboticsteam.communication.Communicatable;
import com.bestroboticsteam.communication.MyDataInputStream;
import com.bestroboticsteam.communication.MyDataOutputStream;
import com.bestroboticsteam.robotsmanagement.Direction;

public class LocalizationInfo implements Communicatable {
	private boolean foundPosition;
	private float[] sensorReadings;
	private Direction nextMove;
	
	public LocalizationInfo(){
		foundPosition = false;
		sensorReadings = new float[4];
		for(int i=0; i<4; i++){
			sensorReadings[i] = 0f;
		}
	}
	
	public void localized(){
		foundPosition = true;
	}
	
	public boolean wasPositionFound(){
		return foundPosition;
	}
	
	public void setSensorReadings(float[] readings){
		sensorReadings = readings;
	}
	
	public float getSensorReading(int i){
		return sensorReadings[i];
	}
	
	public Direction getNextMove(){
		return nextMove;
	}

	@Override
	public void sendObject(MyDataOutputStream o) throws IOException {
		o.writeBoolean(foundPosition);
		o.writeInt(this.sensorReadings.length);
		for (int i=0; i<sensorReadings.length; i++)
			o.writeFloat(sensorReadings[i]);
		
	}

	@Override
	public Object receiveObject(MyDataInputStream i) throws IOException {
		foundPosition = i.readBoolean();
		int directions = i.readInt();
		sensorReadings = new float[directions];
		for (int j = 0; j < directions; j++) 
			sensorReadings[j] = i.readFloat();
		
		return this;
	}
}
