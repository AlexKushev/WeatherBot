package gatetest;

import java.text.ParseException;

public class TestClass {

	public static void main(String[] args) throws ParseException {
		System.out.println(DataConverter.convertUnixToDate(1486504800));
		System.out.println(DataConverter.convertDateToUnix("2017 02 08"));

	}

}
