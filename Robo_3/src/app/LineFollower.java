package app;

import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;
import lejos.hardware.Button;

public class LineFollower extends Thread {

	DataExchange data;
	private static Move move;
	private static EV3ColorSensor cs;

	public LineFollower(DataExchange de) {

		data = de;
		cs = new EV3ColorSensor(SensorPort.S1);
		move = new Move(de);
	}

	@Override
	public void run() {
		while (true) {
			if (data.getCMD() == 1) {

				final SampleProvider sp = cs.getRedMode(); // sama ku ultrasonic sensorissa
				float[] sample = new float[sp.sampleSize()];
				sp.fetchSample(sample, 0);
				float color = (sample[0]);

				if (data.getObstacle() == true) {
					sample = new float[sp.sampleSize()];
					sp.fetchSample(sample, 0);
					color = (sample[0]);
					move.moveFwd(0, 0);
					Delay.msDelay(1000);
					System.out.println("Line follower obstacel: " + data.getObstacle());
					move.avoidObstacle();
					if (color < 0.07) {
						data.setObstacle(false);
					} else {
						System.out.println("valoisaa");
					}
				} else {
					System.out.println(color); // testi printtaus
					if (color > 0.09) { // jos on liian valoisaa, k‰‰nyt‰‰n vasemmalle
						move.moveFwd(350, 200);
					} else if (color < 0.07) { // jos on liian pime‰‰, k‰‰nyt‰‰n oikealle
						move.moveFwd(200, 350);
					} else { // t‰ysill‰ eti‰p‰i
						move.moveFwd(350, 350);
					}
				}

				if (Button.getButtons() != 0) { // jos painaa nappia
					cs.close();
					break;
				}
			} else {
				data.setCMD(0);

			}
		}

	}

}
