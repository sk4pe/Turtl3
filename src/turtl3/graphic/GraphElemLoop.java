package turtl3.graphic;

import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;

/**
 * Graphic element of a loop
 * 
 * @author PKissmer
 *
 */
public class GraphElemLoop extends GraphElem {

	private int[] xCoord = { 18, 7, 7, 18 };
	private int[] yCoord = { -18, -10, 10, 18 };
	private int anchor;
	private int textOffset, smallTextOffset;
	private int id, cycles;

	/**
	 * Creates a new loop element
	 * 
	 * @param x
	 *            x-position
	 * @param y
	 *            y-position
	 * @param type
	 *            type of the loop
	 * @param id
	 *            id of the loop
	 * @param cycles
	 *            loop runs
	 */
	public GraphElemLoop(int x, int y, int type, int id, int cycles) {
		super(x, y);
		anchor = GraphicsLCD.RIGHT;
		textOffset = 3;
		smallTextOffset = -10;
		if (type == 6) {
			textOffset = -3;
			smallTextOffset = 10;
			mirror();
			anchor = GraphicsLCD.LEFT;
		}
		this.id = id;
		this.cycles = cycles;
	}

	/**
	 * Mirrors the coordinates of the loop
	 */
	private void mirror() {
		for (int i = 0; i < 4; i++) {
			xCoord[i] *= -1;
			yCoord[i] *= -1;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graphic.GraphElem#draw()
	 */
	public void draw() {
		for (int i = 0; i < 3; i++) {
			g.drawLine(xCoord[i] + 18 + x, yCoord[i] + 18 + y, xCoord[i + 1] + 18 + x, yCoord[i + 1] + 18 + y);
		}
		g.setColor(255, 255, 255);
		g.drawLine(xCoord[0] + 18 + x, yCoord[0] + 18 + y, xCoord[3] + 18 + x, yCoord[3] + 18 + y);
		g.setColor(0, 0, 0);
		g.drawString("S" + Integer.toString(id), x + 18 + textOffset, y + 5, anchor);
		g.setFont(Font.getSmallFont());
		g.drawString(Integer.toString(cycles), x + 18 + smallTextOffset, y + 25, anchor);
		g.setFont(Font.getDefaultFont());
	}
}
