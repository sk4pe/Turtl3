package turtl3.graphic;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;

/**
 * Abstract class for grahpic elements
 * 
 * @author PKissmer
 *
 */
public abstract class GraphElem {

	protected GraphicsLCD g;
	protected int x, y;

	/**
	 * Sets the position of the element and saves the LCD
	 * 
	 * @param x
	 *            x-position
	 * @param y
	 *            y-position
	 */
	public GraphElem(int x, int y) {
		g = LocalEV3.get().getGraphicsLCD();
		this.x = x;
		this.y = y;
	}

	/**
	 * Sets the position of the element
	 * 
	 * @param x
	 *            x-position
	 * @param y
	 *            y-position
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Draws the current element on the display
	 */
	public abstract void draw();

}
