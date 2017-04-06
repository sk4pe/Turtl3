package turtl3.graphic;

import lejos.hardware.lcd.Font;

/**
 * Graphic element of a text
 * 
 * @author PKissmer
 *
 */
public class GraphElemText extends GraphElem {

	private String text;
	private Font f;

	/**
	 * Creates a new text element
	 * 
	 * @param x
	 *            x-position
	 * @param y
	 *            y-position
	 * @param text
	 *            text to draw on the display
	 */
	public GraphElemText(int x, int y, String text) {
		super(x, y);
		this.text = text;
		this.f = Font.getDefaultFont();
	}

	/**
	 * Creates a new text element with the committed font
	 * 
	 * @param x
	 *            x-position
	 * @param y
	 *            y-position
	 * @param text
	 *            text to draw on the display
	 * @param f
	 *            font of the text
	 */
	public GraphElemText(int x, int y, String text, Font f) {
		super(x, y);
		this.text = text;
		this.f = f;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graphic.GraphElem#draw()
	 */
	public void draw() {
		g.setFont(f);
		g.drawString(text, x, y, 0);
		g.setFont(Font.getDefaultFont());
	}
}
