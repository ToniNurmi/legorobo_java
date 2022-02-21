package app;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;


public class RunClass {

	private static DataExchange de;
	private static LineFollower lf;
	private static Ultra us;

	public static void main(String[] args) {

		Button.LEDPattern(7);
		LCD.drawString("  Press anything", 0, 3);
		LCD.drawString("   to continue!", 0, 4);
		Button.waitForAnyPress();
		Button.LEDPattern(0);
		LCD.clear();

		de = new DataExchange();
		us = new Ultra(de);
		lf = new LineFollower(de);

		new Thread(de).start();
		new Thread(us).start();
		new Thread(lf).start();

	}

}
