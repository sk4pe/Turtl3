package turtl3.data;

import lejos.robotics.RegulatedMotor;

/**
 * Data element for the action a left turn
 * 
 * @author PKissmer
 *
 */
public class DataElemLeft extends DataElem {

	/**
	 * 
	 */
	public DataElemLeft() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#getType()
	 */
	public int getType() {
		return 2;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#setPref()
	 */
	public void setPref() {
		speed = DataController.getRotSpeed();
		distLeft = DataController.getRotDistLeft() * -1;
		distRight = DataController.getRotDistRight();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#toString()
	 */
	public String toString() {
		return "2";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#run(lejos.robotics.RegulatedMotor,
	 * lejos.robotics.RegulatedMotor)
	 */
	public void run(RegulatedMotor left, RegulatedMotor right) {
		final int corr = 100;		
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
