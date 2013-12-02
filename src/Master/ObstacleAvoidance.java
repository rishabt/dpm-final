package Master;

import lejos.nxt.*;

/**
 * 
 * @author Rishabh
 *
 */

public class ObstacleAvoidance extends Thread{

	private UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
	private TwoWheeledRobot robot;
	private Navigation nav;
	
	/**
	 * 
	 * @param nav
	 * @param robot
	 */
	public ObstacleAvoidance(Navigation nav, TwoWheeledRobot robot){
		this.robot = robot;
		this.nav = nav;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean avoider(){
		
		boolean result = false;
		
		if(us.getDistance() <= 35){
			robot.setForwardSpeed(0);
			
			result = true;
		}
		
		return result;
		
	}
	
	/**
	 * 
	 */
	public void action(){
		nav.turnTo(90);
		
		nav.moveBy(30);
		
		nav.turnTo(0);
	}
	
	public void map(){
		
	}

}
