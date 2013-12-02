package Master;

import java.util.LinkedList;

import lejos.nxt.*;
import lejos.nxt.comm.*;

/**
 * 
 * @author Rishabh
 *
 */
public class Search implements SearchController{
	
	private int distance;

	private UltrasonicSensor us;
	private Navigation nav;
	private Odometer odo;
	private ColorSensor cs;
	private Grid grid; 
	private ObjectDetector obj;
	private Point point1; 
	private Point point2;
	private int tempDistance;
	private int lowX;
	private int hightX;
	private int lowY;
	private int highY;
	private TwoWheeledRobot robot;
	
	private LinkedList<Point> path;
	
	/**
	 * 
	 * @param nav
	 * @param us
	 * @param cs
	 * @param role
	 */
	public Search(Navigation nav, UltrasonicSensor us, ColorSensor cs, PlayerRole role){
		this.us = us;
		this.cs = cs;
		this.nav = nav;
		this.odo = nav.getOdometer();
		this.grid = nav.getGrid();
		this.robot = nav.getRobot();
		
		this.obj = new ObjectDetector(nav, us, cs);
	}

	@Override
	/**
	 * 
	 */
	public boolean search() {
		// TODO Auto-generated method stub
		
		boolean result = false;
		
		if(Master.role == PlayerRole.BUILDER){									//Role of a builder robot
			
			robot.setRotationSpeed(30);
			
			if(us.getDistance() <= 35){											//If distance < 35
				try {
					result = obj.detector();									//Check if a Styrofoam block
					
					if(result){													//If yes, return true
						return result;
					}
					
					else{
						nav.moveBy(-30);										//If an obstacle, move back
						nav.turnTo(90);											//Turn to 90 degrees
					}
						
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if(odo.getTheta() >= 90){
				robot.setRotationSpeed(0);											//If Theta crosses 90 degrees, stop rotating 
				
				if(us.getDistance() <= 35){											//If distance < 35
					try {
						result = obj.detector();									//Check if a Styrofoam block
						
						if(result){													//If yes, return true
							return result;
						}
						
						else{
							nav.moveBy(-30);										//If an obstacle, move back		<------
							nav.turnTo(90);											//Turn to 90 degrees			<------
						}
							
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				else{
					nav.moveBy(30);													//Else move robot by 30
				}
				
				if(us.getDistance() <= 35){											//If distance < 35
					try {
						result = obj.detector();									//Check if a Styrofoam block
						
						if(result){													//If yes, return true
							return result;
						}
						
						else{
							nav.moveBy(-30);										//If an obstacle, move back		<------
							nav.turnTo(90);											//Turn to 90 degrees			<------
						}
							
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				else{
					nav.moveBy(30);
				}
				
				nav.turnTo(0);
				
				if(us.getDistance() <= 35){											//If distance < 35
					try {
						result = obj.detector();									//Check if a Styrofoam block
						
						if(result){													//If yes, return true
							return result;
						}
						
						else{
							nav.moveBy(-30);										//If an obstacle, move back		<------
							nav.turnTo(90);											//Turn to 90 degrees			<------
						}
							
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				else{
					nav.moveBy(35);
				}
				
				nav.turnTo(-90);
				
				if(us.getDistance() <= 35){											//If distance < 35
					try {
						result = obj.detector();									//Check if a Styrofoam block
						
						if(result){													//If yes, return true
							return result;
						}
						
						else{
							nav.moveBy(-30);										//If an obstacle, move back		<------
							nav.turnTo(90);											//Turn to 90 degrees			<------
						}
							
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				else{
					nav.moveBy(30);
				}
				
				nav.turnTo(0);
				
				if(us.getDistance() <= 35){											//If distance < 35
					try {
						result = obj.detector();									//Check if a Styrofoam block
						
						if(result){													//If yes, return true
							return result;
						}
						
						else{
							nav.moveBy(-30);										//If an obstacle, move back		<------
							nav.turnTo(90);											//Turn to 90 degrees			<------
						}
							
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
				
				else{
					nav.moveBy(30);
				}
		}
			
			
		}
		
		else{																	//Role of a garbage collector robot
			
		}
		
		
		return result;
		
		
	}

	
	@Override
	/**
	 * 
	 */
	public int readUSDistance() {
		// TODO Auto-generated method stub
		
		return this.distance;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getDistance(){
		us.ping();
		
		return us.getDistance();
	}

}
