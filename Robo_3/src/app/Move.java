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

		Wheel wheelRight = WheeledChassis.modelWheel(motor1, 56).offset(-52); // renkaan l‰pimitta ja et‰isyys keskelt‰
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

	public void avoidObstacle() {
		pilot.rotate(-120); // k‰‰nny oikealle (en tie mihin luku perustuu, ei ainakaan asteisiin)
		pilot.setLinearSpeed(165);
		data.setObstacle(true);
		pilot.arc(360, 90); // radius, angle (matka eteenp‰in, kuinka pitk‰‰n menee kaarta)
		pilot.rotate(110);
	}
}
