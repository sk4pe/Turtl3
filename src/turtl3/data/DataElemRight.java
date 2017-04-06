package turtl3.data;

import lejos.robotics.RegulatedMotor;

/**
 * Data element for the action to drive a right turn
 * 
 * @author PKissmer
 *
 */
public class DataElemRight extends DataElem {

	/**
	 * 
	 */
	public DataElemRight() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#getType()
	 */
	public int getType() {
		return 4;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#setPref()
	 */
	public void setPref() {
		speed = DataController.getRotSpeed();
		distLeft = DataController.getRotDistLeft();
		distRight = DataController.getRotDistRight() * -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#toString()
	 */
	public String toString() {
		return "4";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#run(lejos.robotics.RegulatedMotor,
	 * lejos.robotics.RegulatedMotor)
	 */
	public void run(RegulatedMotor left, RegulatedMotor right) {
		final int corr= 90;
		left.setSpeed(speed);
		right.setSpeed(speed);
		
		left.rotate(-corr,true);
		right.rotate(-corr);
		
		left.rotate(distLeft, true);
		right.rotate(distRight);
		
		left.rotate(corr,true);
		right.rotate(corr);
	}
}
