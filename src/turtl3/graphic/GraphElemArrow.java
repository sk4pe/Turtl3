package turtl3.graphic;

/**
 * Graphic element of an arrow
 * @author PKissmer
 *
 */
public class GraphElemArrow extends GraphElem {

	// Coordinates to draw the arrow
	private int[] xCoord = { 5, -5, -5, -15, -15, 0, 15, 15, 5, 5 };
	private int[] yCoord = { -15, -15, 5, -2, 8, 15, 8, -2, 5, -15 };

	private int[] xCoord2 = { 14, 4, 4, -5, 2, -8, -15, -8, 2, -5, 14 };
	private int[] yCoord2 = { -15, -15, -5, -5, -15, -15, 0, 15, 15, 5, 5 };

	/**
	 * Generates a new arrow
	 * 
	 * @param x
	 *            x-position
	 * @param y
	 *            y-position
	 * @param type
	 *            type of the arrow (1: back, 2: left, 3: forward, 4: right)
	 */
	public GraphElemArrow(int x, int y, int type) {
		super(x, y);

		// Rotate or mirror the coordinates to get the correct arrow
		switch (type) {
		case 2:
			xCoord = xCoord2;
			yCoord = yCoord2;
			mirrorY();
			break;
		case 3:
			rotLeft();
			rotLeft();
			break;
		case 4:
			xCoord = xCoord2;
			yCoord = yCoord2;
			mirrorX();
			mirrorY();
			break;
		}
	}

	/**
	 * Mirrors the coordinates at the x-axis
	 */
	private void mirrorX() {
		for (int i = 0; i < xCoord.length; i++) {
			xCoord[i] = -xCoord[i];
		}
	}

	/**
	 * Mirrors the coordinates at the y-axis
	 */
	private void mirrorY() {
		for (int i = 0; i < yCoord.length; i++) {
			yCoord[i] = -yCoord[i];
		}
	}

	/**
	 * Rotates the coordinates to the left
	 */
	private void rotLeft() {
		for (int i = 0; i < xCoord.length; i++) {
			int temp = xCoord[i];
			xCoord[i] = yCoord[i] * -1;
			yCoord[i] = temp;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graphic.GraphElem#draw()
	 */
	public void draw() {
		for (int i = 0; i < xCoord.length - 1; i++) {
			g.drawLine(xCoord[i] + 18 + x, yCoord[i] + 18 + y, xCoord[i + 1] + 18 + x, yCoord[i + 1] + 18 + y);
		}
		g.drawLine(xCoord[xCoord.length - 1] + 18 + x, yCoord[yCoord.length - 1] + 18 + y, xCoord[0] + 18 + x,
				yCoord[0] + 18 + y);
	}
}
