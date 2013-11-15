package Master;

import lejos.nxt.*;
import lejos.nxt.Button;
import Support.Communicator;

public class Master {
	
	public static void main(String[] args){
		Button.ESCAPE.addButtonListener(new Support.ExitListener());
		
		LCD.drawString("MASTER", 0, 1);
		LCD.drawString("(press button)", 0, 3);
		
		Button.waitForAnyPress();
		LCD.clear();
		
		// INITIALIZE
		
		LCD.drawString("connecting...", 0, 0);
		Communicator communicator = new Communicator(bluetoothConnect());
		LCD.clear();
		LCD.drawString("* bluetooth up", 0, 0);

		TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
		LCD.drawString("* robot ready", 0, 1);
		
		Odometer odo = new Odometer(robot, true);
		LCD.drawString("* odometer on", 0, 2);
		
		Navigation nav = new Navigation(odo);
		LCD.drawString("* nav ready", 0, 3);
				
		LCDInfo lcd = new LCDInfo(odo);
		LCD.drawString("* lcd info on", 0, 4);
		
		Button.waitForAnyPress();
		
	}
	
	public static BTConnection bluetoothConnect() {
		RemoteDevice navigator = Bluetooth.getKnownDevice("navigator");
		if (navigator == null) {
			LCD.drawString("ERROR: device not found", 0, 1);
			Button.waitForAnyPress();
			System.exit(1);
		}
		return Bluetooth.connect(navigator);
	}
	
}