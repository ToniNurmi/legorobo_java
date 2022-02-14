package app;

public class RunClass {

	public static void main(String[] args) {
		//Move move = new Move();
		Ultra us = new Ultra();
		LineFollower lf = new LineFollower();
		DataExchange de = new DataExchange();
		
		//new Thread(move).start();
		new Thread(us).start();
		new Thread(lf).start();
		new Thread(de).start();

	}

}
