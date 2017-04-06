package turtl3;

import lejos.hardware.Button;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.robotics.SampleProvider;
import lejos.utility.Delay;

/**
 * Provides a method to wait for a button press. This includes the buttons of
 * the robot and a touch-sensor in port 1
 * 
 * @author PKissmer
 *
 */
public final class MyButtons {

	private static EV3TouchSensor touch = new EV3TouchSensor(SensorPort.S1);

	/**
	 * 
	 */
	private MyButtons() {
	}

	/**
	 * Return as soon as a button was pressed
	 * 
	 * @return 1 if touch sensor was pressed, 0 if a button was pressed. Use the
	 *         "isPressed" method of the "Buttons" class in the leJOS-API to
	 *         check which button was pressed
	 */
	public static int waitForAnyPress() {
		Delay.msDelay(500);
		SampleProvider s = touch.getTouchMode();
		int value = 0;
		while (true) {
			// Get the value of the sensor
			float[] sample = new float[s.sampleSize()];
			s.fetchSample(sample, 0);
			value = (int) sample[0];
			if (value == 1)
				return 1;
			if (Button.getButtons() != 0)
				return 0;
		}
	}

}
