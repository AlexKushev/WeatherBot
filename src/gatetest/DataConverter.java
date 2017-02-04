package gatetest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DataConverter {

	private static long TEST_VALUE = 1486728000;
	private static String TEST_DATE = "2017-02-10";

	public static String convertUnixToDate(long unixSeconds) {
		Date date = new Date(unixSeconds * 1000L);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));

		String formattedDate = sdf.format(date);
		return formattedDate;
	}

	public static long convertDateToUnix(String date) {
		String formatDate = date.concat(" 08:00:00 GMT-04:00");
		SimpleDateFormat dfm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

		long unixTime = 0;

		dfm.setTimeZone(TimeZone.getTimeZone("GMT-4"));
		try {
			unixTime = dfm.parse(formatDate).getTime();
			unixTime = unixTime / 1000;
		} catch (ParseException e) {
			e.printStackTrace();
		}

		return unixTime;
	}

	public static void main(String[] args) {
		System.out.println(convertUnixToDate(1486641600));
		System.out.println(convertDateToUnix(TEST_DATE));

	}

}
