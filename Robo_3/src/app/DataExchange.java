package app;

public class DataExchange extends Thread { //t‰‰ kaikki on vaa varastettu sielt‰ moodlesta
	
	private boolean obstacle = false;
	private int CMD = 1;
	private int speed = 300;
	
	public DataExchange() {
	}
	
	public boolean getObstacle() {
		return obstacle;
	}

	public void setObstacle(boolean status) {
		this.obstacle = status;
	}

	public int getCMD() {
		return CMD;
	}

	public void setCMD(int command) {
		CMD = command;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
	public void run() {

	}

}
