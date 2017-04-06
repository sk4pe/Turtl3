package turtl3.data;

import lejos.robotics.RegulatedMotor;

/**
 * Data element for the action to drive back
 * 
 * @author PKissmer
 *
 */
public class DataElemBack extends DataElem {

	/**
	 * 
	 */
	public DataElemBack() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#getType()
	 */
	public int getType() {
		return 1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#setPref()
	 */
	public void setPref() {
		speed = DataController.getSpeed();
		distRight = DataController.getDistRight() * -1;
		distLeft = DataController.getDistLeft() * -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#toString()
	 */
	public String toString() {
		return "1";
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
