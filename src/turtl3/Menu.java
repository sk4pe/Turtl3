package turtl3;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Properties;

import lejos.hardware.Button;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.LCD;
import lejos.utility.Delay;
import turtl3.data.DataController;
import turtl3.graphic.DispController;
import turtl3.graphic.GraphElem;
import turtl3.graphic.GraphElemBorder;
import turtl3.graphic.GraphElemLine;
import turtl3.graphic.GraphElemText;
import turtl3.graphic.GraphElemTriangle;

/**
 * Executes every actions in the menu. This includes the navigation, managing
 * the buttons and transfer commands to the DataController or the
 * DisplayController. The class further manages the writing and reading from
 * files.
 * 
 * @author PKissmer
 *
 */
public class Menu {

	private int mainMenuPos = 0;
	private int programMenuPos = 0;
	private int prefMenuPos = 0;

	private DispController dC;
	private DataController datC;

	private GraphElem programMenuIcon;
	private GraphElem mainMenuIcon;
	private GraphElem prefMenuIcon;

	private static final int MAINMENUITEMS = 4;
	private static final int PROGRAMMENUITEMS = 5;
	private static final int PREFMENUITEMS = 4;

	/**
	 * Creates a new menu
	 */
	public Menu() {
		initialize();
		mainMenu();
	}

	/**
	 * Initializes the data and display controller
	 */
	private void initialize() {
		datC = new DataController();
		dC = new DispController(datC);
		datC.addDispController(dC);
	}

	/**
	 * Manages the control of the main menu
	 */
	private void mainMenu() {
		mainMenuPos = 0;
		dC.clear();
		// Adding menu items to the main menu
		dC.addElem(new GraphElemText(25, 10, "Turtl3", Font.getLargeFont()));
		dC.addElem(new GraphElemText(20, 50, "Program"));
		dC.addElem(new GraphElemText(20, 70, "Preferences"));
		dC.addElem(new GraphElemText(20, 90, "Help"));
		dC.addElem(new GraphElemText(20, 110, "Exit"));
		mainMenuIcon = new GraphElemTriangle(0, 50, 10);
		dC.addElem(mainMenuIcon);
		// Loop to wait for input and react to it
		while (true) {
			MyButtons.waitForAnyPress();
			if (Button.DOWN.isDown())
				updateMainMenuPos(true);
			if (Button.UP.isDown())
				updateMainMenuPos(false);
			if (Button.ESCAPE.isDown()) {
				Delay.msDelay(1500);
				if (Button.ESCAPE.isDown()) {
					TurtleMain.systemReset();
					Notification.generateNotification("Files cleared");
				} else {
					System.exit(0);
				}
			}
			if (Button.ENTER.isDown())
				accessMenu();
			dC.refresh();
		}
	}

	/**
	 * Decides which menu to enter
	 */
	private void accessMenu() {
		switch (mainMenuPos) {
		case 0:
			programMenu();
			break;
		case 1:
			preferencesMenu();
			break;
		case 2:
			helpMenu();
		case 3:
			System.exit(0);
			break;
		}
	}

	/**
	 * Mangages the control of the help menu
	 */
	private void helpMenu() {
		dC.clear();
		dC.addElem(new GraphElemText(0, 0, "Ports:"));
		dC.addElem(new GraphElemText(0, 20, "Left Motor: B", Font.getSmallFont()));
		dC.addElem(new GraphElemText(0, 30, "Right Motor: C", Font.getSmallFont()));
		dC.addElem(new GraphElemText(0, 40, "Touch-Sensor: 1", Font.getSmallFont()));
		dC.addElem(new GraphElemText(0, 55, "Programming:"));
		dC.addElem(new GraphElemText(0, 75, "Enter: Open/Close Loop", Font.getSmallFont()));
		dC.addElem(new GraphElemText(0, 85, "Up: Drive Forward", Font.getSmallFont()));
		dC.addElem(new GraphElemText(0, 95, "Down: Drive Backwards", Font.getSmallFont()));
		dC.addElem(new GraphElemText(0, 105, "Left: Turn Left", Font.getSmallFont()));
		dC.addElem(new GraphElemText(0, 115, "Right: Turn Right", Font.getSmallFont()));
		// Just waits for a simple button press to go back to the main menu
		while (true) {
			MyButtons.waitForAnyPress();
			break;
		}
		mainMenu();
	}

