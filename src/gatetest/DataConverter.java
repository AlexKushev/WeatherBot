package gatetest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DataConverter {

	private static long TEST_VALUE = 1486728000;
	private static String TEST_DATE = "2017-02-10";

	public static String convertUnixToDate(long unixSeconds) {
		Date date = new Date(unixSeconds * 1000);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		String formattedDate = sdf.format(date);
		return formattedDate;
	}

	public static long convertDateToUnix(String date) throws ParseException {
		String formatDate = date.concat(" 08:00:00 GMT-04:00");
		SimpleDateFormat dfm = new SimpleDateFormat("yyyyMMdd");
		
		Date d = new SimpleDateFormat("yyyy-MM-dd").parse(date);

		long unixTime = 0;

		unixTime = d.getTime();
		unixTime = unixTime / 1000;

		return unixTime;
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(convertUnixToDate(1486641600));
		System.out.println(convertDateToUnix(TEST_DATE));
		
		Month.valueOf("Frebuary".toUpperCase()).getValue();

	}

}
