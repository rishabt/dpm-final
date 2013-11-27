package Master;

import lejos.nxt.*;

public class GarbageCollecter {
	
	private int[] greenZone;
	private int[] redZone;
	private UltrasonicSensor us;
	private ColorSensor cs;
	private Navigation nav;
	private Odometer odo;
	private TwoWheeledRobot robot;
	int[] path = new int[2];
	private double[] midGreen = new double[2];
	private double[] midRed = new double[2];
	
	public GarbageCollecter(int[] greenZone, int[] redZone, UltrasonicSensor us, ColorSensor cs, Navigation nav){
		this.greenZone = greenZone;
		this.redZone = redZone;
		this.us = us;
		this.cs = cs;
		this.nav = nav;
		this.robot = nav.getRobot();
		this.odo = nav.getOdometer();
		
		midGreen[0] = ((greenZone[0] * 30.48) + (greenZone[2] * 30.48))/2;
		midGreen[1] = ((greenZone[1] * 30.48) + (greenZone[3] * 30.48))/2;
		
		midRed[0] = ((redZone[0] * 30.48) + (redZone[2] * 30.48))/2;
		midRed[1] = ((redZone[1] * 30.48) + (redZone[3] * 30.48))/2;
		
	}
	
	
	public void collect() throws Exception{
		
		while(nav.objectCollected == false){
			
			if(Math.abs(odo.getX() - midRed[0]) < Math.abs(odo.getX() - midGreen[0])){
				nav.travelTo(midRed[0], midRed[1]);
				
			}
			
			else{
				nav.travelTo(midGreen[0] - 60, midGreen[1] - 60);
				
				if(odo.getX() <= 290){
					nav.turnBy(90);
				}
				
				else
					nav.turnBy(-90);
				
				if(us.getDistance() >= 40){
					nav.moveBy(30);
				}
				
				nav.turnBy(-80);
				
				nav.travelTo(midRed[0], midRed[1]);
				
			}
			
			
		}
		
		if(nav.objectCollected == true){
			nav.travelTo(midRed[0], midRed[1]);
		}
		
		
		
	}
	
	
	
}
