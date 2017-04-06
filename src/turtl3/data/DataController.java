package turtl3.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Stack;

import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.robotics.RegulatedMotor;
import lejos.utility.Delay;
import turtl3.Notification;
import turtl3.graphic.DispController;

/**
 * Controls the programmed data elements. This includes adding and removing
 * commands or running the program. The class further writes programs to and
 * loads programs from the file-system.
 * 
 * @author PKissmer
 *
 */

public class DataController {

	private List<DataElem> data;
	private int loopCounter;

	private Stack<Integer> openLoopStack;
	private RegulatedMotor left = new EV3LargeRegulatedMotor(MotorPort.B);
	private RegulatedMotor right = new EV3LargeRegulatedMotor(MotorPort.C);
	private DispController dC;
	private static boolean interrupt = false;

	private static int speed, distLeft, distRight, rotDistLeft, rotDistRight, rotSpeed;

	/**
	 * Generates a new DataController with an empty data list.
	 */
	public DataController() {
		data = new LinkedList<DataElem>();
		loopCounter = 0;
		openLoopStack = new Stack<Integer>();
		speed = distLeft = distRight = rotDistLeft = rotDistRight = rotSpeed = 0;
	}

	/**
	 * Adds a DisplayController to gain control to different methods to
	 * manipulate the display.
	 * 
	 * @param d
	 *            the DisplayController
	 */
	public void addDispController(DispController d) {
		dC = d;
	}

	/**
	 * Deletes the last command of the program
	 */
	public void undo() {
		// Check if program is empty
		if (data.size() != 0) {
			if (data.get(data.size() - 1).getType() == 5) {
				// If last command is a start of a loop, remove it from the loop
				// stack
				openLoopStack.pop();
				loopCounter--;
				data.remove(data.size() - 1);
			} else if (data.get(data.size() - 1).getType() == 6) {
				// If the last command is an end of a loop, push the index of
				// the open loop to the loop stack
				int counter = ((DataElemLoop) data.get(data.size() - 1)).getCounter();
				openLoopStack.push(counter);
				((DataElemLoop) data.get(counter)).setCounter(-1);
				data.remove(data.size() - 1);
			} else
				data.remove(data.size() - 1);
		}
	}

	/**
	 * Writes the current program to the committed file
	 * 
	 * @param f
	 *            the file
	 */
	public void writeProgramToFile(File f) {
		if (data.size() != 0) {
			try {
				Writer writer = new FileWriter(f);
				for (DataElem elem : data) {
					writer.write(elem.toString());
				}
				writer.flush();
				writer.close();
			} catch (IOException e) {
				Notification.generateNotification("Err: Write File");

			}
		}
	}

