package Slave;

import lejos.nxt.*;

public class Clamp {
	public int count = 0;
	
	public static void lockMotors() {
		Motor.A.stop();
		Motor.B.stop();

	}
    public Clamp(int count){
    	this.count = count;
    }
    
    public static void lifting() {
       NXTRegulatedMotor mastermotor = Motor.A;
       NXTRegulatedMotor leftclaw = Motor.B; 
 
       int gangle = 150;
       int langle = 210;
       int cspeed = 50;
         
       mastermotor.setSpeed(cspeed);
    	 leftclaw.setSpeed(cspeed);

    	
    	mastermotor.rotate(langle);
    	//try { Thread.sleep(1000); } catch (InterruptedException e) {}
    	leftclaw.rotate(gangle);
        mastermotor.rotate(-langle/3*2);
		try { Thread.sleep(1000); } catch (InterruptedException e) {}
		Sound.beep();
		
		lockMotors();
    }
    
    public static void dropping(){
    	NXTRegulatedMotor mastermotor = Motor.A;
        NXTRegulatedMotor leftclaw = Motor.B; 

        int gangle = 95;
        int langle = 95;
        int cspeed = 50;
        
        mastermotor.setSpeed(cspeed);
   	    leftclaw.setSpeed(cspeed);

   	    
   	   //mastermotor.rotate(-langle);
		//try { Thread.sleep(2000); } catch (InterruptedException e) {}
		mastermotor.rotate(langle/3*2);
		leftclaw.rotate(-gangle);

		Sound.beepSequence();
		mastermotor.rotate(-100);
		try { Thread.sleep(3000); } catch (InterruptedException e) {}
		lockMotors();
    }
    
    private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
			}
			// methods of determining the rotating angles for the wheels from Lab 2's SquareDriver
  private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
			}
}
