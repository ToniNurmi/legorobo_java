package app;

//import lejos.hardware.motor.EV3LargeRegulatedMotor;
//import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3ColorSensor;
import lejos.robotics.SampleProvider;
//import lejos.robotics.chassis.Chassis;
//import lejos.robotics.chassis.Wheel;
//import lejos.robotics.chassis.WheeledChassis;
//import lejos.robotics.navigation.MovePilot;
//import lejos.utility.Delay;
import lejos.hardware.Button;

public class LineFollower extends Thread {

	DataExchange data;
	private static Move move;
	private static EV3ColorSensor cs;
//	private static EV3LargeRegulatedMotor motor1;
//	private static EV3LargeRegulatedMotor motor2;
//	private static MovePilot pilot;

	public LineFollower(DataExchange de) {

		data = de;
		cs = new EV3ColorSensor(SensorPort.S1);
//		motor1 = new EV3LargeRegulatedMotor(MotorPort.A); // oikea rengas
//		motor2 = new EV3LargeRegulatedMotor(MotorPort.D); // vasen rengas
//		
//		Wheel wheelRight = WheeledChassis.modelWheel(motor1, 56).offset(-72);
//		Wheel wheelLeft = WheeledChassis.modelWheel(motor2, 56).offset(72);
//		Chassis chassis = new WheeledChassis(new Wheel[] { wheelRight, wheelLeft }, WheeledChassis.TYPE_DIFFERENTIAL);
//		pilot = new MovePilot(chassis);

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
					cs.setFloodlight(false); // pistet‰‰n valo pois p‰‰lt‰, ettei se ala ettim‰‰n mustaa
					move.avoidObstacle();
				} else {
					cs.setFloodlight(true);
				}

				System.out.println(color); // testi printtaus
				if (color > 0.09) { // jos on liian valoisaa, k‰‰nyt‰‰n vasemmalle
					move.left();
				} else if (color < 0.07) { // jos on liian pime‰‰, k‰‰nyt‰‰n oikealle
					move.right();
				} else { // t‰ysill‰ eti‰p‰i
					move.forward();
				}

				if (Button.getButtons() != 0) { // jos painaa nappia
					break;
				}
			} else {
				data.setCMD(0);
				// System.exit(0);

			}
		}

	}
	
}
