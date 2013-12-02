package Master;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.Sound;
import lejos.nxt.UltrasonicSensor;

public class lightlocalize {
	
    private LightSensor lsl = new LightSensor(SensorPort.S3);
    private LightSensor lsr = new LightSensor(SensorPort.S4);
    public static double ROTATION_SPEED = 40;    
	public static final double width = 12.61;
	public static final double radius = 2.885;
	public static int linevalue = 410;
	public static double x = 8.6,y = 4.9;//distance's error from the (0,0) to the center of the robot
	private Odometer odo;
	private TwoWheeledRobot robot;
    
	/**
	 * 
	 * @param odo
	 * @param lsl
	 * @param lsr
	 */
    public lightlocalize(Odometer odo, LightSensor lsl,LightSensor lsr){
       this.odo = odo;
       this.lsl = lsl;
       this.lsr = lsr;
    }
    
    /**
     * 
     */
    
    public void dolitlocalize(){
    	 int progress = 0;
    	 
    	 Motor.A.rotate(-15, true);
		 Motor.B.rotate(-15,false);
		 
    	 Motor.A.setSpeed(150);
		 Motor.B.setSpeed(150);
    	 Motor.A.rotate(convertAngle(2.885, width,  95), true);
		 Motor.B.rotate(-convertAngle(2.885, width, 95),false);  
       
		 Motor.A.setSpeed(150);
		 Motor.B.setSpeed(150);
		 Motor.A.forward();
		 Motor.B.forward();
		 
		 boolean islighton1 = true;
		 boolean islighton2 = false;
		 
		 while(islighton1 == true){
		     int lvalue = lsl.getNormalizedLightValue();
		     int rvalue = lsr.getNormalizedLightValue();
		     
		     if(lvalue < linevalue){
		    	Motor.A.setSpeed(0); 
		     }
		     
		     if(rvalue < linevalue){
		    	Motor.B.setSpeed(0); 
		     }
		     
		     if(Motor.A.isMoving() == false && Motor.B.isMoving() == false){
		    	 progress = 1; 
		    	 islighton1 = false;
		    	 
		     }
		 }
		      if (progress == 1){
		    	 
		    	 Motor.A.setSpeed(150);
				 Motor.B.setSpeed(150);
			
		    	 Motor.A.rotate(convertAngle(2.885, width, -90), true);
				 Motor.B.rotate(-convertAngle(2.885, width, -90),false); 
				 
				 Motor.A.setSpeed(150);
				 Motor.B.setSpeed(150);
				 Motor.A.forward();
				 Motor.B.forward();
				 islighton2 = true;
		      }
				 while(islighton2 == true){
				     int lvalue = lsl.getNormalizedLightValue();
				     int rvalue = lsr.getNormalizedLightValue();
				     
				     if(lvalue < linevalue){
				    	Motor.A.setSpeed(0);
				    	Motor.B.setSpeed(100);
				    	islighton2 = false;
				     }
				     
				     if(rvalue < linevalue){
				    	Motor.B.setSpeed(0);
				    	Motor.A.setSpeed(100);
				    	islighton2 = false;
				     }
				     
				     
				 }
				 
//				 Motor.A.setSpeed(200);
//				 Motor.B.setSpeed(200);
				 
				 
				     //updating the odometer.
				     double[] pos = Master.initPosition;
				     boolean[] allow = {true, true, true};
				     odo.setPosition(pos, allow);
				 
//				 odo.setY(y);
//				 odo.setX(x);
//				 odo.setTheta(0);

		 }
		
		
    /**
     * 
     * @param radius
     * @param distance
     * @return
     */
    
    private static int convertDistance(double radius, double distance) {
		return (int) ((180.0 * distance) / (Math.PI * radius));
	}
    
    /**
     * 
     * @param radius
     * @param width
     * @param angle
     * @return
     */
	
	// methods of determining the rotating angles for the wheels from Lab 2's SquareDriver
	private static int convertAngle(double radius, double width, double angle) {
		return convertDistance(radius, Math.PI * width * angle / 360.0);
	}
}