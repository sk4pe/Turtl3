package turtl3;

import lejos.hardware.Button;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.Font;
import lejos.hardware.lcd.GraphicsLCD;

/**
 * Class to generate different types of notifications on the EV3
 * 
 * @author PKissmer
 *
 */
public final class Notification {

	private static GraphicsLCD g = LocalEV3.get().getGraphicsLCD();

	/**
	 * 
	 */
	private Notification() {

	}

	/**
	 * Generates a notification with a number chooser
	 * 
	 * @param text
	 *            text to display in the notification
	 * @param number
	 *            initial value of the number chooser
	 * @return chosen number
	 */
	public static int generateNumberNotification(String text, int number) {
		g.setColor(255, 255, 255);
		// Calculate the position of the notification
		int length = text.length();
		if (length > 170)
			length = 170;
		int xPos = 89 - (length * 5) - 5;
		int yPos = 50;
		// Draw the border and the text
		g.fillRect(xPos, yPos, length * 10 + 10, Font.getDefaultFont().height + 6 + Font.getSmallFont().height + 5);
		g.setColor(0, 0, 0);
		g.drawRect(xPos, yPos, length * 10 + 10, Font.getDefaultFont().height + 6 + Font.getSmallFont().height + 5);
		g.drawString(text, xPos + 5, yPos + 5, GraphicsLCD.TOP);
		g.setFont(Font.getSmallFont());
		int cycles = number;
		g.drawString(Integer.toString(cycles), 89,
				yPos + 5 + Font.getDefaultFont().height + 3 + Font.getSmallFont().height, GraphicsLCD.BASELINE);
		// Loop to wait for the user input
		while (true) {
			MyButtons.waitForAnyPress();
			if (Button.ESCAPE.isDown())
				return Integer.MIN_VALUE;
			if (Button.ENTER.isDown())
				return cycles;
			// Update the number chooser if the down button is pressed
			if (Button.DOWN.isDown()) {
				g.setColor(255, 255, 255);
				g.drawString(Integer.toString(cycles), 89,
						yPos + 5 + Font.getDefaultFont().height + 3 + Font.getSmallFont().height, GraphicsLCD.BASELINE);
				if (--cycles == 0)
					cycles = 1;

				g.setColor(0, 0, 0);
				g.drawString(Integer.toString(cycles), 89,
						yPos + 5 + Font.getDefaultFont().height + 3 + Font.getSmallFont().height, GraphicsLCD.BASELINE);

			}
			// Update the number chooser if the up button is pressed
			if (Button.UP.isDown()) {
				g.setColor(255, 255, 255);
				g.drawString(Integer.toString(cycles), 89,
						yPos + 5 + Font.getDefaultFont().height + 3 + Font.getSmallFont().height, GraphicsLCD.BASELINE);

				g.setColor(0, 0, 0);
				g.drawString(Integer.toString(++cycles), 89,
						yPos + 5 + Font.getDefaultFont().height + 3 + Font.getSmallFont().height, GraphicsLCD.BASELINE);

			}
		}
	}

	/**
	 * Generates a text notification
	 * 
	 * @param text
	 *            text to display
	 */
	public static void generateNotification(String text) {
		g.setColor(255, 255, 255);
		int length = text.length();
		if (length > 170)
			length = 170;
		int xPos = 89 - (length * 5) - 5;
		int yPos = 50;
		g.fillRect(xPos, yPos, length * 10 + 10, (Font.getDefaultFont().height + 6));
		g.setColor(0, 0, 0);
		g.drawRect(xPos, yPos, length * 10 + 10, (Font.getDefaultFont().height + 6));
		g.drawString(text, xPos + 5, yPos + 5, GraphicsLCD.TOP);
		// Close the notification after any button is pressed
		while (true) {
			MyButtons.waitForAnyPress();
			break;

		}
	}

	/**
	 * Writes at most three items to the item notification
	 * 
	 * @param xPos
	 *            x-position
	 * @param yPos
	 *            y-position
	 * @param maxLength
	 *            maximum length of the strings
	 * @param text
	 *            array of strings to write
	 * @param firstItem
	 *            index of the first item
	 * @param rectPos
	 *            position of the rectangle to mark the current item
	 */
	private static void writeItems(int xPos, int yPos, int maxLength, String[] text, int firstItem, int rectPos) {
		g.setColor(255, 255, 255);
		g.fillRect(xPos, yPos, maxLength * 10 + 10, (Font.getDefaultFont().height + 6) * 3 + 6);
		g.setColor(0, 0, 0);
		g.drawRect(xPos, yPos, maxLength * 10 + 10, (Font.getDefaultFont().height + 6) * 3 + 6);

		for (int i = 0; i < 3; i++) {
			if (text.length > i + firstItem) {
				g.drawString(text[i + firstItem], xPos + 5, yPos + 5 + (i * (Font.getDefaultFont().height + 6)),
						GraphicsLCD.TOP);
			}

		}
		g.drawRect(xPos + 2, yPos + 2 + (rectPos * (Font.getDefaultFont().height + 6)), maxLength * 10 + 6,
				Font.getDefaultFont().height + 6);
	}

	/**
	 * Generates a item notification with a variable number of items
	 * 
	 * @param text
	 *            array of items to display
	 * @return returns the index of the chosen element
	 */
	public static int generateItemNotification(String... text) {

		if (text.length == 0) {
			generateNotification("No Files");
			return -1;
		} else {
			int firstItem = 0;
			int itemCount = text.length;
			int maxLength = 0;

			for (String t : text) {
				if (t.length() > maxLength)
					maxLength = t.length();
			}
			if (maxLength > 170)
				maxLength = 170;
			int xPos = 89 - (maxLength * 5) - 5;
			int yPos = 50;
			int rectPos = 0;

			while (true) {
				writeItems(xPos, yPos, maxLength, text, firstItem, rectPos);
				MyButtons.waitForAnyPress();
				if (Button.ENTER.isDown()) {
					return rectPos + firstItem;
				}
				if (Button.ESCAPE.isDown())
					return -1;
				if (Button.UP.isDown()) {
					if (--rectPos == -1) {
						if (firstItem == 0) {
							rectPos = 2;
							firstItem = itemCount - 3;
						} else {
							rectPos = 0;
							firstItem--;
						}
					}

				}
				if (Button.DOWN.isDown()) {
					if (++rectPos == 3) {
						if (rectPos + firstItem == itemCount) {
							rectPos = 0;
							firstItem = 0;

						} else {
							rectPos = 2;
							firstItem++;
						}
					}

				}
			}

		}
	}
}
