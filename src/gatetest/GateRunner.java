package gatetest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Gate;
import gate.LanguageAnalyser;
import gate.creole.SerialAnalyserController;

public class GateRunner {

	public void runner(String text) throws Exception {
		System.setProperty("gate.home", "/Applications/GATE_Developer_8.3/");

		Gate.init();

		Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "ANNIE").toURI().toURL());
		Gate.getCreoleRegister().registerDirectories(new File(Gate.getPluginsHome(), "Tools").toURI().toURL());
		SerialAnalyserController pipeline = (SerialAnalyserController) gate.Factory
				.createResource("gate.creole.SerialAnalyserController");
		LanguageAnalyser sentenceSplitter = (LanguageAnalyser) gate.Factory
				.createResource("gate.creole.splitter.SentenceSplitter");
		LanguageAnalyser tokeniser = (LanguageAnalyser) gate.Factory
				.createResource("gate.creole.tokeniser.DefaultTokeniser");
		LanguageAnalyser gazetteer = (LanguageAnalyser) gate.Factory
				.createResource("gate.creole.gazetteer.DefaultGazetteer");
		LanguageAnalyser neTrans = (LanguageAnalyser) gate.Factory.createResource("gate.creole.ANNIETransducer");

		LanguageAnalyser posTagger = (LanguageAnalyser) gate.Factory.createResource("gate.creole.POSTagger");
		LanguageAnalyser morph = (LanguageAnalyser) gate.Factory.createResource("gate.creole.morph.Morph");
		LanguageAnalyser annieGazetteer = (LanguageAnalyser) gate.Factory
				.createResource("gate.creole.gazetteer.DefaultGazetteer");

		LanguageAnalyser jape = (LanguageAnalyser) gate.Factory.createResource("gate.creole.Transducer",
				gate.Utils.featureMap("grammarURL", new File("weather.jape").toURI().toURL(), "encoding", "UTF-8"));

		pipeline.add(sentenceSplitter);
		pipeline.add(tokeniser);
		pipeline.add(gazetteer);
		pipeline.add(neTrans);
		pipeline.add(posTagger);
		pipeline.add(morph);
		pipeline.add(annieGazetteer);
		pipeline.add(jape);
		Corpus corpus = gate.Factory.newCorpus(null);
		Document doc = gate.Factory.newDocument(text);
		corpus.add(doc);
		pipeline.setCorpus(corpus);
		pipeline.execute();

		System.out.println("Found annotations of the following types: " + doc.getAnnotations().getAllTypes());

		AnnotationSet annSet = doc.getAnnotations();

		GateParser gateParser = new GateParser(doc);
		JsonReader.checkForCondition(JsonReader.getJsonWithData(gateParser.getLocation(annSet)),
				gateParser.isSpecificConditionCheck(annSet));
		System.out.println(gateParser.getDate(annSet));
		
		Calendar calendar = Calendar.getInstance();
	
		Date date = calendar.getTime();
		
		while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
			calendar.add(Calendar.DATE, 1);
		}
		
		System.out.println(calendar.getTime().toString());
		System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));


	
	}
}
