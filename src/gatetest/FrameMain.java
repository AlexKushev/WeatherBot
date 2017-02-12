package gatetest;

import javax.swing.JFrame;

public class FrameMain {

	public static void main(String[] args) {
		double x = 5.5;
		if (x > 5) {
			System.out.println("works");
		}
		JFrame frame = new JFrame("SAM Weather Bot");
		WeatherPanel panel = new WeatherPanel();
		frame.getContentPane().add(panel);
		frame.pack();
		frame.setVisible(true);
	
	}
}
