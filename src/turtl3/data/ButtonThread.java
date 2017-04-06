package turtl3.data;

import lejos.hardware.Button;
import turtl3.MyButtons;

/**
 * Thread to interrupt actions of the robot while running a program by pressing
 * a button.
 * 
 * @author PKissmer
 * 
 */
public class ButtonThread implements Runnable {

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		MyButtons.waitForAnyPress();
		if (Button.ESCAPE.isDown())
			DataController.interrupt();

	}

}
