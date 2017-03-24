package com.bestroboticsteam.robotexecution;

import lejos.nxt.Sound;

public class DontIgnoreTheRobot extends Thread {
	private static final int WAIT_TIME = 10; // Seconds
	
	@Override
	public void run() {
		try {
			Thread.sleep(WAIT_TIME * 1000);
			while(true) {
				Sound.beep();
				Thread.sleep(1 * 1000);
			}
		} catch (InterruptedException e) {
			return;
		}

		
	}
}
