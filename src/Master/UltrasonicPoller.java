package Master;

import lejos.nxt.UltrasonicSensor;
import lejos.nxt.comm.*;
import javax.bluetooth.*;
import Support.Communicator;


public class UltrasonicPoller extends Thread{
	private UltrasonicSensor us;
	private SearchController cont;
	private Communicator comm;
	private Navigation nav;
	
	private boolean loop;
	
	public UltrasonicPoller(Navigation nav, UltrasonicSensor us, SearchController cont, Communicator comm) {
		this.us = us;
		this.cont = cont;
		this.comm = comm;
		this.nav = nav;
	}
	

	public void run() {
		
		loop = true;
		
		while (loop) {
			//process collected data
			boolean result = cont.search();
			
			if(result){
				nav.moveBy(10);
				comm.bluetoothSend("lift");
				loop = false;
			}
			
			try { Thread.sleep(20); } catch(Exception e){}
		}
	}

}
