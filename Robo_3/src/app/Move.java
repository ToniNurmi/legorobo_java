package app;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;

public class Move extends Thread {

	DataExchange data;
	private static EV3LargeRegulatedMotor motor1;
	private static EV3LargeRegulatedMotor motor2;
	private static MovePilot pilot;

	public Move(DataExchange de) {

		data = new DataExchange();

		motor1 = new EV3LargeRegulatedMotor(MotorPort.A); // oikea rengas
		motor2 = new EV3LargeRegulatedMotor(MotorPort.D); // vasen rengas

		Wheel wheelRight = WheeledChassis.modelWheel(motor1, 56).offset(-52); // renkaan läpimitta ja etäisyys keskeltä
		Wheel wheelLeft = WheeledChassis.modelWheel(motor2, 56).offset(52);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheelRight, wheelLeft }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
	}

	public void moveFwd(float a, float b) {
		motor1.setSpeed(a);
		motor1.forward();
		motor2.setSpeed(b);
		motor2.forward();
	}
	
	public void moveBck() {
		motor1.setSpeed(200);
		motor2.setSpeed(200);
		motor1.backward();
		motor2.backward();
	}

	public void avoidObstacle() {
		int rotate = -100;
		pilot.rotate(rotate); // käänny oikealle (en tie mihin luku perustuu, ei ainakaan asteisiin)
		pilot.setLinearSpeed(250);
		data.setObstacle(true); // varmuudeks tämmöne ku joskus se näytti falsee tässä jostain syystä
		pilot.arc(360, 90); // radius, angle (matka eteenpäin, kuinka pitkään menee kaarta)
		pilot.rotate(0 - rotate / 1.5); // monimutkasta
	}
	
	public void rotate() {
		pilot.rotate(500);
	}
	
	public void stopMove() {
		motor1.stop();
		motor2.stop();
	}
}
