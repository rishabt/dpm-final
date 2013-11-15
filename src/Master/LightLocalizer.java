package Master;

import lejos.nxt.*;

public class LightLocalizer {
	
	private Odometer odo;
	private TwoWheeledRobot robot;
	private LightSensor ls;
	private Navigation nav;
	private double distance = 7.5;

	int speed = 150; int count = 4;
	double[] angleList = new double[4];
	
	public double[] position = new double[3];
	
	public LightLocalizer(Odometer odo, LightSensor ls) {			//Constructor to initialize all required objects
		this.odo = odo;
		this.robot = odo.getTwoWheeledRobot();
		this.ls = ls;
		this.nav = new Navigation(odo);
		
		// turn on the light
		ls.setFloodlight(true);
	}
	
	public void doLocalization(){
		
		double[] gridAngles = new double[4];
		
		boolean condition = true;
		
		int i = 0;
		
		while(condition){
			
			robot.setRotationSpeed(30);
			
//			if(ls.readNormalizedValue() <= 500){
//				Sound.beep();
//			}
			
			if(ls.readNormalizedValue() < 500){
				gridAngles[i] = odo.getTheta();
				Sound.beep();
				
				try{Thread.sleep(800);} catch(Exception e){}
			}
			
			i++;
			
			if(i == 4)
				condition = false;
			
			LCD.drawInt(ls.readNormalizedValue(), 0, 5);
			
		}
			
		//robot.setRotationSpeed(0);
		
		double thetaY = Math.abs(gridAngles[1] - gridAngles[3]);
		
		double thetaX = Math.abs(gridAngles[0] - gridAngles[2]);
		
		double newX = - distance * Math.cos(thetaY/2);
		double newY = - distance * Math.cos(thetaX/2);
		
		odo.setPosition(new double [] {newX, newY, 0.0}, new boolean [] {true, true, false});
		
		//nav.travelTo(0, 0);
	}
	

}
