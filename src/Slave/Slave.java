package Slave;

import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import javax.bluetooth.*;
import Support.Communicator;

public class Slave {

	public static void main(String[] args) {
		LCD.drawString("SLAVE", 11, 1);
		LCD.drawString("(press button)", 2, 3);
		
		Button.waitForAnyPress();
		LCD.clear();
		
		// INITIALIZE
		
		LCD.drawString("connecting...", 0, 0);
		
		Communicator communicator = new Communicator(bluetoothConnect());
		LCD.drawString("* bluetooth up", 0, 0);
	}


	public static BTConnection bluetoothConnect() {
		return Bluetooth.waitForConnection();
	}

}
