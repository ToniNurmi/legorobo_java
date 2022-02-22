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
					move.avoidObstacle(); // aloitetaan kierto-protokolla 
					boolean searchBlack = true;
					boolean rotate = true;
					while (searchBlack == true) {
						sample = new float[sp.sampleSize()];
						sp.fetchSample(sample, 0);
						color = (sample[0]);
						if (color < 0.04) { 
							rotate = false;
							System.out.println("a: " + color); // testi printtaus
							move.moveFwd(0, 175); // k‰‰nnyt‰‰n jatkamaan viivaa pitkin
						} else if (color > 0.09 && rotate == true) {
							System.out.println("b: " + color); // testi printtaus
							move.moveFwd(200, 200); //etsit‰‰n musta viiva hitaasti, mutta varmasti
						} else if (color > 0.07 && rotate == false) {
							System.out.println("c: " + color); // testi printtaus
							move.moveFwd(0, 0); // hetkellinen pys‰ytys jotta voi mietti‰ el‰m‰ns‰ valintoja
							Delay.msDelay(250);
							break;
						}
					}
					searchBlack = false;
					data.setObstacle(false); // palataan normiin

				} else {
					System.out.println(color); // testi printtaus
					if (color > 0.09) { // jos on liian valoisaa, k‰‰nyt‰‰n vasemmalle
						move.moveFwd(350, (color * 1000) + 100);
					} else if (color < 0.07) { // jos on liian pime‰‰, k‰‰nyt‰‰n oikealle
						move.moveFwd((color * 1000) + 200, 350);
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
