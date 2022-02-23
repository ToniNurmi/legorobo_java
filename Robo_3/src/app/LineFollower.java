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
		int round = 0;
		while (true) {
			if (data.getCMD() == 1) {

				final SampleProvider sp = cs.getRedMode(); // sama ku ultrasonic sensorissa
				float[] sample = new float[sp.sampleSize()];
				sp.fetchSample(sample, 0);
				float color = (sample[0]);

				if (data.getSpeed() > 200) { // normi nopeus (ei mit‰‰n edess‰)
					
//					System.out.println(color); // testi printtaus
					
					if (color > 0.09) { // jos on liian valoisaa, k‰‰nyt‰‰n vasemmalle
						move.moveFwd(data.getSpeed(), data.getSpeed() - (color * 1000));
					} else if (color < 0.07) { // jos on liian pime‰‰, k‰‰nyt‰‰n oikealle
						move.moveFwd(190 + (color * 1000), data.getSpeed());
					} else { // t‰ysill‰ eti‰p‰i
						move.moveFwd(data.getSpeed(), data.getSpeed());
					}
				} else {
					if (data.getObstacle() == true) { // kun 15cm et‰isyydell‰ on jotain
						if (round == 0) { // kun este n‰hd‰‰n ekan kerran
							
							sample = new float[sp.sampleSize()];
							sp.fetchSample(sample, 0);
							color = (sample[0]);
							
							move.stopMove(); // hetkellinen pys‰ytys jotta voi mietti‰ el‰m‰ns‰ valintoja
							Delay.msDelay(150);
							move.avoidObstacle(); // aloitetaan kierto-protokolla
							boolean searchBlack = true;
							boolean rotate = true;
							while (searchBlack == true) {
								
								sample = new float[sp.sampleSize()];
								sp.fetchSample(sample, 0);
								color = (sample[0]);
								
								if (color < 0.04) {
									rotate = false;
									move.moveFwd(0, 175); // k‰‰nnyt‰‰n jatkamaan viivaa pitkin
								} else if (color > 0.09 && rotate == true) {
									move.moveFwd(175, 225); // etsit‰‰n musta viiva hitaasti, mutta varmasti hieman kaartaen
								} else if (color > 0.07 && rotate == false) {
									move.stopMove();
									Delay.msDelay(100);
									break; // siirryt‰‰n seuraavaan osioon eli riville 72
								}
							}
							searchBlack = false; // ei etit‰ en‰‰ mustaa viivaa "suoralla" linjalla
							data.setObstacle(false); // palataan normiin
							round++;
						} else { // tokan kierroksen este
							move.stopMove();
							Delay.msDelay(500);
							move.moveBck();
							Delay.msDelay(500);
							move.stopMove();
							move.rotate();
							Delay.msDelay(3000);
							cs.close();
							break;
						}

					} else { // kun nopeus on 200 (este 25 cm et‰isyydell‰)
						if (color > 0.09) { // jos on liian valoisaa, k‰‰nyt‰‰n vasemmalle
							move.moveFwd(data.getSpeed(), data.getSpeed() - (color * 1000));
						} else if (color < 0.07) { // jos on liian pime‰‰, k‰‰nyt‰‰n oikealle
							move.moveFwd(90 + (color * 1000), data.getSpeed());
						} else { // t‰ysill‰ eti‰p‰i
							move.moveFwd(data.getSpeed(), data.getSpeed());
						}

					}

					if (Button.getButtons() != 0) { // jos painaa nappia
						cs.close();
						break;
					}

				}
			} else {
				data.setCMD(0);

			}
		}

	}

}
