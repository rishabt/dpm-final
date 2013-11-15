package Master;

import lejos.nxt.*;

public class Search {
	
	TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	Odometer odo = new Odometer(robot, true);
	Navigation nav = new Navigation(odo);
	LCDInfo lcd = new LCDInfo(odo);
	UltrasonicSensor us;
	
	public Search(UltrasonicSensor us){
		this.us = us;
	}
	
	public void searchAlgorithm(){
		
		
	}

}
