package seers.bugreppatterns.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import net.quux00.simplecsv.CsvWriter;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.threads.processor.ThreadProcessor;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.entity.xml.DescriptionParagraph;
import seers.bugreppatterns.entity.xml.DescriptionSentence;
import seers.bugreppatterns.main.MainHRClassifier;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.LabelPredictor;
import seers.bugreppatterns.pattern.predictor.Labels;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public abstract class TextInstanceProcessor extends ThreadProcessor {

	protected String dataFolder;
	protected CsvWriter predictionWriter;
	protected List<PatternMatcher> patterns;
	protected List<File> files;
	protected String granularity;
	protected String system;
	private CsvWriter featuresWriter;
	private LabelPredictor predictor;

	public TextInstanceProcessor(ThreadParameters params) {
		super(params);

		dataFolder = params.getStringParam(MainHRClassifier.DATA_FOLDER);
		predictionWriter = params.getParam(CsvWriter.class, MainHRClassifier.PREDICTION_WRITER);
		patterns = params.getListParam(PatternMatcher.class, MainHRClassifier.PATTERNS);
		files = params.getListParam(File.class, ThreadExecutor.ELEMENTS_PARAM);
		granularity = params.getStringParam(MainHRClassifier.GRANULARITY);
		system = params.getStringParam(MainHRClassifier.SYSTEM);
		featuresWriter = params.getParam(CsvWriter.class, MainHRClassifier.FEATURES_WRITER);
		predictor = params.getParam(LabelPredictor.class, MainHRClassifier.PREDICTOR);
	}

	protected void writePrediction(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches) throws Exception {

		Labels labels = predictor.predictLabels(bugRepId, instanceId, patternMatches);

		List<String> nextLine = Arrays.asList(
				new String[] { system, bugRepId, instanceId, labels.getIsOB(), labels.getIsEB(), labels.getIsSR() });
		predictionWriter.writeNext(nextLine);
	}

	protected void writeFeatures(String bugRepId, String instanceId,
			LinkedHashMap<PatternMatcher, Integer> patternMatches) {
		List<String> nextLine = new ArrayList<>();
		nextLine.add(system);
		nextLine.add(bugRepId);
		nextLine.add(instanceId);

		patternMatches.forEach((k, v) -> {
			nextLine.add(k.getCode() + ":" + v);
		});

		featuresWriter.writeNext(nextLine);
	}

	protected Paragraph parseParagraph(String bugId, DescriptionParagraph paragraph) {

		Paragraph par = new Paragraph(paragraph.getId());

		List<DescriptionSentence> elements = paragraph.getSentences();
		if (elements != null) {
			for (DescriptionSentence descSentence : elements) {

				String sentenceTxt = descSentence.getValue();
				List<Sentence> sentences = TextProcessor.processText(sentenceTxt);

				if (!sentences.isEmpty()) {
					List<Token> allTokens = TextProcessor.getAllTokens(sentences);

					Sentence sentence = new Sentence(descSentence.getId(), allTokens);

					par.addSentence(sentence);
				}

			}
		}

		return par;
	}

}
