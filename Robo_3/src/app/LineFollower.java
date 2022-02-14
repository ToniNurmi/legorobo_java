package app;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;

public class LineFollower extends Thread {

	private static EV3ColorSensor cs = new EV3ColorSensor(SensorPort.S1);
	private static DataExchange de = new DataExchange();
	private static EV3LargeRegulatedMotor motor1 = new EV3LargeRegulatedMotor(MotorPort.A);
	private static EV3LargeRegulatedMotor motor2 = new EV3LargeRegulatedMotor(MotorPort.D);

	private int color = cs.getColorID();

	@Override
	public void run() {
		while (true) {
			if (de.getCMD() == 1) {
				if (color <= 1) { //se tunnistaa mustan jonain -1 kai
					System.out.println(color); //testi printtaus
					motor1.setSpeed(1000); //täysillä etiäpäi
					motor1.forward();
					motor2.setSpeed(1000);
					motor2.forward();
				} else {
					System.out.println("COLOR"); //jos ei näy mustaa nii pysähtyy kai, tätä pitää nyt muovata
					motor1.stop();
					motor2.stop();
				}
			} else {
				System.out.println(cs.getColorID()); //turha
				motor1.stop();
				motor2.stop();

			}
		}

	}
}
