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
import seers.bugreppatterns.main.prediction.HeuristicsClassifier;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.LabelPredictor;
import seers.bugreppatterns.pattern.predictor.Labels;

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

		dataFolder = params.getStringParam(HeuristicsClassifier.DATA_FOLDER);
		predictionWriter = params.getParam(CsvWriter.class, HeuristicsClassifier.PREDICTION_WRITER);
		patterns = params.getListParam(PatternMatcher.class, HeuristicsClassifier.PATTERNS);
		files = params.getListParam(File.class, ThreadExecutor.ELEMENTS_PARAM);
		granularity = params.getStringParam(HeuristicsClassifier.GRANULARITY);
		system = params.getStringParam(HeuristicsClassifier.SYSTEM);
		featuresWriter = params.getParam(CsvWriter.class, HeuristicsClassifier.FEATURES_WRITER);
		featuresWriter2 = params.getParam(CsvWriter.class, HeuristicsClassifier.FEATURES_WRITER2);
		predictor = params.getParam(LabelPredictor.class, HeuristicsClassifier.PREDICTOR);
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

}
