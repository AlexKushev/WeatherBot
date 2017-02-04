package gatetest;

import java.util.Scanner;

public class GateMain {

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		System.out.println("What is your question : ");
		String txt = scanner.nextLine();

		GateRunner gateRunner = new GateRunner();

		try {
			gateRunner.runner(txt);
		} catch (Exception e) {
			e.printStackTrace();
		}

		scanner.close();

	}

}
