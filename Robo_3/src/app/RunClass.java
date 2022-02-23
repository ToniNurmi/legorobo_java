package app;

import lejos.hardware.Button;
import lejos.hardware.lcd.GraphicsLCD;
import lejos.hardware.lcd.LCD;

public class RunClass {

	private static DataExchange de;
	private static LineFollower lf;
	private static Ultra us;
	private static Song song;
	private static Screen lcd;
	
	public static void main(String[] args) {

		Button.LEDPattern(7);
		LCD.drawString("Press anything", GraphicsLCD.VCENTER, 3);
		LCD.drawString(" to continue!",  GraphicsLCD.VCENTER, 4);
		Button.waitForAnyPress();
		Button.LEDPattern(0);
		LCD.clear();
		
		de = new DataExchange();
		
		lcd = new Screen(de);
		new Thread(lcd).start();
		lcd.smile();
		
		song = new Song(de);
		us = new Ultra(de);
		lf = new LineFollower(de);
		
		new Thread(lf).start();
		new Thread(us).start();
		new Thread(song).start();
		new Thread(de).start();

	}

}
