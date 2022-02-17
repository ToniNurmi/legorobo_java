package app;

import lejos.hardware.Button;
//import lejos.hardware.motor.EV3LargeRegulatedMotor;
//import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Ultra extends Thread {

	DataExchange data;
	private static EV3UltrasonicSensor us;

	public Ultra(DataExchange de) {

		data = de;
		us = new EV3UltrasonicSensor(SensorPort.S2);

	}

	@Override
	public void run() {
		final SampleProvider sp = us.getDistanceMode();
		int distance = 100; // alkuun vaa arvo, ei väliä
		int close = 30; // kuinka monen sentin päässä esine saa olla
		int kierros = 0;
		data.setObstacle(false);

		while (true) {
			if (data.getCMD() == 1) {
				if (distance > close) { // jos edessä ei oo mitään
					data.setObstacle(false);
					float[] sample = new float[sp.sampleSize()];
					sp.fetchSample(sample, 0);
					distance = (int) (sample[0] * 100);

					System.out.println("Distance: " + distance + "cm"); // näyttää näytöllä etäisyyden sentteinä
					Delay.msDelay(250);

					if (Button.getButtons() != 0) { // jos painaa nappia
						break;
					}
				} else { // jos havaitaan jotain
					data.setCMD(0);
					data.setObstacle(true);
					if (kierros < 2) {
						
						System.out.println("ESTE!!!"); // testi print
						//kierros += 1;
						data.setCMD(1);
						
					} else {
						// z
						data.setCMD(0);
						System.exit(0);
					}
				}

			}
		}

	}

}