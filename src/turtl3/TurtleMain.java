package turtl3;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Properties;

/**
 * Class with methods to start the program and to do a system reset. Also does a
 * startup routine when launching to generate the file system and example
 * programs
 * 
 * @author PKissmer
 *
 */
public class TurtleMain {

	//Example programs
	private static final String[] programs = { "3323344", "3233232", "3434323", "32233344332334323343", "3233", "33234",
			"333233233343", "2334334323", "323333", "3332333", "3234", "323434", "503326", "334350426350426",
			"350426335042643", "33343234", "502334633" };
	//Names of the example programs
	private static final String[] programNames = { "SymbolA1", "BilderA1", "BilderA2", "AlphabetA1", "AlphabetB1",
			"AlphabetB2", "SymbolB1", "SymbolB2", "InselB1", "InselB2", "BilderC1", "SymbolC1", "AlphabetE1",
			"AlphabetE2", "BilderE1", "InselE1", "SymbolE1" };

	/**
	 * Main method to start the program
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		startupRoutine();
		new Menu();
	}

	/**
	 * Does a system reset. Clears the corresponding folders and executes the
	 * startup routine to recreate the folders and example programs
	 */
	public static void systemReset() {
		clearFolder();
		startupRoutine();
	}

	/**
	 * Clears the corresponding folders and deletes examples and own program
	 * files
	 */
	private static void clearFolder() {
		//Delete the examples folder
		File g = new File("/home/lejos/turtl3prog/examples");
		if (g.exists()) {
			File[] filesG = g.listFiles();
			for (File aktFile : filesG) {
				aktFile.delete();
			}
			g.delete();
		}
		//Delete the program folder
		File h = new File("/home/lejos/turtl3prog/prog");
		if (h.exists()) {
			File[] filesH = h.listFiles();
			for (File aktFile : filesH) {
				aktFile.delete();
			}
			h.delete();
		}
		//Delete the turtle folder
		File f = new File("/home/lejos/turtl3prog");
		if (f.exists()) {
			File[] files = f.listFiles();
			for (File aktFile : files) {
				aktFile.delete();
			}
			f.delete();
		}
	}

	/**
	 * Executes the startup routine to create the file system and generate the example programs
	 */
	private static void startupRoutine() {
		//Generate the turtle folder
		File f = new File("/home/lejos/turtl3prog");
		if (!f.exists()) {
			f.mkdir();
		}
		//Generates the examples folder and creates a file for every example
		File g = new File("/home/lejos/turtl3prog/examples");
		if (!g.exists()) {
			g.mkdir();
			for (int i = 0; i < programs.length; i++) {
				File file = new File("/home/lejos/turtl3prog/examples/" + programNames[i]);
				try {
					Writer writer = new FileWriter(file);
					writer.write(programs[i]);
					writer.flush();
					writer.close();
				} catch (IOException e) {
					Notification.generateNotification("Err: Writing File");
				}
			}
		}
		//Creates the folder for own programs
		File h = new File("/home/lejos/turtl3prog/prog");
		if (!h.exists()) {
			h.mkdir();
		}
		
		//Creates the properties file
		f = new File("/home/lejos/turtl3prog/turtl3prop.txt");
		if (!f.exists()) {
			Properties prop = new Properties();
			prop.put("speed", "120");
			prop.put("distance", "308");
			prop.put("rotSpeed", "80");
			prop.put("rotDistance", "180");
			try {
				Writer writer = new FileWriter(f);
				prop.store(writer, "");
				writer.close();
			} catch (IOException e) {
				Notification.generateNotification("Err: Writing File");
			}
		}
	}
}
