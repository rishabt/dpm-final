package Master;

import lejos.nxt.Motor;
import lejos.nxt.NXTRegulatedMotor;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class USLocalizer {
	
	public enum LocalizationType { FALLING_EDGE, RISING_EDGE };
	public static double ROTATION_SPEED = 60;    
	public static double width = 12.61;
	private Odometer odo;
	private TwoWheeledRobot robot;
	private UltrasonicSensor us;
	private final int walld = 50;
	
	
	public USLocalizer(Odometer odo, UltrasonicSensor us) {
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.us = us;
		us.off();
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
	    		Sound.beepSequence();
	    		//try { Thread.sleep(100); } catch (InterruptedException e) {}    		
	    	}
	    	else if (distance > walld ){
	    		initial = false;
	    		progress = 1;
	    		Sound.beep();	
	    		//try { Thread.sleep(100); } catch (InterruptedException e) {}
		    		
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
	    		Motor.A.resetTachoCount();
	    		Motor.B.resetTachoCount();
	    		progress = 1;
	    		//Sound.beep();
	    		odo.setPosition(reset,reset1);
	    		initial = false;//quit the initial process
	    	Sound.beep();
		    			    	
	    	}	
		    	
	    }
	    //try { Thread.sleep(100); } catch (InterruptedException e) {}
	    // ****progress 1 starts here: 
	    // rotate until reaching a distance less than 40. when it does,
	    // stop the vehicle temporarily and latch angleA.
	    // then proceed to progress 2.
	    while (progress == 1 && distance > walld ){
	    	
             odo.getPosition(pos);
	    	distance = getFilteredData();
	    	robot.setRotationSpeed(ROTATION_SPEED);
		    			    	
	    	if(distance < walld){		    		
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
	    	   Motor.A.rotate(convertAngle(2.885, width,  value-70), true);
			   Motor.B.rotate(-convertAngle(2.885, width, value-70),false);  
	         }
	         if(angleA > angleB){
	           Motor.A.rotate(convertAngle(2.885, width,  -value+15), true);
			   Motor.B.rotate(-convertAngle(2.885, width, -value+15),false);  
	         }
		         
			   try { Thread.sleep(100); } catch (InterruptedException e) {}
			   odo.getPosition(pos);
			   boolean update[] = {true, true, true};
			   double newpos[] = {0,0,0+1};
			   odo.setPosition(newpos, update);
			   //progress = 4;
	       //}
	      // if(angleA > 180){
	       //Motor.A.rotate(convertAngle(2.885, 16.755,  test ), true);
		   //Motor.B.rotate(-convertAngle(2.885, 16.755, test),false);
	       //}
	       }
	    }
	    
	   /*if(progress == 4){
		       
	    	   Motor.A.rotate(convertAngle(2.885, width,  90), true);
			   Motor.B.rotate(-convertAngle(2.885, width, 90),false);  
	         
			   Motor.A.setSpeed(50);
			   Motor.B.setSpeed(50);
			   Motor.A.forward();
			   Motor.B.forward();
			   
			   
	   }*/
	      	
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
