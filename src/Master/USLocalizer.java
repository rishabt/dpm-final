package Master;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 40;    
	
	private Odometer odo;
	private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private final int walld = 50;
	
	
	public USLocalizer(Odometer odo, UltrasonicSensor us) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.us = us;
	}
	
	public void doLocalization() {
		double[] pos = new double [3];
		double angleA = 0, angleB = 0;
      double progress = 0;
	   int distance = getFilteredData();
	   boolean initial = false;
	   boolean isanglefinished = false;
		   
			
		// rotate the robot until it sees no wall
	    // starting direction identification: if the starting feedback distance is more than 50,
	   //  proceed to progress 1(initial = false && progress = 1); 
	   // else, initiate the initial turning to make it more than 40(initial = true && progress =0)
	    while( progress == 0 && initial == false){
	    	distance = getFilteredData();
	    	if(distance < walld){
	    		initial = true;
	    		progress = 0;		    		
	    	}
	    	else if (distance > walld ){
	    		initial = false;
	    		progress = 1;
	    		Sound.beep();		    		
		    		
	    	}
	    }
		    
	    //try { Thread.sleep(100); } catch (InterruptedException e) {}
		    
	    // initial process(progress = 0), if the distance is more than 50, go to progress 1
	    while( progress == 0 && initial == true){
		    	
	    	double[] reset = new double[3];
	    	reset[0] = 0; reset[1] = 0; reset[2] = 0;
	    	boolean[] reset1 = { true, true, true};
		    	
	    	distance = getFilteredData();
	    	robot.setRotationSpeed(ROTATION_SPEED);
		    	
	    	if(distance > walld ){	    		
	    		distance = getFilteredData();
	    		progress = 1;
	    		odo.setPosition(reset,reset1);
	    		initial = false;//quit the initial process
	    	Sound.beep();
		    			    	
	    	}	
		    	
	    }
	    try { Thread.sleep(100); } catch (InterruptedException e) {}
	    // ****progress 1 starts here: 
	    // rotate until reaching a distance less than 40. when it does,
	    // stop the vehicle temporarily and latch angleA.
	    // then proceed to progress 2.
	    while (progress == 1 && distance > walld-1 ){
		    	
             odo.getPosition(pos);
	    	distance = getFilteredData();
	    	robot.setRotationSpeed(ROTATION_SPEED);
		    			    	
	    	if(distance < walld+1){		    		
	    		robot.setRotationSpeed(-ROTATION_SPEED);
	    		angleA = pos[2];		    		
	    		progress = 2;		    				    				    		
	    	}
		    			    	
	    }
		    
		try { Thread.sleep(1000); } catch (InterruptedException e) {}
			
	    //rotate in the opposite direction(progress 2) after angleA is recorded.
	    //then, when seeing the other wall, latch the angle.
	    while (progress == 2){
		    	
	    	odo.getPosition(pos);
	    	distance = getFilteredData();
	    	robot.setRotationSpeed(-ROTATION_SPEED);
		    	
		    	
	    	if(distance < walld ){		    		
	    		angleB = 360 -pos[2];		    		
	    		robot.setRotationSpeed(0.0);	
		    		
	    		try { Thread.sleep(80); } catch (InterruptedException e) {}
		    		
	    		progress = 3;//go to progress 3		    				    		
	    	}
	    }
		    
	    //progress == 3 is the calculation and turning to y=axis process.	
	    if (progress == 3){
		    	
	    	double value = 0.0;
	        //double test = (angleA-angleB)/2 - 45;
		        
	    	if(angleA > angleB){
	    	   value = 45 - (angleA + angleB)/2;
	    	   isanglefinished = true;
	       }
	       else if(angleB > angleA){
	    	   value = 225 - (angleB + angleA)/2;
		    	   
	    	   try { Thread.sleep(80); } catch (InterruptedException e) {}
		    	   
	    	   isanglefinished = true;		    	   
	       }
		       
	    	try { Thread.sleep(80); } catch (InterruptedException e) {}
		    	
	       if(isanglefinished == true){
	       Motor.A.setSpeed(80);
	       Motor.B.setSpeed(80);
		       
	         if(angleA < angleB){
	    	   Motor.A.rotate(convertAngle(2.885, 16.755,  value-87), true);
			   Motor.B.rotate(-convertAngle(2.885, 16.755, value-87),false);  
	         }
	         if(angleA > angleB){
	           Motor.A.rotate(convertAngle(2.885, 16.755,  -value-8), true);
			   Motor.B.rotate(-convertAngle(2.885, 16.755, -value-8),false);  
	         }
		         
			   try { Thread.sleep(100); } catch (InterruptedException e) {}
			   odo.getPosition(pos);
			   boolean update[] = {true, true, true};
			   double newpos[] = {0,0,0+1};
			   odo.setPosition(newpos, update);
	       //}
	      // if(angleA > 180){
	       //Motor.A.rotate(convertAngle(2.885, 16.755,  test ), true);
		   //Motor.B.rotate(-convertAngle(2.885, 16.755, test),false);
	       //}
	       }
	    }
		
		    
		//keep rotating until the robot sees a wall, then latch the angle
			
		//angle latched here( seeing the first-taking-into-account wall)
			
		// switch direction and rotate 
			
			
		// keep rotating until the robot sees a wall, then latch the angle
		
		// angleA is clockwise from angleB, so assume the average of the
		// angles to the right of angleB is 45 degrees past 'north'			
	}
	
	// methods of determining the rotations for the wheels from Lab 2's SquareDriver
	private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
	
	// methods of determining the rotating angles for the wheels from Lab 2's SquareDriver
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}

	private int getFilteredData() {
		int distance;
		
		// do a ping
		us.ping();
		
		// wait for the ping to complete
		try { Thread.sleep(50); } catch (InterruptedException e) {}
		
		// there will be a delay here
		distance = us.getDistance();
				
		return distance;
	}

}
