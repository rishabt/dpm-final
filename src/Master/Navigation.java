package Master;

import lejos.nxt.*;

public class Navigation {
	
	private Odometer odo;
	private TwoWheeledRobot robot;
	private int ROTATE_SPEED = 50;
	private double leftRadius = 2.95;
	private double rightRadius = 2.95;
	private double width = 15.4;
	public double[] position = new double[3];
	public int ROTATION_SPEED = 150;
	final static int FAST = 200, SLOW = 100;
	final static double DEG_ERR = 1.0, CM_ERR = 1.0;

	
	public Navigation(Odometer odo) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
	}
	
	public void travelTo(double x, double y) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		
		double requiredAngle;
		
		requiredAngle = (Math.atan2(y - odo.getY(), x - odo.getX())) * (180.0 / Math.PI);
		
		if (requiredAngle < 0)
			requiredAngle += 360.0;
		
		turnTo(requiredAngle);
		
		while(Math.abs(y - odo.getY()) > CM_ERR || Math.abs(y - odo.getY()) > CM_ERR){
			robot.setForwardSpeed(10);
		}
		
		robot.setForwardSpeed(0);
	}
	
	public void turnTo(double angle) {
		
		odo.getPosition(position);
		double error = Math.abs(angle - position[2]);

		while (Math.abs(error) > DEG_ERR) {

				odo.getPosition(position);
				
				error = Math.abs(angle - position[2]);
				
				robot.setRotationSpeed(30);		
						
		}
		
		robot.setRotationSpeed(0);
		
	}
	
	public void goForward(){
		
		Motor.A.setSpeed(100);
		Motor.B.setSpeed(100);
		
		Motor.A.forward();
		Motor.B.forward();
	}
	
	public void goBackward(){
		
		Motor.A.setSpeed(100);
		Motor.B.setSpeed(100);
		
		Motor.A.backward();
		Motor.B.backward();
	}
	
	public void moveBy(int distance){
		
		Motor.A.setSpeed(100);
		Motor.B.setSpeed(100);
		
		Motor.A.rotate(convertDistance(leftRadius, distance), true);
		Motor.B.rotate(convertDistance(rightRadius, distance), false);
	
	}
	
	public void startRotating(){											//Some helper methods created
		robot.setRotationSpeed(ROTATE_SPEED);
	}
	
	public void startRotatingCounter(){
		robot.setRotationSpeed(-10);
	}
	
	public static int convertDistance(double radius, double distance) { 
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	public static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
}
