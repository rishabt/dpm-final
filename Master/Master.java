package Master;

import lejos.nxt.*;

import lejos.nxt.Button;

public class Master{
	
	public static void main(String[] args){
		
//			 setup the odometer, display, and ultrasonic and light sensors
			TwoWheeledRobot patBot = new TwoWheeledRobot(Motor.A, Motor.B);
			Odometer odo = new Odometer(patBot, true);
			Navigation nav = new Navigation(odo);
			LCDInfo lcd = new LCDInfo(odo);
			
			if(Button.waitForAnyPress() == Button.ID_LEFT){
				nav.travelTo(30, 30);
				
				nav.travelTo(60, 30);
				
				nav.travelTo(0, 0);
			}
		
	}
	
}