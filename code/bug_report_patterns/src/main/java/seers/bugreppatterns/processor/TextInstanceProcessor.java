package seers.bugreppatterns.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

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
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;

public abstract class TextInstanceProcessor extends ThreadProcessor {

	protected String dataFolder;
	protected CsvWriter predictionWriter;
	protected List<PatternMatcher> patterns;
	protected List<File> files;
	protected String granularity;
	protected String system;
	private CsvWriter featuresWriter;
	protected LabelPredictor predictor;
	private CsvWriter featuresWriter2;

	public TextInstanceProcessor(ThreadParameters params) {
		super(params);

		dataFolder = params.getStringParam(MainHRClassifier.DATA_FOLDER);
		predictionWriter = params.getParam(CsvWriter.class, MainHRClassifier.PREDICTION_WRITER);
		patterns = params.getListParam(PatternMatcher.class, MainHRClassifier.PATTERNS);
		files = params.getListParam(File.class, ThreadExecutor.ELEMENTS_PARAM);
		granularity = params.getStringParam(MainHRClassifier.GRANULARITY);
		system = params.getStringParam(MainHRClassifier.SYSTEM);
		featuresWriter = params.getParam(CsvWriter.class, MainHRClassifier.FEATURES_WRITER);
		featuresWriter2 = params.getParam(CsvWriter.class, MainHRClassifier.FEATURES_WRITER2);
		predictor = params.getParam(LabelPredictor.class, MainHRClassifier.PREDICTOR);
	}

	protected void writePrediction(String bugRepId, String instanceId, Labels labels) throws Exception {

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

		// --------------------------

		List<Entry<PatternMatcher, Integer>> entrySet = new ArrayList<>(patternMatches.entrySet());
		entrySet.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

		List<String> nextLine2 = new ArrayList<>();
		nextLine2.add(system);
		nextLine2.add(bugRepId);
		nextLine2.add(instanceId);

		entrySet.forEach(e -> {
			nextLine2.add(e.getKey().getName());
			nextLine2.add(String.valueOf(e.getValue()));
		});

		featuresWriter2.writeNext(nextLine2);

	}

	protected Paragraph parseParagraph(String bugId, DescriptionParagraph paragraph) {

		Paragraph par = new Paragraph(paragraph.getId());

		List<DescriptionSentence> elements = paragraph.getSentences();
		if (elements != null) {
			for (DescriptionSentence descSentence : elements) {

				String sentenceId = descSentence.getId();
				String sentenceTxt = descSentence.getValue();

				Sentence sentence = SentenceUtils.parseSentence(sentenceId, sentenceTxt);
				if (sentence != null) {
					par.addSentence(sentence);
				}

			}
		}

		return par;
	}

}
