package turtl3.data;

import lejos.robotics.RegulatedMotor;

/**
 * Data element for the action to drive forward
 * 
 * @author PKissmer
 *
 */
public class DataElemForward extends DataElem {

	/**
	 * 
	 */
	public DataElemForward() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#getType()
	 */
	public int getType() {
		return 3;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#setPref()
	 */
	public void setPref() {
		speed = DataController.getSpeed();
		distLeft = DataController.getDistLeft();
		distRight = DataController.getDistRight();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#toString()
	 */
	public String toString() {
		return "3";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#run(lejos.robotics.RegulatedMotor,
	 * lejos.robotics.RegulatedMotor)
	 */
	public void run(RegulatedMotor left, RegulatedMotor right) {
		left.setSpeed(speed);
		right.setSpeed(speed);
		left.rotate(distLeft, true);
		right.rotate(distRight);
	}
}
