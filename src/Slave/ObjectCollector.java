package Slave;

import lejos.nxt.*;
public class ObjectCollector {

    
//    public static void main(String[] args){
//             
//    	 Button.waitForAnyPress();
//    	
//    	 lifting();
//    	 
//    	 dropping();
//		
//    }
    
    public static void lifting(){
    	
    	 NXTRegulatedMotor mastermotor = Motor.A;
         NXTRegulatedMotor leftclaw = Motor.B; 
         NXTRegulatedMotor rightclaw = Motor.C;
         int gangle = 95;
         int langle = 95;
         int cspeed = 50;
         
         mastermotor.setSpeed(cspeed);
    	 leftclaw.setSpeed(cspeed);
    	 rightclaw.setSpeed(cspeed);
    	
    	mastermotor.rotate(langle);
    	//try { Thread.sleep(1000); } catch (InterruptedException e) {}
    	leftclaw.rotate(gangle, true);
		rightclaw.rotate(gangle, false);
		try { Thread.sleep(1000); } catch (InterruptedException e) {}
		Sound.beep();
    }
    
    public static void dropping(){
    	NXTRegulatedMotor mastermotor = Motor.A;
        NXTRegulatedMotor leftclaw = Motor.B; 
        NXTRegulatedMotor rightclaw = Motor.C;
        int gangle = 95;
        int langle = 95;
        int cspeed = 50;
        
        mastermotor.setSpeed(cspeed);
   	    leftclaw.setSpeed(cspeed);
   	    rightclaw.setSpeed(cspeed);
   	    
   	   mastermotor.rotate(-langle);
		//try { Thread.sleep(2000); } catch (InterruptedException e) {}
		mastermotor.rotate(langle);
		leftclaw.rotate(-gangle,true);
		rightclaw.rotate(-gangle,false);
		Sound.beepSequence();
		mastermotor.rotate(-100);
		try { Thread.sleep(3000); } catch (InterruptedException e) {}
        
    }
    
    private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
			}
			// methods of determining the rotating angles for the wheels from Lab 2's SquareDriver
  private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
			}
}
