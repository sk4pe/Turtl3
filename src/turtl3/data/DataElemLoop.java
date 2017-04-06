package turtl3.data;

import lejos.robotics.RegulatedMotor;

/**
 * Data element for loops
 * 
 * @author PKissmer
 *
 */
public class DataElemLoop extends DataElem {

	private boolean end;
	private int loopId;
	private int runs;
	private int counter = -1;

	/**
	 * Constructor
	 * 
	 * @param end
	 *            end of a loop
	 * @param id
	 *            id of the loop
	 * @param runs
	 *            loop runs
	 */
	public DataElemLoop(boolean end, int id, int runs) {
		this.end = end;
		loopId = id;
		this.runs = runs;
	}

	/**
	 * Constructor
	 * 
	 * @param end
	 *            end of the loop
	 * @param id
	 *            id of the loop
	 * @param runs
	 *            loop runs
	 * @param counter
	 *            counterpart
	 */
	public DataElemLoop(boolean end, int id, int runs, int counter) {
		this.end = end;
		loopId = id;
		this.runs = runs;
		this.counter = counter;
	}

	/**
	 * Sets the counterpart to the loop
	 * 
	 * @param counter
	 *            index of the counterpart
	 */
	public void setCounter(int counter) {
		this.counter = counter;
	}

	/**
	 * Returns the index of the counterpart
	 * 
	 * @return index
	 */
	public int getCounter() {
		return counter;
	}

	/**
	 * Returns the loop runs
	 * 
	 * @return runs
	 */
	public int getRuns() {
		return runs;
	}

	/**
	 * Returns the ID of the loop
	 * 
	 * @return id
	 */
	public int getId() {
		return loopId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#getType()
	 */
	public int getType() {
		return (end ? 6 : 5);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#setPref()
	 */
	public void setPref() {
		speed = 0;
		distLeft = 0;
		distRight = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#toString()
	 */
	public String toString() {
		String ret = "5";
		String highRuns = "0";
		if (end) {
			ret = "6";
			return ret;
		}
		if (runs >= 10)
			highRuns = "1";
		ret = ret + highRuns + runs;
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see data.DataElem#run(lejos.robotics.RegulatedMotor,
	 * lejos.robotics.RegulatedMotor)
	 */
	public void run(RegulatedMotor left, RegulatedMotor right) {
	}
}
