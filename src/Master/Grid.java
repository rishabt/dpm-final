package Master;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.HashMap;

public class Grid {
	private static final int UNSAFE = -5;
	
	// width and height in centimeters
	private int width, height;
	private int rows, columns;
	
	// separation in centimeters
	private static final int SEPARATION = 10;
	private int[][] grid;
			
	// external width and height in centimeters
	public Grid(int width, int height) {
		this.width = width;
		this.height = height;
		this.rows = width / SEPARATION + 1;
		this.columns = height / SEPARATION + 1;
		
		grid = new int[rows][columns];
		
		reportEdges();
	}
	
	
	// OBSTACLE AVOIDANCE


	public boolean unsafe(Point point) {
		return unsafe(row(point.x), col(point.y));
	}
	
	public boolean safe(Point point) {
		return !unsafe(point);
	}
	
	private boolean unsafe(int row, int column) {
		return grid[row][column] < 0;
	}
	
	private boolean safe(int row, int column) {
		return !unsafe(row, column);
	}
	
	public void report(int x, int y) {
		int row = row(x), col = col(y);
		
		int current = grid[row][col];
		int value;
		if (current >= 0) {
			value = -1;
		} else {
			value = current - 1;
		}
		grid[row][col] = value;
	}
	
	
	// SEARCH
	
	
	public void checkin(int x, int y) {
		int row = row(x), col = col(y);
		int current = grid[row][col];
		if (current > UNSAFE) {
			grid[row][col] = current + 1;
		}
	}
	
	public Point wanderFrom(Point point) {
		Point freshest = null;
		int minCount = Integer.MAX_VALUE;
		
		LinkedList<Point> points = safeNeighborsOf(point);
		
		for (Point p : safeNeighborsOf(point)) {
			int count = grid[row(p.x)][col(p.y)];
			if (count < minCount) {
				minCount = count;
				freshest = p;
			}
		}
		return freshest;
	}
	
	public Point getFreshPoint() {
		Point freshest = null;
		int lowest = Integer.MAX_VALUE;
		
		for (int row = 0; row < rows; row ++) {
			for (int col = 0; col < columns; col ++) {
				int count = grid[row][col];
				if (safe(row, col) && count < lowest) {
					lowest = count;
					freshest = pointFor(row, col);
				}
			}
		}
		return freshest;
	}
	
	
	// NAVIGATION (SHORTEST PATH)
	
	
	public LinkedList<Point> depthFirst(Point a, Point b) {
		LinkedList<Point> path = null;
		if (a.equals(b)) {
			path = new LinkedList<Point>();
			path.add(a);	
		} else {
			for (Point point : sort(safeNeighborsOf(a), b)) {
				path = depthFirst(point, b);
				if (path != null) {
					path.add(0, a);
					break;
				}
			}
		}		
		return path;
	}
		
	// sort points by min distance to destination
	private LinkedList<Point> sort(LinkedList<Point> points, Point destination) {
		LinkedList<Point> sorted = new LinkedList<Point>();	
		while (!points.isEmpty()) {
			Point min = closest(points, destination);
			points.remove(min);
			sorted.add(min);
		}
		return sorted;
	}
	
	/*
	*  closest([points], point) returns the closest point in distance to the
	*  destination
	*/
	private Point closest(LinkedList<Point> points, Point destination) {
		Point min = null;
		int minDistance = Integer.MAX_VALUE;
		for (Point point : points) {
			int distance = point.distanceTo(destination);
			if (distance < minDistance) {
				minDistance = distance;
				min = point;
			}
		}
		return min;
	}
	
	// Djikstra's algorithm
	public LinkedList<Point> breadthFirst(Point a, Point b) {
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
				
				int distance = distances.get(current).intValue() + current.distanceTo(neighbor);
								
				if (newPoint || distance < prevDistance.intValue()) {
					predecessors.put(neighbor, current);
					distances.put(neighbor, distance);
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

	
	// HELPERS
	
	
	// x -> row
	private int row(int x) {
		return Math.min(rows - 1, Math.max(0, sep(x)));		
	}
	
	// y -> column
	private int col(int y) {
		return Math.min(columns - 1, Math.max(0, sep(y)));
	}
	
	// x -> row, y -> column (no checks)
	private int sep(int coordinate) {
		return coordinate / SEPARATION;
	}
	
	// row -> x, column -> y
	private int coord(int separation) {
		return separation * SEPARATION;
	}
	
	// (row, column) -> (x, y)
	private Point pointFor(int row, int column) {
		return new Point(coord(row), coord(column));
	}
	
	// prints large grid. not suitable for lejos LCD
	public String toString() {
		String str = "-- Grid width=" + width + " height=" + height;
		str += " rows=" + rows + " columns=" + columns + "\n";		
		
		for (int col = 0; col < columns; col++) {
			for (int row = 0; row < rows; row++) {
				if(unsafe(row, col)) str += "*";
				else str += grid[row][col];
				str += " ";
			}
			str = str.trim() + "\n";
		}
		return str;
	}

	// all edge points are "unsafe"
	private void reportEdges() {
		for (int col = 0; col < columns; col ++) {
			grid[0][col] = UNSAFE;
			grid[rows - 1][col] = UNSAFE;
		}
		
		for (int row = 1; row < rows - 1; row ++) {
			grid[row][0] = UNSAFE;
			grid[row][columns - 1] = UNSAFE;
		}
	}
}
