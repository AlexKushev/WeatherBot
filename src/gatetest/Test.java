package gatetest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.DocumentContent;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.Utils;
import gate.creole.SerialAnalyserController;

public class Test {

	public static void main(String[] args) throws Exception {
		// initialize the GATE library
		System.setProperty("gate.home", "/Applications/GATE_Developer_8.3/");

		Gate.init();

		Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "ANNIE").toURI().toURL());
		SerialAnalyserController pipeline = (SerialAnalyserController) gate.Factory
				.createResource("gate.creole.SerialAnalyserController");
		LanguageAnalyser tokeniser = (LanguageAnalyser) gate.Factory
				.createResource("gate.creole.tokeniser.DefaultTokeniser");
		LanguageAnalyser gazetteer = (LanguageAnalyser) gate.Factory
				.createResource("gate.creole.gazetteer.DefaultGazetteer");
		LanguageAnalyser neTrans = (LanguageAnalyser) gate.Factory.createResource("gate.creole.ANNIETransducer");
		LanguageAnalyser jape = (LanguageAnalyser) gate.Factory.createResource("gate.creole.Transducer",
				gate.Utils.featureMap("grammarURL", new File("weather.jape").toURI().toURL(), "encoding", "UTF-8"));

		pipeline.add(tokeniser);
		pipeline.add(gazetteer);
		pipeline.add(neTrans);
		pipeline.add(jape);
		Corpus corpus = gate.Factory.newCorpus(null);
		Document doc = gate.Factory.newDocument("Will it be sun today in Yabmol?");
		DocumentContent dc = doc.getContent();
		corpus.add(doc);
		pipeline.setCorpus(corpus);
		pipeline.execute();
		System.out.println("Found annotations of the following types: " + doc.getAnnotations().getAllTypes());

		AnnotationSet annSet = doc.getAnnotations();
		String type = "Weather";

		// Get all location annotations
		AnnotationSet locationsSet = annSet.get(type);

		// Sort the annotations
		List<Annotation> locationsList = new ArrayList(locationsSet);

		String json = JsonReader.getJsonWithData("Yabmol");

		if (locationsList.isEmpty()) {
			System.out.println("No Location");
		} else {
			System.out.println("There's location");
		}

		String condition = null;

		for (Annotation a : locationsList) {
			System.out.println(a.getType() + " : " + Utils.stringFor(doc, a));
			System.out.println(a.toString());
			condition = Utils.stringFor(doc, a);
		}
		if (condition.equals("sun")) {
			condition = new String("clear");
		}

		boolean check = JsonReader.checkForCondition(json, condition);

		if (check == true) {
			System.out.println("Yes");
		} else {
			System.out.println("No");
		}

	}
}
