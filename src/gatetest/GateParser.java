package gatetest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Utils;

public class GateParser {

	private Document document;

	public GateParser(Document document) {
		this.document = document;
	}

	public String getLocation(AnnotationSet annSet) {
		String type = "Location";
		String location = null;

		AnnotationSet locationSet = annSet.get(type);
		List<Annotation> locationsList = new ArrayList<Annotation>(locationSet);

		if (locationsList.isEmpty()) {
			location = "Sofia";
		} else {
			for (Annotation a : locationsList) {
				location = Utils.stringFor(document, a);
			}
		}
		return location;
	}

	public String isSpecificConditionCheck(AnnotationSet annSet) {
		String type = "WeatherCondition";
		String condition = null;

		AnnotationSet conditionSet = annSet.get(type);
		List<Annotation> conditionsList = new ArrayList<Annotation>(conditionSet);

		if (conditionsList.isEmpty()) {
			return null;
		} else {
			for (Annotation a : conditionsList) {
				condition = a.getFeatures().get("kind").toString();
			}
		}
		return condition;
	}

	public String isCheckForTemperatureOrHumidity(AnnotationSet annSet) {
		String type = "WeatherDetail";

		String text = null;

		AnnotationSet conditionSet = annSet.get(type);
		List<Annotation> conditionsList = new ArrayList<Annotation>(conditionSet);

		if (conditionsList.isEmpty()) {
			return null;
		} else {
			for (Annotation a : conditionsList) {
				text = Utils.stringFor(document, a);
			}
		}

		return text;
	}

	public boolean isCheckForForecat(AnnotationSet annSet) {
		String type = "Forecast";

		AnnotationSet forecastSet = annSet.get(type);
		List<Annotation> forecastList = new ArrayList<Annotation>(forecastSet);

		if (forecastList.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	public static String getDate(AnnotationSet annSet) {
		String type = "ConvertDate";
		String date = null;

		AnnotationSet dateSet = annSet.get(type);
		List<Annotation> dateList = new ArrayList<Annotation>(dateSet);

		if (dateList.isEmpty()) {
			Date curDate = new Date();
			date = new SimpleDateFormat("yyyy-MM-dd").format(curDate);
		} else {
			for (Annotation a : dateList) {
				date = a.getFeatures().get("date").toString();
			}
		}

		return date;

	}

	public boolean getAddExtraConditions(AnnotationSet annSet) {
		String type = "ExtraCondition";

		AnnotationSet set = annSet.get(type);
		List<Annotation> list = new ArrayList<Annotation>(set);

		if (list.isEmpty()) {
			return false;
		}

		return true;

	}

	public boolean isQuestion(AnnotationSet annSet) {
		String type = "WeatherQuestionSentences";

		AnnotationSet set = annSet.get(type);
		List<Annotation> questionList = new ArrayList<Annotation>(set);

		if (questionList.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public boolean isNaiveBayesQuestion(AnnotationSet annSet) {
		String type = "NaiveBayesQuestion";
		
		AnnotationSet naiveBayesSet = annSet.get(type);
		List<Annotation> naiveBayesList = new ArrayList<Annotation>(naiveBayesSet);
		
		if (naiveBayesList.isEmpty()) {
			return false;
		}
		return true;
	}
	
	public String checkNaiveBayesCondition(AnnotationSet annSet, String json, String date) {
		String type = "NaiveBayesCondition";
		String activity = null;
		
		AnnotationSet naiveBayesSet = annSet.get(type);
		List<Annotation> naiveBayesList = new ArrayList<Annotation>(naiveBayesSet);
		
		for (Annotation a : naiveBayesList) {
			activity = a.getFeatures().get("kind").toString();
		}
		
		NaiveBayesClassifier nb = new NaiveBayesClassifier();
		String fileName = "TrainingSet-" + activity;
		String[] arr = new String[4];
		arr[0] = JsonReader.getOverlook(json, date);
		arr[1] = JsonReader.getTemperature(json, date);
		arr[2] = JsonReader.getHumidity(json, date);
		arr[3] = JsonReader.getWind(json, date);
		String naiveBayesResult = nb.classify(arr, "TrainingSets/" + fileName);
		
		if (naiveBayesResult.equals("Yes")) {
			return "Yes";
		} else {
			return "No";
		}
		
	}
	
	
	public boolean shouldNaiveBayes(AnnotationSet annSet) {
		return true;
	}
	
	

}
