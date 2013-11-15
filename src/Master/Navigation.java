package Master;

import lejos.nxt.*;

//													USED AS GIVEN TO US IN LAB 4

public class Navigation {
	// put your navigation code here 
	// NAVIGATION USED AS A MIX FROM LAST LAB AND FROM THE GIVEN CODE AND SOME EXTRA METHODS ADDED
	
	private Odometer odo;
	private UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
	private TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	private int ROTATE_SPEED = 50;
	private double leftRadius = 2.9;
	private double rightRadius = 2.9;
	private double width = 12.61;
	public double[] position = new double[3];
	public int ROTATION_SPEED = 150;
	final static int FAST = 200, SLOW = 100;
	final static double DEG_ERR = 1.0, CM_ERR = 1;
	
	private static double finalX, finalY;
	
	private boolean objectCollected = false;
	
	private int i = 1;

	
	public Navigation(Odometer odo) {
		this.odo = odo;
	}
	
	public TwoWheeledRobot getTwoWheeledRobot(){
		return this.robot;
	}
	
	public void travelTo(double x, double y) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		
		double requiredAngle;
		
		boolean obstacle = false;
		
		requiredAngle = Math.toDegrees((Math.atan2(x - odo.getX(), y - odo.getY())));
		
//		if (requiredAngle > Math.PI)
//			requiredAngle = requiredAngle - 2*Math.PI; 
		
		turnTo(requiredAngle);
		
		while (Math.abs(x - odo.getX()) > CM_ERR || Math.abs(y - odo.getY()) > CM_ERR) {
			
			if(us.getDistance() <= 25 && objectCollected == false){
				obstacleAvoider(x, y);					
				break;
				} 
			
			else if(us.getDistance() <= 25 && objectCollected == true){
				objectDeliver(finalX, finalY);
				break;
			}
				

			robot.setForwardSpeed(10);
		}
		
		robot.setForwardSpeed(0);

		
	}
	
	public double minTheta(double angle){
		if (angle < -180)
			angle += 360;
		
		else if(angle > 180)
			angle -= 360;
		
		return angle;
	}
	
	public void turnTo(double angle){
		
				
				Motor.A.setSpeed(ROTATE_SPEED);
				Motor.B.setSpeed(ROTATE_SPEED);			
				
				double correctedAngle = angle - odo.getTheta();
				
				correctedAngle = minTheta(correctedAngle);
				
				Motor.A.rotate(convertAngle(leftRadius, width, correctedAngle), true);
				Motor.B.rotate(-convertAngle(rightRadius, width, correctedAngle), false);
				
				Sound.beep();
				
				Motor.A.stop();
				Motor.B.stop();
		
	}
	
	
	public int convertDistance(double radius, double distance) {							//Copied same from Lab 2 
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	public int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	

	
	public void goForward(){
		
		Motor.A.setSpeed(100);
		Motor.B.setSpeed(100);
		
		Motor.A.forward();
		Motor.B.forward();
	}
	
	public void obstacleAvoider(double x, double y){
		
		Sound.beepSequence();
		
		int filter = 0;
		
		try {
			if(ObjectDetector.detector() == false){
				if(i % 2 != 0){
					turnTo(odo.getTheta() + 90);
					Sound.beepSequenceUp();
				}
				
				else
					turnTo(odo.getTheta() - 90);
				
				
				moveBy(35);
				
				
				if(i % 2 != 0)
					turnTo(odo.getTheta() - 90);
				
				else
					turnTo(odo.getTheta() + 100);
				
				
				while (true){
				
					if(us.getDistance() >= 55){
						filter ++;
					}
					
					else
						turnTo(odo.getTheta() + 15);
					
					if(filter >= 10){
						moveBy(45);
						travelTo(x,y);
						
						moveBy(-30);
						
						break;
					}
				}
				
			}
			
			
			if(ObjectDetector.detector() == true){			// DO THE COMMUNICATION AND GRABBING VIA SLAVE
				
			}
			
			
		} 
		
		catch (Exception e) {
		}
		
		i++;
		
	}
	
	public void objectDeliver(double x, double y){
		Sound.beepSequenceUp();
		
		int filter = 0;
		
		if(i % 2 != 0){
			turnTo(odo.getTheta() + 90);
		}
		
		else
			turnTo(odo.getTheta() - 90);
		
		moveBy(35);
		
		if(i % 2 != 0){
			turnTo(odo.getTheta() - 90);
		}
		
		else
			turnTo(odo.getTheta() + 90);
		
		
		while (true){
			
			if(us.getDistance() >= 55){
				filter ++;
			}
			
			else
				turnTo(odo.getTheta() + 15);
			
			if(filter >= 10){
				moveBy(45);
				travelTo(x,y);
				break;
			}
		}
		
		i++;
		
	}
	
	public void moveBy(int distance){
		
		Motor.A.setSpeed(100);
		Motor.B.setSpeed(100);
		
		Motor.A.rotate(convertDistance(leftRadius, distance), true);
		Motor.B.rotate(convertDistance(rightRadius, distance), false);
	
	}
	
	public void stop()
	{
		Motor.A.stop();
		Motor.B.stop();
	}
	
	
	
//	public void startRotating(){											//Some helper methods created
//		robot.setRotationSpeed(ROTATE_SPEED);
//	}
//	
//	public void startRotatingCounter(){
//		robot.setRotationSpeed(-10);
//	}
	
}
