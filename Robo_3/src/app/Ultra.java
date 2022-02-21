package app;

import lejos.hardware.Button;
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
		int detect = 15; // kuinka monen sentin päässä esine saa olla
		int kierros = 0;

		while (true) {
			if (data.getCMD() == 1) {
				us.enable();
				float[] sample = new float[sp.sampleSize()];
				sp.fetchSample(sample, 0);
				int distance = (int) (sample[0] * 100);
				if (distance > detect) { // jos edessä ei oo mitään
					data.setObstacle(false);
					System.out.println("Distance: " + distance + "cm"); // näyttää näytöllä etäisyyden sentteinä
					Delay.msDelay(250);

					if (Button.getButtons() != 0) { // jos painaa nappia
						us.close();
						break;
					}
				} else { // jos havaitaan jotain
					us.disable();
					System.out.println("Kierros:" + kierros);
					data.setCMD(0);
					if (kierros < 1) {
						sample = new float[sp.sampleSize()];
						sp.fetchSample(sample, 0);
						distance = (int) (sample[0] * 100);
						obstacle();
						Delay.msDelay(3000);
						kierros++;
					} else {
						us.close();
						System.exit(0);
					}
				}
			}
		}
	}

	public void obstacle() {
		data.setObstacle(true);
		data.setCMD(1);
		Delay.msDelay(500);
	}

}