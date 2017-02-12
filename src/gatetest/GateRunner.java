package gatetest;

import java.io.File;

import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;

public class GateRunner {

	SerialAnalyserController pipeline;

	public void runner() throws Exception {
		System.setProperty("gate.home", "/Applications/GATE_Developer_8.3/");

		Gate.init();

		Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "ANNIE").toURI().toURL());
		Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Tools").toURI().toURL());
		this.pipeline = (SerialAnalyserController) gate.Factory.createResource("gate.creole.SerialAnalyserController");
		LanguageAnalyser docResetter = (LanguageAnalyser) gate.Factory
				.createResource("gate.creole.annotdelete.AnnotationDeletePR");
		LanguageAnalyser sentenceSplitter = (LanguageAnalyser) gate.Factory
				.createResource("gate.creole.splitter.SentenceSplitter");
		LanguageAnalyser tokeniser = (LanguageAnalyser) gate.Factory
				.createResource("gate.creole.tokeniser.DefaultTokeniser");
		LanguageAnalyser gazetteer = (LanguageAnalyser) gate.Factory
				.createResource("gate.creole.gazetteer.DefaultGazetteer");
		LanguageAnalyser neTrans = (LanguageAnalyser) gate.Factory.createResource("gate.creole.ANNIETransducer");
		LanguageAnalyser posTagger = (LanguageAnalyser) gate.Factory.createResource("gate.creole.POSTagger");
		LanguageAnalyser morph = (LanguageAnalyser) gate.Factory.createResource("gate.creole.morph.Morph");
		LanguageAnalyser orthoMatcher = (LanguageAnalyser) gate.Factory
				.createResource("gate.creole.orthomatcher.OrthoMatcher");
		LanguageAnalyser jape = (LanguageAnalyser) gate.Factory.createResource("gate.creole.Transducer",
				gate.Utils.featureMap("grammarURL", new File("japeRules/main.jape").toURI().toURL(), "encoding", "UTF-8"));

		this.pipeline.add(docResetter);
		this.pipeline.add(tokeniser);
		this.pipeline.add(sentenceSplitter);
		this.pipeline.add(posTagger);
		this.pipeline.add(morph);
		this.pipeline.add(gazetteer);
		this.pipeline.add(neTrans);
		this.pipeline.add(orthoMatcher);
		this.pipeline.add(jape);

	}

	public String test(String text) throws ResourceInstantiationException, ExecutionException {
		Corpus corpus = gate.Factory.newCorpus(null);
		Document doc = gate.Factory.newDocument(text);
		corpus.add(doc);
		this.pipeline.setCorpus(corpus);
		this.pipeline.execute();

		System.out.println("Found annotations of the following types: " + doc.getAnnotations().getAllTypes());

		AnnotationSet annSet = doc.getAnnotations();
		GateParser gateParser = new GateParser(doc);

		if (!gateParser.isQuestion(annSet)) {
			return "This is not weather question.";

		}

		String date = GateParser.getDate(annSet);

		String condition = gateParser.isSpecificConditionCheck(annSet);
		String temperature = gateParser.isCheckForTemperatureOrHumidity(annSet);

		if (condition != null) {
			return JsonReader.checkForCondition(JsonReader.getJsonWithData(gateParser.getLocation(annSet)), condition,
					date);
		} else if (temperature != null) {
			return JsonReader.checkForTemperatureOrHumidity(JsonReader.getJsonWithData(gateParser.getLocation(annSet)),
					temperature, date);
		} else if (gateParser.isCheckForForecat(annSet)) {
			return JsonReader.returnForecast(JsonReader.getJsonWithData(gateParser.getLocation(annSet)), date);
		} else if (gateParser.getAddExtraConditions(annSet)) {
			return JsonReader.getUmbrella(JsonReader.getJsonWithData(gateParser.getLocation(annSet)), date);
		} else {
			return "I have no idea what you want";
		}

	}
}
