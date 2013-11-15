package Slave;

import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import javax.bluetooth.*;
import Support.*;

public class Slave {

	public static void main(String[] args) {
		Button.ESCAPE.addButtonListener(new ExitListener());
		
		LCD.drawString("SLAVE", 11, 1);
		LCD.drawString("(press button)", 2, 3);
		
		Button.waitForAnyPress();
		LCD.clear();
		
		// INITIALIZE
		
		Bluetooth.setFriendlyName("slave");
		
		Clamp.lockMotors();
		
		LCD.drawString("connecting...", 0, 0);
		
		Communicator communicator = new Communicator(bluetoothConnect());
		LCD.drawString("* bluetooth up", 0, 0);
		
		String message = "";
		while (true) {
			message = communicator.bluetoothReceive();
			if (message.equals("lift")) {
				Clamp.lifting();
			} else if (message.equals("drop")) {
				Clamp.dropping();
			}
			if (message.length() == 0) message = "N/A";
			
			LCD.clear();
			LCD.drawString(message, 3, 3);
			try { Thread.sleep(250); } catch(Exception e) {}
		}
	}

	public static BTConnection bluetoothConnect() {
		return Bluetooth.waitForConnection();
	}

}
