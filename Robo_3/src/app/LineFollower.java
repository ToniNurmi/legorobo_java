package app;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
import lejos.robotics.navigation.DifferentialPilot;
//import lejos.utility.Delay;
import lejos.hardware.Button;

public class LineFollower extends Thread {

	DataExchange data;
	private static EV3ColorSensor cs;
	private static EV3LargeRegulatedMotor motor1;
	private static EV3LargeRegulatedMotor motor2;
	private static DifferentialPilot pilot;
	
	public LineFollower(DataExchange de) {
		
		data = de;
		cs = new EV3ColorSensor(SensorPort.S1);
		motor1 = new EV3LargeRegulatedMotor(MotorPort.A); // oikea rengas
		motor2 = new EV3LargeRegulatedMotor(MotorPort.D); // vasen rengas
		pilot = new DifferentialPilot(2.2, 6.2, motor1, motor2, true);
		
	}

	@Override
	public void run() {
		while (true) {
			if (data.getCMD() == 1) {
				
				final SampleProvider sp = cs.getRedMode(); //sama ku ultrasonic sensorissa
				float[] sample = new float[sp.sampleSize()];
				sp.fetchSample(sample, 0);
				float color = (sample[0]);
				
				if(data.getObstacle() == true) {
					
					cs.setFloodlight(false);
					pilot.arcForward(30);
					
				} else {
					cs.setFloodlight(true);
				}
				
				System.out.println(color); // testi printtaus
				if (color > 0.09) { // jos on liian valoisaa, k‰‰nyt‰‰n vasemmalle
					motor1.setSpeed(350); // ISOMPI
					motor1.forward();
					motor2.setSpeed(150); // PIENEMPI
					motor2.forward();
				} else if (color < 0.07) { // jos on liian pime‰‰, k‰‰nyt‰‰n oikealle
					motor1.setSpeed(150); // PIENEMPI
					motor1.forward();
					motor2.setSpeed(350); // ISOMPI
					motor2.forward();
				} else { // t‰ysill‰ eti‰p‰i
					motor1.setSpeed(350);
					motor1.forward();
					motor2.setSpeed(350);
					motor2.forward();
				}

				if (Button.getButtons() != 0) { // jos painaa nappia
					break;
				}
			} else {
				data.setCMD(0);
				//System.exit(0);

			}
		}

	}
}
