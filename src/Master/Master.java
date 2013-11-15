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
			UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
			LightSensor ls = new LightSensor(SensorPort.S3);
			
			if(Button.waitForAnyPress() == Button.ID_LEFT){
				
//				USLocalizer usl = new USLocalizer(odo, us, USLocalizer.LocalizationType.FALLING_EDGE);
//				usl.doLocalization();
				//try { Thread.sleep(2000); } catch (InterruptedException e) {}
				
				nav.travelTo(70, 60);
				
				nav.travelTo(20, 80);
				
				nav.travelTo(60, 80);
				
				nav.travelTo(60, 100);
				
				nav.travelTo(90, 10);
				
				nav.travelTo(120, 90);
				
				nav.travelTo(120, 120);
				
				nav.travelTo(150, 150);
				
				nav.travelTo(170, 170);
				
				nav.travelTo(100, 60);
				
				
			}
			
			
			
			if(Button.waitForAnyPress() == Button.ID_ESCAPE){
				System.exit(0);
			}
		
	}
	
}