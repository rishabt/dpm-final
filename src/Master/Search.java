package Master;

import java.util.LinkedList;

import lejos.nxt.*;
import lejos.nxt.comm.*;

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
	
	public Search(Navigation nav, UltrasonicSensor us, ColorSensor cs){
		this.us = us;
		this.cs = cs;
		this.nav = nav;
		this.odo = nav.getOdometer();
		this.grid = nav.getGrid();
		this.robot = nav.getRobot();
		
		this.obj = new ObjectDetector(nav, us, cs);
	}

	@Override
	public boolean search() {
		// TODO Auto-generated method stub
		
		boolean result = false;
		
		if(odo.getX() <= 290 && odo.getY() <= 290){
			
		}
		
		return result;
		
		
	}

	
	@Override
	public int readUSDistance() {
		// TODO Auto-generated method stub
		
		return this.distance;
	}
	
	public int getDistance(){
		us.ping();
		
		return us.getDistance();
	}

}
