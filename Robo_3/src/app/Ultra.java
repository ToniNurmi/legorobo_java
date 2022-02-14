package app;

import lejos.hardware.Button;
import lejos.hardware.lcd.LCD;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Ultra extends Thread {

	private static EV3UltrasonicSensor us = new EV3UltrasonicSensor(SensorPort.S2);
	private static DataExchange de = new DataExchange();

	@Override
	public void run() {

		final SampleProvider sp = us.getDistanceMode();
		int distance = 100; //alkuun vaa arvo, ei väliä
		int close = 25; //kuinka monen sentin päässä esin saa olla

		while (true) {
			if (distance > close) { 
				de.setCMD(1);
				float[] sample = new float[sp.sampleSize()];
				sp.fetchSample(sample, 0);
				distance = (int) (sample[0] * 100);

				System.out.println("Distance: " + distance + "cm"); //näyttää näytöllä etäisyyden sentteinä
				Delay.msDelay(250);
				LCD.clearDisplay(); //ei tee mitää jostain syystä

				if (Button.getButtons() != 0) { //jos painaa nappia
					break;
				}
			} else { //jos havaitaan jotain, tähän nyt pitäs tehä se väistö
				de.setCMD(0);
				System.exit(0);
			}
		}

	}

}