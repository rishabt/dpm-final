package Master;

import lejos.nxt.*;

public class ObjectDetector {
	private ColorSensor cs;
	private UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
	private TwoWheeledRobot robot;
	private Odometer odo;
	private Navigation nav;
	int approximate = 7;
	
	private static final int ROTATE_SPEED = 20;
	
	public ObjectDetector(Navigation nav, UltrasonicSensor us, ColorSensor cs) {
		this.nav = nav;
		this.robot = nav.getRobot();
		this.us = us;
		this.cs = cs;
		this.odo = nav.getOdometer();
	}
	
	public boolean detect(int direction) {
		double angle = odo.getTheta();				
		robot.setSpeeds(0.0, direction * ROTATE_SPEED);
		
		int distance = 255, low = 255;
		int prevDistance = distance;
		
		int trueCount = 0, allCount = 0;
		
		while ((trueCount < 2 || allCount < 5) && angleWithin(angle, 90.0)) {
			LCD.clear();
			LCD.drawString("true: " + trueCount, 0, 2);
			LCD.drawString("all:  " + allCount, 0, 3);
			LCD.drawString("dist: " + distance, 0, 4);
			LCD.drawString("low:  " + low, 0, 5);
			
			distance = getDistance();
			
			boolean unreliable = distance < 50 && prevDistance > 200;
			if (distance < 120 && !unreliable && distance < low) low = distance;
			
			if (distance > low) {
				if (distance < 255) {
					trueCount ++;
				} 
				allCount ++;
			}
			
			if (distance < 255) prevDistance = distance;
			
			try { Thread.sleep(10); } catch (Exception e) {}
		}
		
		robot.stop();
		return angleWithin(angle, 90.0);
	}
	
	public boolean angleWithin(double angle, double degrees) {
		double theta = odo.getTheta();
		double diff = Odometer.minimumAngleFromTo(angle, theta);
		return Math.abs(diff) < degrees;
	}
	
	public int getDistance() {
		int[] distances = new int[]{ us.getDistance(), us.getDistance(), us.getDistance() };
		int distance = 255;
		for (int dist : distances) {
			distance = Math.min(dist, distance);
		}
		return distance;
	}
	
	
	public enum type { OBJECT, BLOCK }										//Enum gives the types of objects
	
	public static type TYPE;
	
		
	public boolean detector() throws Exception {
		
		boolean result = false;	
		boolean detecting = true;
		
		Sound.beepSequence();
		
		int red,green,blue;
		
		Motor.A.setSpeed(100);												//Sets the forward speed of both left and right motors
		Motor.B.setSpeed(100);
		
		while(detecting) {													//Runs a while loop
			
			//Sound.beepSequence();
			//LCD.drawInt(us.getDistance(), 0, 2);
			Motor.A.forward();												//Moves forward
			Motor.B.forward();
			
			//robot.setForwardSpeed(30);
			
			if(us.getDistance() <= 16){										//If the distance from the ultrasonic sensor is less than 16
				cs.setFloodlight(cs.BLUE);									//The floodlight is set to blue
				
				Motor.A.stop();												//The robot stops
				Motor.B.stop();
				
				//robot.setForwardSpeed(0);
				
				Thread.sleep(500);											//Thread sleeps for 500 ms
				
//				LCD.drawString(cs.getColor().getBlue() + "", 0 , 1);
//				LCD.drawString(cs.getColor().getGreen() + "", 0 , 3);
//				LCD.drawString(cs.getColor().getRed() + "", 0 , 5);
				
				red= cs.getColor().getRed();								//We get the red, green and blue colour and stores them in variables
				green= cs.getColor().getGreen();
				blue= cs.getColor().getBlue();
				
				if(Math.abs(red-blue) <= approximate){						//The following test the conditions for detection					
					LCD.drawString("Object", 0, 3);							//If the red - blue is within the approximate range then it is an object
					TYPE = type.OBJECT;
					Sound.beep();
					result = true;
					detecting = false;
				}
				
				else{
					LCD.drawString("Block", 0, 3);							//Else a block
					TYPE = type.BLOCK;
					Sound.buzz();
					result = false;
					detecting = false;
				}
				
				
				
			}
			
			
		}
		return result;
		
	}
	

}
