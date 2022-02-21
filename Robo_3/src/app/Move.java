package app;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.chassis.Chassis;
import lejos.robotics.chassis.Wheel;
import lejos.robotics.chassis.WheeledChassis;
import lejos.robotics.navigation.MovePilot;
import lejos.utility.Delay;

public class Move extends Thread {

	DataExchange data;
	private static EV3LargeRegulatedMotor motor1;
	private static EV3LargeRegulatedMotor motor2;
	private static MovePilot pilot;

	public Move(DataExchange de) {

		data = new DataExchange();

		motor1 = new EV3LargeRegulatedMotor(MotorPort.A); // oikea rengas
		motor2 = new EV3LargeRegulatedMotor(MotorPort.D); // vasen rengas

		Wheel wheelRight = WheeledChassis.modelWheel(motor1, 56).offset(-63); // renkaan l‰pimitta ja et‰isyys keskelt‰
		Wheel wheelLeft = WheeledChassis.modelWheel(motor2, 56).offset(63);
		Chassis chassis = new WheeledChassis(new Wheel[] { wheelRight, wheelLeft }, WheeledChassis.TYPE_DIFFERENTIAL);
		pilot = new MovePilot(chassis);
	}

	public void moveFwd(int a, int b) {
		motor1.setSpeed(a);
		motor1.forward();
		motor2.setSpeed(b);
		motor2.forward();
	}

	public void avoidObstacle() {
		pilot.rotate(-175); // k‰‰nny oikealle (en tie mihin luku perustuu, ei ainakaan asteisiin)
		pilot.setLinearSpeed(165);
		pilot.arc(345, 360, true); // radius, angle (matka, kulma)
		if (data.getObstacle() == false) {
			pilot.stop();
			Delay.msDelay(3000);
			pilot.rotate(-175);
		} else if (!pilot.isMoving()) {
			data.setObstacle(false);
		}
	}
}
