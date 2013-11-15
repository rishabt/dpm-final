package Master;

import lejos.nxt.*;

public class ObjectDetector extends Thread{
	
	private static ColorSensor cs = new ColorSensor(SensorPort.S2);
	private static UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
	private TwoWheeledRobot robot;
	static Odometer odo;
	static Navigation nav;
	static int approximate = 7;
	
	public ObjectDetector(Odometer odo, Navigation nav, TwoWheeledRobot robot){
		this.odo = odo;
		this.nav = nav;		
		this.robot = robot;
	}
	
	public enum type { OBJECT, BLOCK }										//Enum gives the types of objects
	
	public static type TYPE;
	
	public static type detector() throws Exception{
		
		boolean detecting = true;
		
		int red,green,blue;
		
		Motor.A.setSpeed(100);												//Sets the forward speed of both left and right motors
		Motor.B.setSpeed(100);
		
		while(detecting){													//Runs a while loop
					
			//LCD.drawInt(us.getDistance(), 0, 2);
			Motor.A.forward();												//Moves forward
			Motor.B.forward();
			
			if(us.getDistance() <= 16){										//If the distance from the ultrasonic sensor is less than 16
				cs.setFloodlight(cs.BLUE);									//The floodlight is set to blue
				
				Motor.A.stop();												//The robot stops
				Motor.B.stop();
				
				Thread.sleep(500);											//Thread sleeps for 500 ms
				
				LCD.drawString(cs.getColor().getBlue() + "", 0 , 1);
				LCD.drawString(cs.getColor().getGreen() + "", 0 , 3);
				LCD.drawString(cs.getColor().getRed() + "", 0 , 5);
				
				red= cs.getColor().getRed();								//We get the red, green and blue colour and stores them in variables
				green= cs.getColor().getGreen();
				blue= cs.getColor().getBlue();
				
				if(Math.abs(red-blue) <= approximate){						//The following test the conditions for detection					
					LCD.drawString("Object", 0, 3);							//If the red - blue is within the approximate range then it is an object
					TYPE = type.OBJECT;
					Sound.beep();
					nav.moveBy(20);
					nav.travelTo(80, 190);
					detecting = false;
				}
				
				else{
					LCD.drawString("Block", 0, 3);							//Else a block
					TYPE = type.BLOCK;
					nav.moveBy(-10);
					detecting = false;
				}
				
				
				
			}
			
			
		}
		return TYPE;
		
	}

}
