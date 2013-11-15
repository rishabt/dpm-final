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
		
		LCD.drawString("connecting...", 0, 0);
		
		Communicator communicator = new Communicator(bluetoothConnect());
		LCD.drawString("* bluetooth up", 0, 0);
		
		Button.waitForAnyPress();
	}

	public static BTConnection bluetoothConnect() {
		return Bluetooth.waitForConnection();
	}

}
