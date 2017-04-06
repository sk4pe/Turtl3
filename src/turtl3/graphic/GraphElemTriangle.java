package turtl3.graphic;

/**
 * Graphic element of a triangle
 * 
 * @author PKissmer
 *
 */
public class GraphElemTriangle extends GraphElem {

	private int size;

	/**
	 * Creates a new triangle
	 * 
	 * @param x
	 *            x-position
	 * @param y
	 *            y-position
	 * @param size
	 *            size of the triangle
	 */
	public GraphElemTriangle(int x, int y, int size) {
		super(x, y);
		this.size = size;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see graphic.GraphElem#draw()
	 */
	public void draw() {
		g.drawLine(x, y, x, y + size);
		g.drawLine(x, y, x + size, y + (size / 2));
		g.drawLine(x, y + size, x + size, y + (size / 2));
	}
}
