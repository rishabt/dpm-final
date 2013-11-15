package Master;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.HashMap;

import lejos.nxt.*;

public class Grid {
		
	// width and height in centimeters
	private int width, height;
	private int rows, columns;
	
	// separation in centimeters
	private static final int SEPARATION = 10;
	private HashSet<Point> safePoints = new HashSet<Point>();
	private HashMap<Point, Integer> checkins = new HashMap<Point, Integer>();
		
	// external width and height in centimeters
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		this.rows = width / SEPARATION + 1;
		this.columns = height / SEPARATION + 1;
		
		for (int row = 1; row < rows - 1; row++) {
			for (int col = 1; col < columns - 1; col++) {
				safePoints.add(new Point(row * SEPARATION, col * SEPARATION));
			}
		}
	}
	
	/*
	* pointFor(7, 7)  ->  (10, 10)
	* pointFor(2, 7)  ->  (0, 10)
	*/
	public Point pointFor(int x, int y) {
		double separation = SEPARATION * 1.0;
		int xRound = ((int) Math.round(x / separation)) * SEPARATION;
		int yRound = ((int) Math.round(y / separation)) * SEPARATION;
		return new Point(xRound, yRound);
	}
	
	public boolean safe(Point point) {
		return safePoints.contains(point);
	}
	
	public boolean unsafe(Point point) {
		return !safe(point);
	}
	
	public boolean unsafe(int x, int y) {
		return unsafe(pointFor(x, y));
	}
	
	public void report(int x, int y) {
		safePoints.remove(pointFor(x, y));
	}
	
	public LinkedList<Point> getDirections(Point a, Point b) {
		return shortestPath(a, b);
	}
	
	public void checkin(int x, int y) {
		Point point = pointFor(x, y);
		Integer prevCount = checkins.get(point);
		int count = prevCount == null ? 0 : prevCount.intValue();
		checkins.put(point, count + 1);
	}
	
	public Point getFreshPoint() {
		Point freshest = null;
		int lowest = Integer.MAX_VALUE;
		for (Object object : safePoints.toArray()) {
			Point point = (Point) object;
			Integer intObject = checkins.get(point);
			int count = intObject == null ? 0 : intObject.intValue();			
			if (count < lowest) {
				freshest = point;
				lowest = count;
			}
		}
		return freshest;
	}

	/*
	* shortestPath(a, b) uses Djikstra's algorithm to find the 
	* shortest path between point a and b
	*/
	private LinkedList<Point> shortestPath(Point a, Point b) {
		HashMap<Point, Point> predecessors = new HashMap<Point, Point>();
		HashMap<Point, Integer> distances = new HashMap<Point, Integer>();
		Queue<Point> queue = new Queue<Point>();
		distances.put(a, 0);
		
		Point current = a;
		while (current != null && !current.equals(b)) {
			for (Point neighbor : safeNeighborsOf(current)) {				
				Integer prevDistance = distances.get(neighbor);
				boolean newPoint = prevDistance == null;
				
				if (newPoint) queue.push(neighbor);
				
				int newDistance = distances.get(current).intValue() + current.distanceTo(neighbor);
								
				if (newPoint || newDistance < prevDistance.intValue()) {
					predecessors.put(neighbor, current);
					distances.put(neighbor, newDistance);
				}
			}
			current = (Point) queue.pop();
		}
		return recreatePath(predecessors, current);
	}
	
	/*
	* reacreatePath is a helper method for shortestPath. It follows
	* the chain of predecessors and outputs the shortest path.
	*/
	private LinkedList<Point> recreatePath(HashMap<Point, Point> predecessors, Point last) {
		Point head = last;
		LinkedList<Point> path = new LinkedList<Point>();
		while (head != null) {
			path.add(0, head);
			head = predecessors.get(head);
		}
		return path;
	}
	
	/*
	* safeNeighborsOf(p) returns a list of neighbors in the
	* safe points hashset
	*/
	private LinkedList<Point> safeNeighborsOf(Point point) {
		LinkedList<Point> safeNeighbors = new LinkedList<Point>();
		for (Point neighbor : neighborsOf(point)) {
			if (safe(neighbor)) safeNeighbors.add(neighbor);
		}
		return safeNeighbors;
	}
	
	/*
	* neighborsOf(p) returns all points around it:
	* 
	*    * * *
	*    * p *    ->   [ 8 point array]
	*    * * *
	*/
	private Point[] neighborsOf(Point point) {
		int x = point.x, y = point.y;
			
		return new Point[] {
			new Point(x - SEPARATION, y - SEPARATION),
			new Point(x, y - SEPARATION),
			new Point(x + SEPARATION, y - SEPARATION),
			new Point(x - SEPARATION, y),
			new Point(x + SEPARATION, y),
			new Point(x - SEPARATION, y + SEPARATION),
			new Point(x, y + SEPARATION),
			new Point(x + SEPARATION, y + SEPARATION)
		};
	}
	
	public String toString() {		
		boolean[][] grid = new boolean[columns][rows];
		
		String str = "-- Grid width=" + width + " height=" + height;
		str += " rows=" + rows + " columns=" + columns + "\n";
		for (int col = 0; col < columns; col++) {
			for (int row = 0; row < rows; row++) {
				if (unsafe(row * SEPARATION, col * SEPARATION)) str += "*";
				else str += " ";
				str += " ";
			}
			str = str.trim() + "\n";
		}
		return str;
	}
}