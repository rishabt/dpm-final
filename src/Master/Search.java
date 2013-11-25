package Master;

import java.util.LinkedList;

import lejos.nxt.*;
import lejos.nxt.comm.*;

public class Search implements SearchController{
	
	private int distance;

	UltrasonicSensor us;
	Navigation nav;
	Odometer odo;
	ColorSensor cs;
	Grid grid;
	ObjectDetector obj;
	Point point1;
	Point point2;
	
	LinkedList<Point> path;
	
	public Search(Navigation nav, UltrasonicSensor us, ColorSensor cs){
		this.us = us;
		this.cs = cs;
		this.nav = nav;
		this.odo = nav.getOdometer();
		this.grid = nav.getGrid();
		
		this.obj = new ObjectDetector(nav, us, cs);
	}

	@Override
	public void search(int distance) {
		// TODO Auto-generated method stub
		
		
		
	}

	@Override
	public int readUSDistance() {
		// TODO Auto-generated method stub
		return this.distance;
	}


}
