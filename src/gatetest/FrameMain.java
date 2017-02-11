package gatetest;

import javax.swing.JFrame;

public class FrameMain {

	public static void main(String[] args) {
		JFrame frame = new JFrame("SAM Weather Bot");
		WeatherPanel panel = new WeatherPanel();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	}
}
