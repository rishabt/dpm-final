package Master;

public class Point {

	public int x, y;

	/**
	 * 
	 * @param x
	 * @param y
	 */
	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/**
	 * 
	 * @param point
	 * @return
	 */
	public int distanceTo(Point point) {
		int dx = point.x - x, dy = point.y - y;
		double sqrt = Math.sqrt(dx * dx + dy * dy);
		return (int) Math.round(sqrt);
	}

	@Override
	/**
	 * 
	 */
	public String toString() {
		return "(" + x + "," + y + ")";
	}
		
	/**
	 * 
	 */
	@Override
	public int hashCode() {
	    int hash = 7;
	    hash = hash * 71 + x;
	    hash = hash * 71 + y;
	    return hash;
	}
	
	@Override
	/**
	 * 
	 */
	public boolean equals(Object o) {
		if (o instanceof Point) {
			Point point = (Point) o;
			return x == point.x && y == point.y;
		} else {
			return false;
		}
	}

}