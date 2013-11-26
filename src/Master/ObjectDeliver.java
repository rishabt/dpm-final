package Master;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class ObjectDeliver {
	
	private static UltrasonicSensor us = new UltrasonicSensor(SensorPort.S1);
	private static Grid grid = new Grid(30 * 8, 30 * 8);
	private static TwoWheeledRobot robot = new TwoWheeledRobot(Motor.A, Motor.B);
	private static Odometer odo = new Odometer(robot, grid);
	private static Navigation nav =  new Navigation(odo, us);
	
	
	public static void deliver(){
		
	}
}
