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

	private static String API_KEY = "09d51d887bc97a501affa2ab1511b63a";

	public static String generateApiCallURL(String city) {
		String url = String.format("http://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s", city, API_KEY);
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

	public static boolean checkForCondition(String json, String condition) {
		JSONObject jsonObject = new JSONObject(json);
		JSONArray JSONArray_weather = jsonObject.getJSONArray("weather");
		String resultMain = null;
		if (JSONArray_weather.length() > 0) {
			JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(0);
			resultMain = JSONObject_weather.getString("main");
			System.out.println(resultMain);
			System.out.println(condition);
		}
		
		if (resultMain.toLowerCase().contains(condition.toLowerCase())) {
			return true;
		}
		
		return false;
	}

	static private String ParseResult(String json) throws JSONException {

		String parsedResult = "";

		JSONObject jsonObject = new JSONObject(json);

		parsedResult += "Number of object = " + jsonObject.length() + "\n\n";

		// "coord"
		JSONObject JSONObject_coord = jsonObject.getJSONObject("coord");
		Double result_lon = JSONObject_coord.getDouble("lon");
		Double result_lat = JSONObject_coord.getDouble("lat");

		// "sys"
		JSONObject JSONObject_sys = jsonObject.getJSONObject("sys");
		String result_country = JSONObject_sys.getString("country");
		int result_sunrise = JSONObject_sys.getInt("sunrise");
		int result_sunset = JSONObject_sys.getInt("sunset");

		// "weather"
		String result_weather;
		JSONArray JSONArray_weather = jsonObject.getJSONArray("weather");
		if (JSONArray_weather.length() > 0) {
			JSONObject JSONObject_weather = JSONArray_weather.getJSONObject(0);
			int result_id = JSONObject_weather.getInt("id");
			String result_main = JSONObject_weather.getString("main");
			String result_description = JSONObject_weather.getString("description");
			String result_icon = JSONObject_weather.getString("icon");

			result_weather = "weather\tid: " + result_id + "\tmain: " + result_main + "\tdescription: "
					+ result_description + "\ticon: " + result_icon;
		} else {
			result_weather = "weather empty!";
		}

		// "base"
		String result_base = jsonObject.getString("base");

		// "main"
		JSONObject JSONObject_main = jsonObject.getJSONObject("main");
		Double result_temp = JSONObject_main.getDouble("temp");
		Double result_pressure = JSONObject_main.getDouble("pressure");
		Double result_humidity = JSONObject_main.getDouble("humidity");
		Double result_temp_min = JSONObject_main.getDouble("temp_min");
		Double result_temp_max = JSONObject_main.getDouble("temp_max");

		// "wind"
		JSONObject JSONObject_wind = jsonObject.getJSONObject("wind");
		Double result_speed = JSONObject_wind.getDouble("speed");
		// Double result_gust = JSONObject_wind.getDouble("gust");
		Double result_deg = JSONObject_wind.getDouble("deg");
		String result_wind = "wind\tspeed: " + result_speed + "\tdeg: " + result_deg;

		// "clouds"
		JSONObject JSONObject_clouds = jsonObject.getJSONObject("clouds");
		int result_all = JSONObject_clouds.getInt("all");

		// "dt"
		int result_dt = jsonObject.getInt("dt");

		// "id"
		int result_id = jsonObject.getInt("id");

		// "name"
		String result_name = jsonObject.getString("name");

		// "cod"
		int result_cod = jsonObject.getInt("cod");

		return "coord\tlon: " + result_lon + "\tlat: " + result_lat + "\n" + "sys\tcountry: " + result_country
				+ "\tsunrise: " + result_sunrise + "\tsunset: " + result_sunset + "\n" + result_weather + "\n"
				+ "base: " + result_base + "\n" + "main\ttemp: " + result_temp + "\thumidity: " + result_humidity
				+ "\tpressure: " + result_pressure + "\ttemp_min: " + result_temp_min + "\ttemp_max: " + result_temp_min
				+ "\n" + result_wind + "\n" + "clouds\tall: " + result_all + "\n" + "dt: " + result_dt + "\n" + "id: "
				+ result_id + "\n" + "name: " + result_name + "\n" + "cod: " + result_cod + "\n" + "\n";
	}
}