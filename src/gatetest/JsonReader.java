package gatetest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

	private static String API_KEY = "09d51d887bc97a501affa2ab1511b63a";
	private static String KEY = "00470eef42ab425393a165823170502";

	public static String generateApiCallURL(String city) {
		String url = String.format("http://api.apixu.com/v1/forecast.json?key=%s&q=%s&days=7", KEY, city);
		System.out.println(url);
		return url;
	}

	public static String getJsonWithData(String city) {

		String result = "";

		try {

			String url = generateApiCallURL(city);

			URL url_weather = new URL(url);

			HttpURLConnection httpURLConnection = (HttpURLConnection) url_weather.openConnection();

			if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

				InputStreamReader inputStreamReader = new InputStreamReader(httpURLConnection.getInputStream());
				BufferedReader bufferedReader = new BufferedReader(inputStreamReader, 8192);
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					result += line;
				}

				bufferedReader.close();

				// String weatherResult = ParseResult(result);
				//
				// System.out.println(weatherResult);

			} else {
				System.out.println("Error in httpURLConnection.getResponseCode()!!!");
			}

		} catch (MalformedURLException ex) {
		} catch (IOException ex) {
		} catch (JSONException ex) {
		}

		return result;
	}

	public static void main(String[] args) throws ParseException {
		String json = getJsonWithData("Sofia");
		System.out.println(json);
		JSONObject jsonObject = new JSONObject(json);
		JSONObject jsonObjectForecast = jsonObject.getJSONObject("forecast");
		JSONArray jsonArrayForecastDay = jsonObjectForecast.getJSONArray("forecastday");
		for (int i = 0; i < jsonArrayForecastDay.length(); i++) {
			JSONObject JSONObject_weather = jsonArrayForecastDay.getJSONObject(i);
			System.out.println(JSONObject_weather.get("date"));
		}

	}

	public static void checkForCondition(String json, String condition, String date) {
		String conditionForCurrentDay = null;
		JSONObject jsonObject = new JSONObject(json);
		JSONObject jsonObjectForecast = jsonObject.getJSONObject("forecast");
		JSONArray jsonArrayForecastDay = jsonObjectForecast.getJSONArray("forecastday");
		for (int i = 0; i < jsonArrayForecastDay.length(); i++) {
			JSONObject JSONObject_weather = jsonArrayForecastDay.getJSONObject(i);
			if (JSONObject_weather.get("date").equals(date)) {
				conditionForCurrentDay = JSONObject_weather.getJSONObject("day").getJSONObject("condition").get("text")
						.toString();
				break;
			}
		}

		if (conditionForCurrentDay.toLowerCase().contains(condition.toLowerCase())) {
			System.out.println("Yes ( " + conditionForCurrentDay + " )");
		} else {
			System.out.println("No ( " + conditionForCurrentDay + " )");
		}

	}

	public static void checkForTemperatureOrHumidity(String json, String text, String date) {
		String temperature = null;
		JSONObject jsonObject = new JSONObject(json);
		JSONObject jsonObjectForecast = jsonObject.getJSONObject("forecast");
		JSONArray jsonArrayForecastDay = jsonObjectForecast.getJSONArray("forecastday");
		for (int i = 0; i < jsonArrayForecastDay.length(); i++) {
			JSONObject JSONObject_weather = jsonArrayForecastDay.getJSONObject(i);
			if (JSONObject_weather.get("date").equals(date)) {
				temperature = JSONObject_weather.getJSONObject("day").get("avgtemp_c").toString();
				System.out.println("Temperature : " + temperature);
				break;
			}
		}
	}

	public static void returnForecast(String json, String date) {
		JSONObject jsonObject = new JSONObject(json);
		JSONObject jsonLocation = jsonObject.getJSONObject("location");
		String city = jsonLocation.get("name").toString();
		String country = jsonLocation.get("country").toString();
		String condition = null;
		String lowT = null;
		String highT = null;
		JSONObject jsonObjectForecast = jsonObject.getJSONObject("forecast");
		JSONArray jsonArrayForecastDay = jsonObjectForecast.getJSONArray("forecastday");
		for (int i = 0; i < jsonArrayForecastDay.length(); i++) {
			JSONObject JSONObject_weather = jsonArrayForecastDay.getJSONObject(i);
			if (JSONObject_weather.get("date").equals(date)) {
				condition = JSONObject_weather.getJSONObject("day").getJSONObject("condition").get("text").toString();
				lowT = JSONObject_weather.getJSONObject("day").get("mintemp_c").toString();
				highT = JSONObject_weather.getJSONObject("day").get("maxtemp_c").toString();
				break;
			}
		}
		String weatherDetails = String.format("%s in %s, %s, it’ll be %s with a high of %s°C and a low of %s°C.", date,
				city, country, condition, highT, lowT);
		System.out.println(weatherDetails);
	}

}