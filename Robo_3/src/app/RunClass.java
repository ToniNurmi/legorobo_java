package app;

public class RunClass {

	public static void main(String[] args) {
		//Ultra us = new Ultra();
		LineFollower lf = new LineFollower();
		DataExchange de = new DataExchange();
		
		//new Thread(us).start();
		new Thread(lf).start();
		new Thread(de).start();

	}

}
