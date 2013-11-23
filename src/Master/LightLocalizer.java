package Master;

import lejos.nxt.*;

public class LightLocalizer {
	
	private Odometer odo;
	private TwoWheeledRobot robot;
	private LightSensor ls1;
	private LightSensor ls2;
	private double distance = 7.5;

	int speed = 150; int count = 4;
	double[] angleList = new double[4];
	
	public double[] position = new double[3];
	
	public LightLocalizer(Navigation nav, LightSensor ls1, LightSensor ls2) {			//Constructor to initialize all required objects
		this.odo = nav.getOdometer();
		this.robot = odo.getTwoWheeledRobot();
		this.ls1 = ls1;
		this.ls2 = ls2;
		
		// turn on the light
		ls1.setFloodlight(true);
		ls2.setFloodlight(true);
	}
	
	public void doLocalization(){
		
		double[] gridAngles = new double[4];
		
		boolean condition = true;
		
		int i = 0;
		
		while(condition){
			
			
		}
			

		
		double thetaY = Math.abs(gridAngles[1] - gridAngles[3]);
		
		double thetaX = Math.abs(gridAngles[0] - gridAngles[2]);
		
		double newX = - distance * Math.cos(thetaY/2);
		double newY = - distance * Math.cos(thetaX/2);
		
		odo.setPosition(new double [] {newX, newY, 0.0}, new boolean [] {true, true, false});
		
		//nav.travelTo(0, 0);
	}
	

}