	/**
	 * Manages the control of the program menu
	 */
	private void programMenu() {
		// Adding elements to the program menu
		dC.clear();
		dC.addElem(new GraphElemText(5, 15, "Play", Font.getSmallFont()));
		dC.addElem(new GraphElemText(40, 15, "Edit", Font.getSmallFont()));
		dC.addElem(new GraphElemText(75, 15, "Del", Font.getSmallFont()));
		dC.addElem(new GraphElemText(110, 15, "Open", Font.getSmallFont()));
		dC.addElem(new GraphElemText(145, 15, "Save", Font.getSmallFont()));
		dC.addElem(new GraphElemLine(0, 30, LCD.SCREEN_WIDTH, 30));
		programMenuIcon = new GraphElemBorder(0, 10, 15, 35);
		programMenuIcon.setPosition(0 + (programMenuPos * 35), 10);
		dC.drawProgram();
		dC.addElem(programMenuIcon);
		// Loop to wait for input and react to it
		while (true) {
			MyButtons.waitForAnyPress();
			if (Button.ESCAPE.isDown())
				mainMenu();
			if (Button.RIGHT.isDown())
				updateProgramMenuPos(true);
			if (Button.LEFT.isDown())
				updateProgramMenuPos(false);
			if (Button.ENTER.isDown()) {
				if (programMenuPos == 0)
					playProgram();
				if (programMenuPos == 1)
					editProgram();
				if (programMenuPos == 2)
					deleteProgram();
				if (programMenuPos == 3)
					openProgram();
				if (programMenuPos == 4)
					saveProgram();
			}
			dC.refresh();
		}

	}

