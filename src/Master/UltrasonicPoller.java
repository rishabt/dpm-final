package Master;

import lejos.nxt.UltrasonicSensor;


public class UltrasonicPoller extends Thread{
	private UltrasonicSensor us;
	private SearchController cont;
	
	public UltrasonicPoller(UltrasonicSensor us, SearchController cont) {
		this.us = us;
		this.cont = cont;
	}
	
	public void run() {
		while (true) {
			//process collected data
			cont.search(us.getDistance());
			try { Thread.sleep(20); } catch(Exception e){}
		}
	}

}
