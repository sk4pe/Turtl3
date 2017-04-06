package turtl3.data;

import lejos.robotics.RegulatedMotor;

/**
 * Abstract class for data elements
 * 
 * @author PKissmer
 *
 */
public abstract class DataElem {

	protected int speed;
	protected int distRight, distLeft;
	

	/**
	 * 
	 */
	public DataElem() {
	}

	/**
	 * Returns the type of the data element
	 * 
	 * @return type
	 */
	public abstract int getType();

	/**
	 * Executes the action of the data element
	 * 
	 * @param left
	 *            motor for the left wheel
	 * @param right
	 *            motor for the right wheel
	 */
	public abstract void run(RegulatedMotor left, RegulatedMotor right);

	/**
	 * Sets the preferences (speed, distance) for the data element
	 */
	public abstract void setPref();

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();

}