	/**
	 * Generates notifications and manages the user interaction to open a file
	 * in the file system
	 */
	private void openProgram() {
		// Notification to open examples or files
		int k = Notification.generateItemNotification("Examples", "Files");
		dC.refresh();
		if (k >= 0) {
			if (k == 0) {
				// Path to the example files
				File f = new File("/home/lejos/turtl3prog/examples");
				File[] files = f.listFiles();
				ArrayList<String> fileList = new ArrayList<String>();
				// Reading the file names
				for (int i = 0; i < files.length; i++) {
					fileList.add(files[i].getName());
				}
				String[] datNames = fileList.toArray(new String[fileList.size()]);
				// Generate a new notification with the file names to choose one
				// file
				int file = Notification.generateItemNotification(datNames);
				if (file >= 0) {
					// Loads the example, generates a program and writes the
					// program to the display
					f = new File("/home/lejos/turtl3prog/examples/" + datNames[file]);
					datC.loadProgramFromFile(f);
					dC.clearProgram();
					dC.drawProgram();
					dC.refresh();
				}
			} else if (k == 1) {
				// Path to the own files
				File f = new File("/home/lejos/turtl3prog/prog");
				File[] files = f.listFiles();
				ArrayList<String> fileList = new ArrayList<String>();
				// Reading the file names
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().endsWith(".turtl3"))
						fileList.add(files[i].getName());
				}
				String[] datNames = fileList.toArray(new String[fileList.size()]);
				// Generates a new notification with the file names
				int file = Notification.generateItemNotification(datNames);
				if (file >= 0) {
					f = new File("/home/lejos/turtl3prog/prog/" + datNames[file]);
					// Generates a new Notification to open or delete the file
					int del = Notification.generateItemNotification("Open", "Delete");
					dC.refresh();
					if (del >= 0) {
						if (del == 0) {
							// Open the file and show the program on the display
							datC.loadProgramFromFile(f);
							dC.clearProgram();
							dC.drawProgram();
							dC.refresh();
						} else if (del == 1) {
							f.delete();
						}
					}
				}
			}
		}
	}

	/**
	 * Saves a program to the file system
	 */
	private void saveProgram() {
		// Notification to choose a number for the file
		int number = Notification.generateNumberNotification("Nummer:", 1);
		if (number > 0) {
			File f = new File("/home/lejos/turtl3prog/prog/" + Integer.toString(number) + ".turtl3");
			datC.writeProgramToFile(f);
		}
	}

	/**
	 * Plays the current program
	 */
	private void playProgram() {
		if (datC.openLoops())
			Notification.generateNotification("Close Loops");
		else {
			dC.setLargeView();
			Delay.msDelay(1000);
			datC.runProgram();
			dC.setSmallView();
		}

	}

	/**
	 * Manages the control of the edit menu
	 */
	private void editProgram() {
		// Change to large view
		dC.remove(programMenuIcon);
		dC.setViewToLast();
		dC.setLargeView();
		// Loop to wait for user input
		while (true) {
			int i = MyButtons.waitForAnyPress();
			if (Button.ESCAPE.isDown()) {
				dC.addElem(programMenuIcon);
				dC.setViewToFirst();
				dC.setSmallView();
				break;
			}
			// Adding the different commands to the program
			if (Button.UP.isDown()) {
				datC.add(3);
			}
			if (Button.DOWN.isDown()) {
				datC.add(1);
			}
			if (Button.LEFT.isDown()) {
				datC.add(2);
			}
			if (Button.RIGHT.isDown()) {
				datC.add(4);
			}
			// Generate a new loop
			if (Button.ENTER.isDown()) {
				int type = Notification.generateItemNotification("New Loop", "End Loop");
				if (type == 0) {
					dC.refresh();
					int cyc = Notification.generateNumberNotification("Loop runs:", 1);
					if (cyc != Integer.MIN_VALUE)
						datC.add(5, cyc);
				}
				if (type == 1) {
					dC.refresh();
					if (!datC.add(6))
						Notification.generateNotification("Open Loop first");
				}

			}
			if (i == 1) {
				datC.undo();
			}
			dC.refresh();
			dC.setViewToLast();
		}

	}

	/**
	 * Deletes the Current program
	 */
	private void deleteProgram() {
		int i = Notification.generateItemNotification("Delete", "Cancel");
		if (i == 0) {
			datC.deleteProgram();
			dC.clearProgram();
			dC.refresh();
			dC.drawProgram();
		}
	}

	/**
	 * Keeps the position of the border in the program menu updated
	 * 
	 * @param right
	 *            true if right button was pressed
	 */
	private void updateProgramMenuPos(boolean right) {
		if (right)
			programMenuPos++;
		else
			programMenuPos--;
		if (programMenuPos == -1)
			programMenuPos = PROGRAMMENUITEMS - 1;
		if (programMenuPos == PROGRAMMENUITEMS)
			programMenuPos = 0;
		programMenuIcon.setPosition(0 + (programMenuPos * 35), 10);

	}

	/**
	 * Manages the control of the preferences menu
	 */
	private void preferencesMenu() {
		prefMenuPos = 0;
		// Loading the saved preferences from the file system
		Reader reader;
		Properties properties = new Properties();
		try {
			File f = new File("/home/lejos/turtl3prog/turtl3prop.txt");
			reader = new FileReader(f);
			properties.load(reader);
			reader.close();
		} catch (Exception e) {
			Notification.generateNotification("Err: Load Pref.");

		}
		// Draw the menu on the display
		dC.clear();
		dC.addElem(new GraphElemText(20, 30, "Speed"));
		dC.addElem(new GraphElemText(20, 50, "Distance"));
		dC.addElem(new GraphElemText(20, 70, "Rotation Speed"));
		dC.addElem(new GraphElemText(20, 90, "Rotation Distance"));
		prefMenuIcon = new GraphElemTriangle(0, 30, 10);
		dC.addElem(prefMenuIcon);
		// Loop to wait for user input
		while (true) {
			MyButtons.waitForAnyPress();
			if (Button.DOWN.isDown())
				updatePrefMenuPos(true);
			if (Button.UP.isDown())
				updatePrefMenuPos(false);
			if (Button.ESCAPE.isDown())
				mainMenu();
			// Show the current value of the selected item and saves changes to
			// the properties file
			if (Button.ENTER.isDown()) {
				Writer writer;
				int dat = 0;
				int value = 0;
				switch (prefMenuPos) {
				case 0:
					dat = Integer.parseInt(properties.getProperty("speed"));
					value = Notification.generateNumberNotification("Speed:", dat);
					if (value != Integer.MIN_VALUE)
						properties.put("speed", Integer.toString(value));
					try {
						File f = new File("/home/lejos/turtl3prog/turtl3prop.txt");
						writer = new FileWriter(f);
						properties.store(writer, "");
						writer.flush();
						writer.close();
					} catch (IOException e) {
						Notification.generateNotification("Err: Write Pref.");

					}
					break;
				case 1:
					dat = Integer.parseInt(properties.getProperty("distance"));
					value = Notification.generateNumberNotification("Distance:", dat);
					if (value != Integer.MIN_VALUE)
						properties.put("distance", Integer.toString(value));
					try {
						File f = new File("/home/lejos/turtl3prog/turtl3prop.txt");
						writer = new FileWriter(f);
						properties.store(writer, "");
						writer.flush();
						writer.close();
					} catch (IOException e) {
						Notification.generateNotification("Err: Write Pref.");
					}
					break;
				case 2:
					dat = Integer.parseInt(properties.getProperty("rotSpeed"));
					value = Notification.generateNumberNotification("Rotation Speed:", dat);
					if (value != Integer.MIN_VALUE)
						properties.put("rotSpeed", Integer.toString(value));
					try {
						File f = new File("/home/lejos/turtl3prog/turtl3prop.txt");
						writer = new FileWriter(f);
						properties.store(writer, "");
						writer.flush();
						writer.close();
					} catch (IOException e) {
						Notification.generateNotification("Err: Write Pref.");
					}
					break;
				case 3:
					dat = Integer.parseInt(properties.getProperty("rotDistance"));
					value = Notification.generateNumberNotification("Rotation Distance:", dat);
					if (value != Integer.MIN_VALUE)
						properties.put("rotDistance", Integer.toString(value));
					try {
						File f = new File("/home/lejos/turtl3prog/turtl3prop.txt");
						writer = new FileWriter(f);
						properties.store(writer, "");
						writer.flush();
						writer.close();
					} catch (IOException e) {
						Notification.generateNotification("Err: Write Pref.");
					}
					break;
				}
			}
			dC.refresh();
		}
	}

	/**
	 * Keeps the position of the triangle in the preferences menu updated
	 * 
	 * @param dir
	 *            true if the down button was pressed
	 */
	private void updatePrefMenuPos(boolean dir) {
		if (dir)
			prefMenuPos++;
		else
			prefMenuPos--;
		if (prefMenuPos == -1)
			prefMenuPos = PREFMENUITEMS - 1;
		if (prefMenuPos == PREFMENUITEMS)
			prefMenuPos = 0;
		prefMenuIcon.setPosition(0, 30 + (prefMenuPos * 20));
	}

	/**
	 * Keeps the position of the triangle in the main menu updated
	 * 
	 * @param dir
	 *            true if the down button was pressed
	 */
	private void updateMainMenuPos(boolean dir) {
		if (dir)
			mainMenuPos++;
		else
			mainMenuPos--;
		if (mainMenuPos == -1)
			mainMenuPos = MAINMENUITEMS - 1;
		if (mainMenuPos == MAINMENUITEMS)
			mainMenuPos = 0;
		mainMenuIcon.setPosition(0, 50 + (mainMenuPos * 20));
	}

}
