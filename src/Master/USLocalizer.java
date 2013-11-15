package Master;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 30;

	private Odometer odo;
	private Navigation nav;
	private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private LocalizationType locType;
	public boolean isRotating;
	public boolean wallPresence;
	public boolean wallAbsence;
	public double[] position = new double[3];
	public int distanceCheck;
	private double newTheta; //change to private later
	
	public USLocalizer(Odometer odo, UltrasonicSensor us, LocalizationType locType) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.us = us;
		this.locType = locType;
		
		this.nav = new Navigation(odo);
		
		// switch off the ultrasonic sensor
		us.off();
	}
	
	public void doLocalization() {
		//double [] position = new double [3];
		double angleA, angleB;
		int filterControl = 0;
		
		try{
			
		if (locType == LocalizationType.FALLING_EDGE) {
			// rotate the robot until it sees no wall
			
			if(getFilteredData() <= 32){									//Checks if the robot is facing the wall, in which case it has 
				while(getFilteredData() <= 32){								//to filter out values to start with the falling edge algorithm
					robot.setRotationSpeed(ROTATION_SPEED);
					
					if(getFilteredData() == 50){
						filterControl ++;
						Thread.sleep(500);
					}
				}
				
				
				
			}
			
			robot.setRotationSpeed(ROTATION_SPEED);
			
			
			
			if(filterControl >= 5){
				robot.setRotationSpeed(0);
			}
			
			
			robot.setRotationSpeed(ROTATION_SPEED);						//Robot starts rotating
			isRotating = true;											//Important variables initialized to their desired values
			wallAbsence = true;
			wallPresence = false;
			
			robot.setRotationSpeed(ROTATION_SPEED);
			
			while(wallAbsence){											//Condition when wall is absent 
				
				if(getFilteredData() < 40){								//If the sensor senses a wall nearby, it reverses the conditions
					wallAbsence = false;
					wallPresence = true;
				}
			}
			
			//robot.setRotationSpeed(0);
			
			odo.getPosition(position);									//The exact angle of the position of the wall is stored in angleA
			angleA = position[2];
			distanceCheck = getFilteredData();
			//LCD.drawString("" + angleA, 0, 4);
			
			robot.setRotationSpeed(-ROTATION_SPEED);					//The rotation is reversed to check the wall on the other side
			wallPresence = false;										//Conditions reset
			wallAbsence = true;
			
			Thread.sleep(500);											
			
			
			while(wallAbsence){											//While wall is absent the loop is run
				while(wallAbsence){
					
					if(getFilteredData() < 40){							//If a wall is detected, the conditions are reversed
						wallAbsence = false;
						wallPresence = true;
					}
				}
			}
			
			robot.setRotationSpeed(0);									//The motors stop rotating at that point
			
			odo.getPosition(position);									//The position of the second wall is stored in angleB
			angleB = 360 - position[2];
			//LCD.drawString("" + angleB, 0, 5);
			
			if(angleA > angleB){										//Conditions to calculate the newTheta
				newTheta = 44 - (angleA + angleB)/2;
				//LCD.drawString("aa" + newTheta, 0, 6);
			}
			
			else if(angleA < angleB){
					newTheta = 200 + (angleA + angleB)/2;
					//LCD.drawString("bb" + newTheta, 0, 6);
			}
			
			else {
				newTheta= 45;
				//LCD.drawString("abc", 0, 6);
			}
			odo.getPosition(position);
			double realPosition = ((newTheta)); 						//The odometer is set to the actual position it is at
			//LCD.drawString("dd" + realPosition, 0, 7);
			//Button.waitForAnyPress();
			odo.setPosition(new double [] {0.0, 0.0, realPosition}, new boolean [] {false, false, true});	
			//Button.waitForAnyPress();
			nav.turnTo(0);
			
			// keep rotating until the robot sees a wall, then latch the angle
		
		
			
			
			// switch direction and wait until it sees no wall
			
			// keep rotating until the robot sees a wall, then latch the angle
			
			// angleA is clockwise from angleB, so assume the average of the
			// angles to the right of angleB is 45 degrees past 'north'
			
			// update the odometer position (example to follow:)
			//odo.setPosition(new double [] {0.0, 0.0, 0.0}, new boolean [] {true, true, true});
		} 
		
		else {
			/*
			 * The robot should turn until it sees the wall, then look for the
			 * "rising edges:" the points where it no longer sees the wall.
			 * This is very similar to the FALLING_EDGE routine, but the robot
			 * will face toward the wall for most of it.
			 */
			
			//
			// FILL THIS IN
			//
				
			/* Exactly opposite of falling edge - The robot if present in a wallAbsence area, then it rotates till it sees the wall, in 
			 * which case it starts the rising edge algorithm. The cases are the opposite - If the robot detects noWall then it 
			 * records the angle and rotates in the opposite way, and as soon as it hits a point where it detects another wallAbsence
			 * it records that angle and stops. Then it calculates it's actual orientation depending on these angles, based on separate cases
			 * as given in FALLING_EDGE and adjusts all the readings. In the end, the robot is set to turn back to zero degrees.
			 */
						
			
			robot.setRotationSpeed(ROTATION_SPEED);
			
			if(getFilteredData() == 50){
				wallPresence = false;
				wallAbsence = true;
			}

			while(wallAbsence)
			{
				 if(getFilteredData() < 25)
				 {
					  wallAbsence = false;
					  wallPresence = true;
				 }
			}
			
			Thread.sleep(500);
			
			while(wallPresence){
				
				if(getFilteredData() > 32){
					wallAbsence = true;
					wallPresence = false;
				}
			}
			
			odo.getPosition(position);
			angleA=position[2];
			
			robot.setRotationSpeed(-ROTATION_SPEED);
			
			Thread.sleep(500);

			wallPresence = true;
			wallAbsence = false;
			
			while(wallPresence){
				
				if(getFilteredData() > 32){
					wallAbsence = true;
					wallPresence = false;
				}
			}
			
			odo.getPosition(position);
			angleB=position[2];
			
			robot.setRotationSpeed(0);

			odo.getPosition(position);
			
			if(angleA < angleB){
				newTheta = 45-(angleA + angleB)/2;
				//LCD.drawString("aa" + newTheta, 0, 6);
			}
			
			else if(angleA > angleB){
					newTheta = 225-(angleA + angleB)/2;
					//LCD.drawString("bb" + newTheta, 0, 6);
			}
			
			else {
				newTheta= 45;
				//LCD.drawString("abc", 0, 6);
			}
			
			odo.getPosition(position);
			
			double realPosition = position[2] + (position[2] + newTheta); 
			LCD.drawString("dd" + realPosition, 0, 7);
			//Button.waitForAnyPress();
			odo.setPosition(new double [] {0.0, 0.0, realPosition}, new boolean [] {false, false, true});	
			
			nav.turnTo(0);
			
		}
		
		}
		
		catch(Exception e){
			
		}
		
	}
	
	private int getFilteredData() {
		int distance;
		
		// do a ping
		us.ping();
		
		// wait for the ping to complete
		try { 
			Thread.sleep(50); 
		} catch (InterruptedException e) {}
		
		// there will be a delay here
		distance = us.getDistance();
				
		return distance;
	}

}
