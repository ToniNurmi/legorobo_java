package app;

public class RunClass {
	
	private static DataExchange de;
	private static LineFollower lf;
	private static Ultra us;

	public static void main(String[] args) {
		
		de = new DataExchange();
		us = new Ultra(de);
		lf = new LineFollower(de);
		
		new Thread(us).start();
		new Thread(lf).start();
		new Thread(de).start();

	}

}
