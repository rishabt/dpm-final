package Master;

import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import javax.bluetooth.*;
import Support.*;

public class Master {
	
	public static void main(String[] args){
		Button.ESCAPE.addButtonListener(new ExitListener());
		
		LCD.drawString("MASTER", 0, 1);
		LCD.drawString("(press button)", 0, 3);
		
		Button.waitForAnyPress();
		LCD.clear();
		
		// INITIALIZE
		
		Bluetooth.setFriendlyName("master");
		
		LCD.drawString("connecting...", 0, 0);
		Communicator communicator = new Communicator(bluetoothConnect());
		LCD.clear();
		LCD.drawString("* bluetooth up", 0, 0);

		TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
		LCD.drawString("* robot ready", 0, 1);
		
		Odometer odo = new Odometer(robot, true);
		LCD.drawString("* odometer on", 0, 2);
		
		Navigation nav = new Navigation(odo, communicator);
		LCD.drawString("* nav ready", 0, 3);
				
		// LCDInfo lcd = new LCDInfo(odo);
		// LCD.drawString("* lcd info on", 0, 4);
		
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		LCD.drawString("* u.s. ready", 0, 5);
		
		LightSensor ls = new LightSensor(SensorPort.S3);
		LCD.drawString("* l.s. ready", 0, 6);
		
		ColorSensor cs = new ColorSensor(SensorPort.S2);
		 
		ObjectDetector objectDetector = new ObjectDetector(nav, us, cs);
		
		Grid grid = new Grid(30 * 8, 30 * 8);
		
		// LOCALIZE
		
		int option = Button.waitForAnyPress();
		
		if (option == Button.ID_LEFT) {
			USLocalizer localizer = new USLocalizer(odo, us);
			localizer.doLocalization();
		} else if(option == Button.ID_RIGHT) {
			boolean detected = objectDetector.detect(1);
			// LCD.clear();
			
			if (detected) {
				LCD.drawString("DETECTED", 0, 1);
			} else {
				LCD.drawString("FAIL", 0, 1);
			}
			
			/*
			nav.travelTo(60, 60);
			
			nav.travelTo(20, 80);
				
			nav.travelTo(60, 80);
				
			nav.travelTo(60, 100);
				
			nav.travelTo(90, 10);
				
			nav.travelTo(120, 90);
				
			nav.travelTo(120, 120);
				
			nav.travelTo(150, 150);
				
			nav.travelTo(170, 170);
				
			nav.travelTo(100, 60);
			*/
		}
		
		Button.waitForAnyPress();
	}
	
	public static BTConnection bluetoothConnect() {
		RemoteDevice slave = Bluetooth.getKnownDevice("slave");
		if (slave == null) {
			LCD.drawString("ERROR: device not found", 0, 1);
			Button.waitForAnyPress();
			System.exit(1);
		}
		return Bluetooth.connect(slave);
	}
	
}