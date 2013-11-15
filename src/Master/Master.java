package Master;

import lejos.nxt.*;

import lejos.nxt.Button;

public class Master{
	
	public static void main(String[] args){
		Button.ESCAPE.addButtonListener(new Support.ExitListener());
		
		Button.waitForAnyPress();
		
		TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
		Odometer odo = new Odometer(robot, true);
		Navigation nav = new Navigation(odo);
		LCDInfo lcd = new LCDInfo(odo);
		
		Grid grid = new Grid(4 * 30, 4 * 30);
		
		
		Point p = grid.getFreshPoint();
		LCD.clear();
		LCD.drawString(p.toString(), 0, 5);
		
		Button.waitForAnyPress();
		
	}
	
}