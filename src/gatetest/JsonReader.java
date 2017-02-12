package gatetest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonReader {

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

	public static String checkForCondition(String json, String condition, String date) {
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

		String botAnswer = null;

		if (conditionForCurrentDay.toLowerCase().contains(condition.toLowerCase())) {
			botAnswer = "Yes, " + conditionForCurrentDay + ".";
		} else {
			botAnswer = "No ( " + conditionForCurrentDay + " )";
		}
		return botAnswer;
	}

	public static String checkForTemperatureOrHumidity(String json, String text, String date) {
		String temperature = null;
		JSONObject jsonObject = new JSONObject(json);
		JSONObject jsonObjectForecast = jsonObject.getJSONObject("forecast");
		JSONArray jsonArrayForecastDay = jsonObjectForecast.getJSONArray("forecastday");
		String botAnswer = null;
		for (int i = 0; i < jsonArrayForecastDay.length(); i++) {
			JSONObject JSONObject_weather = jsonArrayForecastDay.getJSONObject(i);
			if (JSONObject_weather.get("date").equals(date)) {
				temperature = JSONObject_weather.getJSONObject("day").get("avgtemp_c").toString();
				botAnswer = "Temperature is " + temperature + ".";
				break;
			}
		}
		return botAnswer;
	}

	public static String getTemperature(String json, String date) {
		String temperature = null;
		JSONObject jsonObject = new JSONObject(json);
		JSONObject jsonObjectForecast = jsonObject.getJSONObject("forecast");
		JSONArray jsonArrayForecastDay = jsonObjectForecast.getJSONArray("forecastday");
		for (int i = 0; i < jsonArrayForecastDay.length(); i++) {
			JSONObject JSONObject_weather = jsonArrayForecastDay.getJSONObject(i);
			if (JSONObject_weather.get("date").equals(date)) {
				temperature = JSONObject_weather.getJSONObject("day").get("avgtemp_c").toString();
				break;
			}
		}

		double temp = Double.parseDouble(temperature);

		if (temp > 28) {
			return "Hot";
		} else if (temp > 12 && temp <= 28) {
			return "Mild";
		} else if (temp > 4 && temp <= 12) {
			return "Cool";
		} else {
			return "Cold";
		}
	}

	public static String getWind(String json, String date) {
		String wind = null;
		JSONObject jsonObject = new JSONObject(json);
		JSONObject jsonObjectForecast = jsonObject.getJSONObject("forecast");
		JSONArray jsonArrayForecastDay = jsonObjectForecast.getJSONArray("forecastday");
		for (int i = 0; i < jsonArrayForecastDay.length(); i++) {
			JSONObject JSONObject_weather = jsonArrayForecastDay.getJSONObject(i);
			if (JSONObject_weather.get("date").equals(date)) {
				wind = JSONObject_weather.getJSONObject("day").get("maxwind_kph").toString();
				break;
			}
		}

		double windR = Double.parseDouble(wind);

		if (windR > 40) {
			return "Strong";
		} else if (windR > 10 && windR <= 40) {
			return "Weak";
		} else {
			return "None";
		}
	}

	public static String getOverlook(String json, String date) {
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

		if (conditionForCurrentDay.contains("sun")) {
			return "Sunny";
		} else if (conditionForCurrentDay.contains("rain")) {
			return "Rain";
		} else if (conditionForCurrentDay.contains("snow")) {
			return "Snow";
		} else if (conditionForCurrentDay.contains("overcast")) {
			return "Overcast";
		} else if (conditionForCurrentDay.contains("cloud")) {
			return "Cloudy";
		} else {
			return "Mist";
		}
	}

	public static String getHumidity(String json, String date) {
		String hourDate = date.concat(" 16:00");
		String humidity = null;
		JSONObject jsonObject = new JSONObject(json);
		JSONObject jsonObjectForecast = jsonObject.getJSONObject("forecast");
		JSONArray jsonArrayForecastDay = jsonObjectForecast.getJSONArray("forecastday");
		for (int i = 0; i < jsonArrayForecastDay.length(); i++) {
			JSONObject JSONObject_weather = jsonArrayForecastDay.getJSONObject(i);
			if (JSONObject_weather.get("date").equals(date)) {
				JSONArray jsonHoursArray = JSONObject_weather.getJSONArray("hour");
				for (int j = 0; j < jsonHoursArray.length(); j++) {
					JSONObject jsonObjectHour = jsonHoursArray.getJSONObject(j);
					if (jsonObjectHour.get("time").equals(hourDate)) {
						humidity = jsonObjectHour.get("humidity").toString();
						break;
					}
				}

				break;
			}
		}

		Double humidityD = Double.parseDouble(humidity);
		if (humidityD > 60) {
			return "High";
		}
		return "Normal";
	}

	public static String returnForecast(String json, String date) {
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
		String weatherDetails = String.format("%s in %s, %s, it will be %s with a high of %s °C and a low of %s °C.", date,
				city, country, condition, highT, lowT);
		return weatherDetails;
	}

	public static String getUmbrella(String json, String date) {
		if (checkForCondition(json, "rain", date).contains("Yes")
				|| checkForCondition(json, "snow", date).contains("Yes")) {
			return "Yes, take your umbrella.";
		} else {
			return "No, you don't need umbrella.";
		}
	}

}