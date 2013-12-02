package Master;

import lejos.util.Timer;
import lejos.util.TimerListener;

/**
 * 
 * @author Rishabh
 *
 */
public class Odometer implements TimerListener {
        public static final int DEFAULT_PERIOD = 25;
        private TwoWheeledRobot robot;
        private Grid grid;
        private Timer odometerTimer;
        // position data
        private Object lock;
        private double x, y, theta;
        private double [] oldDH, dDH;
        
        /**
         * 
         * @param robot
         * @param grid
         * @param period
         */
        public Odometer(TwoWheeledRobot robot, Grid grid, int period) {
                // initialise variables
                this.robot = robot;
                this.grid = grid;
                odometerTimer = new Timer(period, this);
                x = 0.0;
                y = 0.0;
                theta = 0.0;
                oldDH = new double [2];
                dDH = new double [2];
                lock = new Object();
                
                // start the odometer immediately
                odometerTimer.start();                        
        }
        
        /**
         * 
         * @param robot
         * @param grid
         */
        public Odometer(TwoWheeledRobot robot, Grid grid) {
                this(robot, grid, DEFAULT_PERIOD);
        }
        
        /**
         * 
         */
        public void timedOut() {
                robot.getDisplacementAndHeading(dDH);
                dDH[0] -= oldDH[0];
                dDH[1] -= oldDH[1];
                
                // update the position in a critical region
                synchronized (lock) {
                        theta += dDH[1];
                        theta = fixDegAngle(theta);
                        
                        x += dDH[0] * Math.sin(Math.toRadians(theta));
                        y += dDH[0] * Math.cos(Math.toRadians(theta));
                }
                
                grid.checkin((int) x, (int) y);
                
                oldDH[0] += dDH[0];
                oldDH[1] += dDH[1];
        }
        
        /**
         * 
         * @return
         */
        public TwoWheeledRobot getRobot() {
                return robot;
        }
        
        /**
         * 
         * @return
         */
        public Grid getGrid() {
                return grid;
        }
        
        /**
         * 
         * @param pos
         */
        // accessors
        public void getPosition(double [] pos) {
                synchronized (lock) {
                        pos[0] = x;
                        pos[1] = y;
                        pos[2] = theta;
                }
        }
        
        /**
         * 
         * @param theta
         */
        public void setTheta(double theta){
                this.theta = fixDegAngle(theta);
        }
        
        /**
         * 
         * @param Y
         */
        public void setY(double Y){
                this.y = Y;
        }
        
        /**
         * 
         * @param X
         */
        public void setX(double X){
                this.x = X;
        }
        
        /**
         * 
         * @return
         */
        public TwoWheeledRobot getTwoWheeledRobot() {
                return robot;
        }
        
        /**
         * 
         * @param pos
         * @param update
         */
        // mutators
        public void setPosition(double [] pos, boolean [] update) {
                synchronized (lock) {
                        if (update[0]) x = pos[0];
                        if (update[1]) y = pos[1];
                        if (update[2]) theta = fixDegAngle(pos[2]);
                }
        }
        
        /**
         * 
         * @param angle
         * @return
         */
        // static 'helper' methods
        public static double fixDegAngle(double angle) {                
                if (angle < 0.0)
                        angle = 360.0 + (angle % 360.0);
                
                return angle % 360.0;
        }
        
        /**
         * 
         * @param a
         * @param b
         * @return
         */
        
        public static double minimumAngleFromTo(double a, double b) {
                double d = fixDegAngle(b - a);
                
                if (d < 180.0)
                        return d;
                else
                        return d - 360.0;
        }
        
        /**
         * 
         * @return
         */
        public double getX() {
                double result;

                synchronized (lock) {
                        result = x;
                }

                return result;
        }
        
        /**
         * 
         * @return
         */
        public double getY() {
                double result;

                synchronized (lock) {
                        result = y;
                }

                return result;
        }
        
        /**
         * 
         * @return
         */

        public double getTheta() {
                double result;

                synchronized (lock) {
                        result = theta;
                }

                return result;
        }
}