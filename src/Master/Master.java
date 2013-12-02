package Master;

import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;
import javax.bluetooth.*;
import Support.Communicator;
import Master.*;

import Support.*;

/**
 * 
 * @author Rishabh
 *
 */
public class Master {
	
	public static PlayerRole role;
	public static int[] greenZone = new int[4];
	public static int[] redZone = new int[4];
	public static StartCorner corner;
	public static double[] initPosition = new double[3];
	
	/**
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception{
		Button.ESCAPE.addButtonListener(new ExitListener());
		
		Bluetooth.setFriendlyName("master");
		
		TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
		Grid grid = new Grid(30 * 12, 30 * 12);
		Odometer odo = new Odometer(robot, grid);
		
		LCD.drawString("MASTER", 0, 1);
		LCD.drawString("(press button)", 0, 3);
		
		Button.waitForAnyPress();
		LCD.clear();
		
		BluetoothConnection conn = new BluetoothConnection();
		Transmission t = conn.getTransmission();
		
		if (t == null) {
			LCD.drawString("Failed to read transmission", 0, 5);
		} else {
			corner = t.startingCorner;
			
			role = t.role;
			
			// green zone is defined by these (bottom-left and top-right) corners:
			greenZone = t.greenZone;
			
			// red zone is defined by these (bottom-left and top-right) corners:
			redZone = t.redZone;
			
			// print out the transmission information to the LCD
			conn.printTransmission();
		}
		
		if(corner == StartCorner.BOTTOM_RIGHT){
			initPosition[0] = 304.8 - lightlocalize.y;
			initPosition[1] = 0.0 + lightlocalize.x;
			initPosition[2] = 270;
			
		}
		
		else if(corner == StartCorner.TOP_LEFT){
			initPosition[0] = 0.0 + lightlocalize.y;
			initPosition[1] = 304.8 - lightlocalize.x;
			initPosition[2] = 90;
		}
		
		else if(corner == StartCorner.TOP_RIGHT){
			initPosition[0] = 304.8 - lightlocalize.y;
			initPosition[1] = 304.8 - lightlocalize.x;
			initPosition[2] = 180;
		}
		
		// INITIALIZE
		
		LCD.drawString("connecting...", 0, 0);
		Communicator communicator = new Communicator(bluetoothConnect());
		LCD.clear();
		LCD.drawString("* bluetooth up", 0, 0);
		
		UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
		LCD.drawString("* u.s. ready", 0, 5);
		
		Navigation nav = new Navigation(odo, us, communicator);
		LCD.drawString("* nav ready", 0, 3);
				
		LCDInfo lcd = new LCDInfo(odo);
		// LCD.drawString("* lcd info on", 0, 4);
		
		LightSensor ls = new LightSensor(SensorPort.S3);
		LightSensor ls2 = new LightSensor(SensorPort.S4);
		
		ColorSensor cs = new ColorSensor(SensorPort.S2);
		 
		ObjectDetector objectDetector = new ObjectDetector(nav, us, cs);
		
		Search search = new Search(nav, us, cs, PlayerRole.BUILDER);
		
		UltrasonicPoller usPoller;
		
		// LOCALIZE
	
			
			USLocalizer localizer = new USLocalizer(odo, us);
			lightlocalize lit = new lightlocalize(odo,ls,ls2);
			localizer.doLocalization();
			lit.dolitlocalize();
			Sound.beep();
			
			Thread.sleep(1000);
		
		
		us.ping();
		
		//Sound.beepSequence();
		
		nav.travelTo(odo.getX() + 30, odo.getY() + 30);
		
		
		if(role.getId() == 1){
			
			Builder play = new Builder(greenZone, redZone, us, cs, nav);
			
			play.build();
			
		}
		
		else{
			
			GarbageCollecter play = new GarbageCollecter(greenZone, redZone, us, cs, nav);
			
			play.collect();
			
		}
		
		
		Button.waitForAnyPress();
	}
	
	/**
	 * 
	 * @return
	 */
	
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