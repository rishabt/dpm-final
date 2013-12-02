package Support;

import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import javax.bluetooth.*;

/**
 * 
 * @author Rishabh
 *
 */
public class Communicator {
	protected BTConnection connection;
	protected DataInputStream inStream;
	protected DataOutputStream outStream;
	
	/**
	 * 
	 * @param connection
	 */
	public Communicator(BTConnection connection) {
		LCD.drawString("connecting...", 0, 1);
		if (connection == null) {
			LCD.clear();
			LCD.drawString("ERROR: connection failed", 0, 1);
			Button.waitForAnyPress();
			System.exit(1);
		}
		this.connection = connection;
		this.inStream = connection.openDataInputStream();
		this.outStream = connection.openDataOutputStream();
		LCD.clear();
	}
	
	/**
	 * 
	 */
	public void bluetoothClose() {
		try {
			inStream.close();
			outStream.close();
			connection.close();
		} catch (IOException e) {}
	}
	
	/**
	 * 
	 * @param message
	 */
	public void bluetoothSend(String message) {
		try {
			outStream.writeUTF(message);
			outStream.flush();
		} catch (IOException e) {
			LCD.clear();
			LCD.drawString("ERROR", 0, 0);
			LCD.drawString("msg not sent", 0, 1);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public String bluetoothReceive() {
		String message = "";
		try { message = inStream.readUTF(); } catch (IOException e) {
			LCD.drawString("ERROR", 0, 0);
			LCD.drawString("no msg", 0, 1);
		}
		return message;
	}
}