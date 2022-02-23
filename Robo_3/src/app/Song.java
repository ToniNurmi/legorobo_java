package app;

import lejos.hardware.Sound;
import lejos.utility.Delay;

public class Song extends Thread {

	public Song(DataExchange de) { 
	}
	
	public void obstacle() {
		Sound.playTone(932, 75, 100);
		Sound.playTone(1319, 200, 100);
	}
	
	public void gameOver() {
		Sound.playTone(659, 150, 100);
		Sound.playTone(659, 150, 100);
		Sound.playTone(659, 150, 100);
		Sound.playTone(659, 450, 100);
		Sound.playTone(523, 450, 100);
		Sound.playTone(587, 450, 100);
		Sound.playTone(659, 150, 100);
		Delay.msDelay(150);
		Sound.playTone(587, 150, 100);
		Sound.playTone(659, 450, 100);
		Delay.msDelay(1500);
	}

}
