package app;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.hardware.Button;

public class LineFollower extends Thread {

	private static EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S1);
	private static DataExchange de = new DataExchange();
	private static EV3LargeRegulatedMotor motor1 = new EV3LargeRegulatedMotor(MotorPort.A);
	private static EV3LargeRegulatedMotor motor2 = new EV3LargeRegulatedMotor(MotorPort.D);

	@Override
	public void run() {
		while (true) {
			if (de.getCMD() == 1) {
				
				final SampleProvider sp = cs.getRGBMode(); //sama ku ultrasonic sensorissa
				float[] sample = new float[sp.sampleSize()];
				sp.fetchSample(sample, 0);
				float color = (sample[0] * 10);
				
				System.out.println(color); // testi printtaus
				if (color > 0.19) { // jos on liian valoisaa, k‰‰nyt‰‰n vasemmalle
					motor1.setSpeed(5000);
					motor1.forward();
					motor2.setSpeed(50);
					motor2.forward();
				} else if (color < 0.09) { // jos on liian pime‰‰, k‰‰nyt‰‰n oikealle
					motor1.setSpeed(50);
					motor1.forward();
					motor2.setSpeed(500);
					motor2.forward();
				} else { // t‰ysill‰ eti‰p‰i
					motor1.setSpeed(500);
					motor1.forward();
					motor2.setSpeed(500);
					motor2.forward();
				}

				if (Button.getButtons() != 0) { // jos painaa nappia
					break;
				}
			} else {
				de.setCMD(0);
				System.exit(0);

			}
		}

	}
}
