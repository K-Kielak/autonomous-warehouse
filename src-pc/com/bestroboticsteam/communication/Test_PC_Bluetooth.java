package com.bestroboticsteam.communication;

import static com.bestroboticsteam.communication.RobotNames.*;

public class Test_PC_Bluetooth {
	/*
	 * Example on how to create a connection to a robot
	 */
	public static void main(String[] args) {
		System.out.println("Connecting...");
		(new PCConnectionHandler(ROBOT_1_NAME)).run();
	}
}
