package Master;

import lejos.nxt.*;
import Support.Communicator;
import java.util.LinkedList;

/**
 * 
 * @author Rishabh
 *
 */

public class Navigation {
	
	private Odometer odo;
	private UltrasonicSensor us;
	private TwoWheeledRobot robot;
	private Grid grid;
	
	private int ROTATE_SPEED = 50;
	
	private double leftRadius = 2.885;
	private double rightRadius = 2.885;
	private double width = 12.61;
	
	public double[] position = new double[3];
	
	public int ROTATION_SPEED = 150;
	final static int FAST = 200, SLOW = 100;
	final static double DEG_ERR = 1.0, CM_ERR = 1;
	
	private static double finalX = 6.0, finalY = 5.0;
	
	public boolean objectCollected = false;
	
	private int i = 1;
	private double tempX;
	private double tempY;
	private Communicator comm;
	
	/**
	 * 
	 * @param odo
	 * @param us
	 * @param comm
	 */
	
	public Navigation(Odometer odo, UltrasonicSensor us, Communicator comm) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		//this.grid = odo.getGrid();
		this.us = us;
		this.comm = comm;
		
		us.ping();
	}
	
	/*
	*  Navigation is done by generating a path of points, and then sequentially
	*  following these steps:
	*    - check if there is a block to close. If there is, report the location
	*      of the block
	*    - if there was a block, restart, otherwise travel to point
	*/
	
	/**
	 * 
	 * @param destination
	 * @throws Exception
	 */
	public void navigateTo(Point destination) throws Exception {
		Point location = new Point((int) odo.getX(), (int) odo.getY());
		LinkedList<Point> path = grid.depthFirst(location, destination);
		
		while (path.size() > 0 && navigate(path));
		
		// recursively try again if we didn't make it
		if (path.size() > 0) navigateTo(destination);
	}
	
	/**
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	private boolean navigate(LinkedList<Point> path) throws Exception {
		Point collision = collisionPoint();
		
		if (collision == null) {
			travelTo(path.remove(0));
			return true;
		} else {
			grid.report(collision.x, collision.y);
			return false;
		}
	}
		
	/**
	 * 
	 * @return
	 */
	private Point collisionPoint() {
		int distance = getDistance();
		
		Point point = null;
		
		// Rishab: the distance for a collision must be determined experimentally
		
		if (distance < 40) {
			double angle = Math.toRadians(odo.getTheta());
		
			int x = (int) (distance * Math.cos(angle) + odo.getX());
			int y = (int) (distance * Math.sin(angle) + odo.getY());
			
			point = new Point(x, y);
		}
		
		return point;
	}
	
	/**
	 * 
	 * @return
	 */
	private int getDistance() {
		
		// Rishab: possibly need to do multiple readings here
		
		int filter = 0;
		
		int[] distances = new int[8];
		
		int minimum = 255;
		
		us.getDistances(distances);
		
		for(int i = 0; i < distances.length; i++){
			if(distances[i] < minimum){
				minimum = distances[i];
			}
		}
		
		
		return minimum;
	}
	
	/*
	*  Travelling simply moves the robot to (x, y) assuming the path is safe. It
	*  must only be used for short journeys. 
	*/
		
	/**
	 * 
	 * @param point
	 * @throws Exception
	 */
	public void travelTo(Point point) throws Exception {
		travelTo(point.x, point.y);
	}
		
	/**
	 * 
	 * @param x
	 * @param y
	 * @return
	 * @throws Exception
	 */
	public boolean travelTo(double x, double y) throws Exception {
		
		boolean travelled = false;
		
		tempX = x;
		tempY = y;
		
		double requiredAngle;
		
		requiredAngle = Math.toDegrees((Math.atan2(x - odo.getX(), y - odo.getY())));
		
		
//		if (requiredAngle > Math.PI)
//			requiredAngle = requiredAngle - 2*Math.PI; 
		
		turnTo(requiredAngle);
		
		while (Math.abs(x - odo.getX()) > CM_ERR || Math.abs(y - odo.getY()) > CM_ERR) {
			
			us.ping();
			
			if(us.getDistance() <= 30){
				obstacleAvoid();
				break;
			}
			
			robot.setForwardSpeed(25);
			
		}
		
		robot.setForwardSpeed(0);
		
		travelled = true;
		
		return travelled;
		
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	public void obstacleAvoid() throws Exception{
		
		us.ping();
		
		boolean styrofoam = ObjectDetector.detector();
		
		if(styrofoam){																//If a Styrofoam block
			//moveBy(10);
			comm.bluetoothSend("lift");
		}
		
		else{
			//moveBy(-10);
			
			turnBy(90);
			
			if(us.getDistance() >= 45){
				moveBy(35);
			}
			
			else{
				turnBy(-180);
				
				if(us.getDistance() >= 45){
					moveBy(35);
				}
				
			}
			
			if(odo.getX() < 300 && odo.getY() < 300){
				travelTo(tempX + 30, tempY + 30);
			}
		}
	}
	
	/**
	 * 
	 * @param angle
	 * @return
	 */
	
	public double minTheta(double angle){
		if (angle < -180)
			angle += 360;
		
		else if(angle > 180)
			angle -= 360;
		
		return angle;
	}
	
	/**
	 * 
	 * @param angle
	 */
	public void turnTo(double angle) {
		// USE THE FUNCTIONS setForwardSpeed and setRotationalSpeed from TwoWheeledRobot!
		
		Motor.A.setSpeed(ROTATE_SPEED);
		Motor.B.setSpeed(ROTATE_SPEED);			
		
		double correctedAngle = angle - odo.getTheta();
		
		correctedAngle = minTheta(correctedAngle) + 2;
		
		Motor.A.rotate(convertAngle(leftRadius, width, correctedAngle), true);
		Motor.B.rotate(-convertAngle(rightRadius, width, correctedAngle), false);
		
		Sound.beep();
		
		odo.setTheta(angle);
		
		Motor.A.stop();
		Motor.B.stop();
		
	}
	
	/**
	 * 
	 * @param radius
	 * @param distance
	 * @return
	 */
	public int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}

	/**
	 * 
	 * @param radius
	 * @param width
	 * @param angle
	 * @return
	 */
	public int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
	
	/**
	 * 
	 * @param distance
	 */
	public void moveBy(int distance){
		
		Motor.A.setSpeed(100);
		Motor.B.setSpeed(100);
		
		Motor.A.rotate(convertDistance(leftRadius, distance), true);
		Motor.B.rotate(convertDistance(rightRadius, distance), false);
	
	}
	
	/**
	 * 
	 * @param angle
	 */
	public void turnBy(int angle){
		
		turnTo(odo.getTheta() + angle);
	}
	
	/**
	 * 
	 */
	
	public void stop() {
		robot.stop();
	}
	
	
	
	
	/*
	*  Simple getters for internal objects. Passing in Navigation object gives
	*  access to Odometer, Robot, UltrasonicSensor, and Grid.
	*/
	/**
	 * 
	 * @return
	 */
	public TwoWheeledRobot getRobot(){
		return this.robot;
	}
	
	/**
	 * 
	 * @return
	 */
	public Odometer getOdometer() {
		return this.odo;
	}
	
	/**
	 * 
	 * @return
	 */
	public Grid getGrid() {
		return this.grid;
	}
	
	/**
	 * 
	 * @return
	 */
	public UltrasonicSensor getUltrasonicSensor() {
		return this.us;
	}
	
}
