package Master;

import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import javax.bluetooth.*;
import Support.Communicator;

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
		
		Grid grid = new Grid(30 * 8, 30 * 8);
		Odometer odo = new Odometer(robot, grid);
		LCD.drawString("* odometer on", 0, 2);
		
		
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		LCD.drawString("* u.s. ready", 0, 5);
		
		Navigation nav = new Navigation(odo, us);
		LCD.drawString("* nav ready", 0, 3);
				
		LCDInfo lcd = new LCDInfo(odo);
		// LCD.drawString("* lcd info on", 0, 4);

		
		LightSensor ls = new LightSensor(SensorPort.S3);
		LCD.drawString("* l.s. ready", 0, 6);
		
		ColorSensor cs = new ColorSensor(SensorPort.S2);
		 
		ObjectDetector objectDetector = new ObjectDetector(nav, us, cs);
		
		Search search = new Search(nav, us, cs);
		
		UltrasonicPoller usPoller;
		
		// LOCALIZE
		
		int option = Button.waitForAnyPress();
		
		if (option == Button.ID_LEFT) {
			USLocalizer localizer = new USLocalizer(odo, us);
			localizer.doLocalization();
		} else if(option == Button.ID_RIGHT) {
			usPoller = new UltrasonicPoller(nav, us, search, communicator);
			usPoller.start();
			
//			nav.turnTo(90);
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