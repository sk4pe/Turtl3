package turtl3.graphic;

/**
 * Graphic element of a line
 * @author PKissmer
 *
 */
public class GraphElemLine extends GraphElem {

	private int x2, y2;

	/**
	 * Creates a new line element
	 * 
	 * @param x
	 *            x start point
	 * @param y
	 *            y start point
	 * @param x2
	 *            x end point
	 * @param y2
	 *            y end point
	 */
	public GraphElemLine(int x, int y, int x2, int y2) {
		super(x, y);
		this.x2 = x2;
		this.y2 = y2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graphic.GraphElem#draw()
	 */
	public void draw() {
		g.drawLine(x, y, x2, y2);
	}
}
