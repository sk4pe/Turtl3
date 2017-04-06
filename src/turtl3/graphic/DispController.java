package turtl3.graphic;

import java.util.LinkedList;
import java.util.List;

import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.GraphicsLCD;
import turtl3.data.DataController;

/**
 * Controls the visual representation of the data elements and the menu
 * elements. This includes adding and removing graphic elements and redrawing
 * the display. The class further can change between different views of the
 * programmed elements
 * 
 * @author PKissmer
 *
 */
public class DispController {

	// Lists to store the elements to draw on the display
	private List<GraphElem> menuElemList = new LinkedList<GraphElem>();
	private List<GraphElem> progElemList = new LinkedList<GraphElem>();

	private GraphicsLCD g;
	private DataController d;

	// Positions of the program elements (small and large view)
	private static final int[] XPOSSMALL = { 0, 45, 90, 135, 0, 45, 90, 135 };
	private static final int[] YPOSSMALL = { 35, 35, 35, 35, 80, 80, 80, 80 };

	private static final int[] XPOSLARGE = { 0, 45, 90, 135, 0, 45, 90, 135, 0, 45, 90, 135 };
	private static final int[] YPOSLARGE = { 5, 5, 5, 5, 46, 46, 46, 46, 87, 87, 87, 87 };

	private int[] xPos, yPos;
	private int viewLeft, viewRight, maxElem;
	private boolean smallView = true;

	/**
	 * Generates a new DispController and sets the committed DataController
	 * 
	 * @param prog
	 *            DataController
	 */
	public DispController(DataController prog) {
		g = LocalEV3.get().getGraphicsLCD();
		d = prog;
		viewLeft = 0;
		viewRight = 7;
		maxElem = 8;
	}

	/**
	 * Changes the view to the large view
	 */
	public void setLargeView() {
		smallView = false;
		maxElem = 12;
		setViewToLast();
	}

	/**
	 * Changes the view to the small view
	 */
	public void setSmallView() {
		smallView = true;
		maxElem = 8;
		setViewToFirst();
	}

	/**
	 * Sets the focus to the committed element
	 * 
	 * @param i
	 *            index of the element
	 */
	public void setFocus(int i) {
		viewLeft = i;
		viewRight = maxElem - 1 + i;
		if (viewRight >= d.getSize())
			viewRight = d.getSize() - 1;
		this.clearProgram();
		this.drawProgram();
		drawFocus(0);
	}

	/**
	 * Draws the program to the display
	 */
	public void drawProgram() {
		int position = 0;
		while (d.getData(position + viewLeft) != -1 && position + viewLeft <= viewRight) {
			drawRect(position, d.getData(position + viewLeft));
			position++;
		}
		if (position < maxElem) {
			drawRect(position, 0);
		}
	}

	/**
	 * Sets the view to the last element
	 */
	public void setViewToLast() {
		viewLeft = d.getSize() - (maxElem - 1);
		if (viewLeft < 0)
			viewLeft = 0;
		viewRight = d.getSize() - 1;
		if (viewRight < 0)
			viewRight = 0;
		this.clearProgram();
		drawProgram();
	}

	/**
	 * Sets the view to the first element
	 */
	public void setViewToFirst() {
		viewLeft = 0;
		viewRight = maxElem - 1;
		this.clearProgram();
		drawProgram();
	}

	/**
	 * Deletes the visual elements of the program
	 */
	public void clearProgram() {
		progElemList.clear();
		this.refresh();
	}

	/**
	 * Adds a GraphElem to the display
	 * 
	 * @param e
	 *            GraphElem
	 */
	public void addElem(GraphElem e) {
		menuElemList.add(e);
		this.refresh();
	}

	/**
	 * Removes a GraphElem from the display
	 * 
	 * @param e
	 *            GraphElem
	 */
	public void remove(GraphElem e) {
		menuElemList.remove(e);
		this.refresh();
	}

	/**
	 * Refreshes the display and writes every item saved in the element lists to
	 * the display
	 */
	public void refresh() {
		g.clear();
		if (smallView) {
			if (!menuElemList.isEmpty()) {
				for (GraphElem elem : menuElemList) {
					elem.draw();
				}
			}
		}
		if (!progElemList.isEmpty()) {
			for (GraphElem elem : progElemList) {
				elem.draw();
			}
		}
	}

	/**
	 * Deletes all items on the display
	 */
	public void clear() {
		menuElemList.clear();
		progElemList.clear();
		g.clear();
	}

	/**
	 * Draws a border around the focused element
	 * 
	 * @param i
	 *            index of the element
	 */
	private void drawFocus(int i) {
		if (smallView) {
			xPos = XPOSSMALL;
			yPos = YPOSSMALL;
		} else {
			xPos = XPOSLARGE;
			yPos = YPOSLARGE;
		}
		g.drawRect(xPos[i] - 1, yPos[i] - 1, 38, 38);
	}

	/**
	 * Draws one of the commands to the committed position on the display
	 * 
	 * @param position
	 *            index of the position on the screen
	 * @param type
	 *            type of the command
	 */
	private void drawRect(int position, int type) {
		if (smallView) {
			xPos = XPOSSMALL;
			yPos = YPOSSMALL;
		} else {
			xPos = XPOSLARGE;
			yPos = YPOSLARGE;
		}
		// Adding a new empty border
		this.addProgElem(new GraphElemBorder(xPos[position], yPos[position], 36, 36));

		// Adding a command element
		switch (type) {
		case 1:
			this.addProgElem(new GraphElemArrow(xPos[position], yPos[position], type));
			break;
		case 2:
			this.addProgElem(new GraphElemArrow(xPos[position], yPos[position], type));
			break;
		case 3:
			this.addProgElem(new GraphElemArrow(xPos[position], yPos[position], type));
			break;
		case 4:
			this.addProgElem(new GraphElemArrow(xPos[position], yPos[position], type));
			break;
		case 5:
			this.addProgElem(new GraphElemLoop(xPos[position], yPos[position], type, d.getId(position + viewLeft),
					d.getCycles(position + viewLeft)));
			break;
		case 6:
			this.addProgElem(new GraphElemLoop(xPos[position], yPos[position], type, d.getId(position + viewLeft),
					d.getCycles(position + viewLeft)));
			break;
		}
	}

	/**
	 * Adds a program element to the list of program elements
	 * 
	 * @param e
	 *            element to add
	 */
	private void addProgElem(GraphElem e) {
		progElemList.add(e);
		this.refresh();
	}
}
