package app;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

public class Ultra extends Thread {

	DataExchange data;
	private static EV3UltrasonicSensor us;
	private static Song song;

	public Ultra(DataExchange de) {

		data = de;
		us = new EV3UltrasonicSensor(SensorPort.S2);
		song = new Song(de);
	}

	@Override
	public void run() {
		
		final SampleProvider sp = us.getDistanceMode();
		int detect_pre = 25;
		int detect = 15; // kuinka monen sentin p‰‰ss‰ esine saa olla
		int kierros = 0;

		while (true) {
			if (data.getCMD() == 1) {
				
				us.enable();
				
				float[] sample = new float[sp.sampleSize()];
				sp.fetchSample(sample, 0);
				int distance = (int) (sample[0] * 100);

				if (distance > detect) { // jos edess‰ ei oo mit‰‰n
					
					//System.out.println("Distance: " + distance + "cm"); // n‰ytt‰‰ n‰ytˆll‰
					// et‰isyyden senttein‰
					// Delay.msDelay(100);
					
					System.out.println(""); // ilman t‰t‰ ultra pist‰‰ ittens‰ pois p‰‰lt‰, koska ?????????????????

					if (Button.getButtons() != 0) { // jos painaa nappia
						us.close();
						break;
					}
					if (distance < detect_pre) { // jos havaitaan jotain kaukana
						data.setSpeed(240); // SƒƒDETƒƒN NOPEUTTA

						if (Button.getButtons() != 0) { // jos painaa nappia
							us.close();
							break;
						}
					}
				} else { // jos havaitaan jotain
					//us.disable();
//						System.out.println("Kierros:" + kierros);
					if (kierros < 1) { // eka kierros

						sample = new float[sp.sampleSize()];
						sp.fetchSample(sample, 0);
						distance = (int) (sample[0] * 100); // etit‰‰n uus distance, ettei vanha j‰‰ kummittelemaan

						data.setObstacle(true); // ilmoitetaan linefollowerille
						song.obstacle();
						data.setCMD(1);
						Delay.msDelay(3000); // Ettei se n‰‰ uutta estett‰ v‰littˆm‰sti
						data.setSpeed(275); // SƒƒDETƒƒN NOPEUTTA
						kierros++;
					} else { // toka kierros
						data.setObstacle(true); // ilmoitetaan linefollowerille
						song.gameOver();
						us.close();
						System.exit(0); // sammutetaan saha
					}
				}
			}

		}
	}
}