	/**
	 * Loads a program from the committed file
	 * 
	 * @param f
	 *            the file
	 */
	public void loadProgramFromFile(File f) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(f));
			String line;
			deleteProgram();
			while ((line = reader.readLine()) != null) {
				generateItem(line);
			}
			reader.close();
		} catch (IOException e) {
			Notification.generateNotification("Err: Load File");

		}
	}

	/**
	 * Adds a start of a loop to the program
	 * 
	 * @param i
	 *            type of the element (5 for a start)
	 * @param cycles
	 *            number of runs
	 * @return true if adding was successful
	 */
	public boolean add(int i, int cycles) {
		if (i == 5) {
			data.add(new DataElemLoop(false, ++loopCounter, cycles));
			openLoopStack.push(data.size() - 1);
			return true;
		}
		return false;
	}

	/**
	 * Adds a new element to the program (1: back, 2: left, 3: forward, 4:
	 * right, 5: loop start, 6: loop end)
	 * 
	 * @param i
	 *            element type
	 * @return true if adding was successful
	 */
	public boolean add(int i) {
		switch (i) {
		case 1:
			data.add(new DataElemBack());
			return true;
		case 2:
			data.add(new DataElemLeft());
			return true;
		case 3:
			data.add(new DataElemForward());
			return true;
		case 4:
			data.add(new DataElemRight());
			return true;
		case 5:
			data.add(new DataElemLoop(false, ++loopCounter, 1));
			openLoopStack.push(data.size() - 1);
			break;
		case 6:
			if (openLoopStack.size() == 0)
				return false;
			else {
				// Adds a loop end and sets the different values in the element
				// and the corresponding loop start
				data.add(new DataElemLoop(true, ((DataElemLoop) data.get(openLoopStack.peek())).getId(),
						((DataElemLoop) data.get(openLoopStack.peek())).getRuns(), openLoopStack.peek()));
				DataElemLoop l = (DataElemLoop) data.get(openLoopStack.pop());
				l.setCounter(data.size() - 1);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the loop runs an element
	 * 
	 * @param i
	 *            position of the element
	 * @return -1 if it is not a loop element or number of loop if it is a loop
	 *         element
	 */
	public int getCycles(int i) {
		if (data.get(i) instanceof DataElemLoop) {
			return ((DataElemLoop) data.get(i)).getRuns();
		} else {
			return -1;
		}
	}

	/**
	 * Returns the ID of an element
	 * 
	 * @param i
	 *            position of the element
	 * @return ID of the element
	 */
	public int getId(int i) {
		return ((DataElemLoop) data.get(i)).getId();
	}

	/**
	 * Deletes the current program
	 */
	public void deleteProgram() {
		data.clear();
		loopCounter = 0;
		openLoopStack.clear();
	}

	/**
	 * Returns the type of an element
	 * 
	 * @param i
	 *            position of the element
	 * @return 1: back, 2: left, 3: forward, 4: right, 5: loop start, 6: loop
	 *         end
	 */
	public int getData(int i) {
		if (i < data.size())
			return data.get(i).getType();
		else
			return -1;
	}

	/**
	 * Returns the amount of commands in the program
	 * 
	 * @return number of commands
	 */
	public int getSize() {
		return data.size();
	}

	/**
	 * Checks for open loops
	 * 
	 * @return true if all loops are closed
	 */
	public boolean openLoops() {
		return (openLoopStack.size() != 0);
	}

	/**
	 * Runs the current program
	 */
	public void runProgram() {
		// Read the preferences from the properties file and set the speed and
		// distances for every command
		readPref();
		setPref();
		DataController.interrupt = false;
		// Start the Thread for interrupting the execution of the program
		Thread t1 = new Thread(new ButtonThread());
		t1.start();
		this.runProgram(0, data.size() - 1);
	}

	/**
	 * Sets the speed and distance for every command
	 */
	private void setPref() {
		for (DataElem elem : data) {
			elem.setPref();
		}
	}

	/**
	 * Reads the preferences from the properties file
	 */
	private void readPref() {
		Reader reader;
		Properties properties = new Properties();
		try {
			File f = new File("/home/lejos/turtl3prog/turtl3prop.txt");
			reader = new FileReader(f);
			properties.load(reader);
			reader.close();
		} catch (Exception e) {
			Notification.generateNotification("Err: Read Pref");

		}
		speed = Integer.parseInt(properties.getProperty("speed"));
		distLeft = Integer.parseInt(properties.getProperty("distance"));
		distRight = Integer.parseInt(properties.getProperty("distance"));
		rotDistLeft = Integer.parseInt(properties.getProperty("rotDistance"));
		rotDistRight = Integer.parseInt(properties.getProperty("rotDistance"));
		rotSpeed = Integer.parseInt(properties.getProperty("rotSpeed"));
	}

	/**
	 * Runs a part of the program
	 * 
	 * @param l
	 *            left limit
	 * @param r
	 *            right limit
	 */
	private void runProgram(int l, int r) {
		for (int i = l; i <= r; i++) {
			// Interrupt the execution if a button was pressed
			if (interrupt)
				break;
			// Set the display focus to the current command
			dC.setFocus(i);
			DataElem dat = data.get(i);
			switch (dat.getType()) {
			// Run the command in case of a command to move the robot
			case 1:
			case 2:
			case 3:
			case 4:
				dat.run(left, right);
				Delay.msDelay(500);
				break;
			case 5:
				this.generateLoop(i);
				DataElemLoop loop = (DataElemLoop) data.get(i);
				i = loop.getCounter();
				break;
			}
		}
	}

	/**
	 * Generates a new item from a committed string from a file
	 * 
	 * @param line
	 *            a string containing a series of command types
	 */
	private void generateItem(String line) {
		for (int i = 0; i < line.length(); i++) {
			int type = Integer.parseInt(line.substring(i, i + 1));
			switch (type) {
			case 1:
				add(type);
				break;
			case 2:
				add(type);
				break;
			case 3:
				add(type);
				break;
			case 4:
				add(type);
				break;
			case 5:
				if (Integer.parseInt(line.substring(i + 1, i + 2)) == 0) {
					add(type, Integer.parseInt(line.substring(i + 2, i + 3)));
					i += 2;
				} else {
					add(type, Integer.parseInt(line.substring(i + 2, i + 4)));
					i += 3;
				}
				break;
			case 6:
				add(type);

				break;
			}
		}
	}

	/**
	 * Runs a part of the program several types to generate a loop
	 * 
	 * @param i
	 *            index of the loop start
	 */
	private void generateLoop(int i) {
		DataElemLoop first = (DataElemLoop) data.get(i);
		int indexFirst = i + 1;
		int indexLast = first.getCounter() - 1;
		int runs = first.getRuns();
		for (int j = 1; j <= runs; j++) {
			runProgram(indexFirst, indexLast);
		}
	}

	/**
	 * Method to interrupt the execution of current program
	 */
	public static void interrupt() {
		interrupt = true;
	}

	/**
	 * Returns the speed
	 * 
	 * @return speed
	 */
	public static int getSpeed() {
		return speed;
	}

	/**
	 * Returns the distance to drive for the left motor
	 * 
	 * @return distance
	 */
	public static int getDistLeft() {
		return distLeft;
	}

	/**
	 * Returns the distance to drive for the right motor
	 * 
	 * @return distance
	 */
	public static int getDistRight() {
		return distRight;
	}

	/**
	 * Returns the distance to drive a turn for the left motor
	 * 
	 * @return distance
	 */
	public static int getRotDistLeft() {
		return rotDistLeft;
	}

	/**
	 * Returns the distance to drive a turn for the right motor
	 * 
	 * @return distance
	 */
	public static int getRotDistRight() {
		return rotDistRight;
	}

	/**
	 * Returns the speed for turns
	 * 
	 * @return speed
	 */
	public static int getRotSpeed() {
		return rotSpeed;
	}
}
