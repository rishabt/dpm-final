package Master;

import lejos.nxt.LCD;
import lejos.util.Timer;
import lejos.util.TimerListener;

public class LCDInfo implements TimerListener{
	public static final int LCD_REFRESH = 100;
	private Odometer odo;
	private Timer lcdTimer;
	
	// arrays for displaying data
	private double [] pos;
	
	public LCDInfo(Odometer odo) {
		this.odo = odo;
		this.lcdTimer = new Timer(LCD_REFRESH, this);
		
		// initialise the arrays for displaying data
		pos = new double [3];
		
		// start the timer
		lcdTimer.start();
	}
	
	public void timedOut() { 
		odo.getPosition(pos);
		LCD.clear();
		LCD.drawString("X: ", 0, 0);
		LCD.drawString("Y: ", 6, 0);
		LCD.drawString("H: ", 12, 0);
		LCD.drawInt((int)(pos[0]), 3, 0);
		LCD.drawInt((int)(pos[1]), 9, 0);
		LCD.drawInt((int) (pos[2]), 14, 0);
	}
}
