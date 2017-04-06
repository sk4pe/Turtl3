package turtl3.graphic;

/**
 * Graphic element of a border
 * @author PKissmer
 *
 */
public class GraphElemBorder extends GraphElem {

	private int height, width;

	/**
	 * Creates a new border element
	 * 
	 * @param x
	 *            x-position
	 * @param y
	 *            y-position
	 * @param height
	 *            height of the border
	 * @param width
	 *            width of the border
	 */
	public GraphElemBorder(int x, int y, int height, int width) {
		super(x, y);
		this.height = height;
		this.width = width;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graphic.GraphElem#draw()
	 */
	public void draw() {
		g.drawLine(x, y, x + width, y);
		g.drawLine(x, y, x, y + height);
		g.drawLine(x + width, y, x + width, y + height);
		g.drawLine(x, y + height, x + width, y + height);
	}
}
