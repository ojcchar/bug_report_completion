package seers.bugreppatterns.processor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
	protected String system;
	private CsvWriter preFeaturesWriter;
	protected LabelPredictor predictor;
	private CsvWriter patternsPreFeaturesWriter;

	public TextInstanceProcessor(ThreadParameters params) {
		super(params);

		dataFolder = params.getStringParam(HeuristicsClassifier.DATA_FOLDER);
		predictionWriter = params.getParam(CsvWriter.class, HeuristicsClassifier.PREDICTION_WRITER);
		patterns = params.getListParam(PatternMatcher.class, HeuristicsClassifier.PATTERNS);
		files = params.getListParam(File.class, ThreadExecutor.ELEMENTS_PARAM);
		system = params.getStringParam(HeuristicsClassifier.SYSTEM);
		preFeaturesWriter = params.getParam(CsvWriter.class, HeuristicsClassifier.PRE_FEATURES_WRITER);
		patternsPreFeaturesWriter = params.getParam(CsvWriter.class, HeuristicsClassifier.PATTERN_PRE_FEATURES_WRITER);
		predictor = params.getParam(LabelPredictor.class, HeuristicsClassifier.PREDICTOR);
	}

	protected void writePrediction(String bugRepId, String instanceId, Labels labels) throws Exception {

		List<String> nextLine = Arrays.asList(
				new String[] { system, bugRepId, instanceId, labels.getIsOB(), labels.getIsEB(), labels.getIsSR() });
		predictionWriter.writeNext(nextLine);
	}

	protected void writePreFeatures(String bugRepId, String instanceId, List<PatternFeature> features) {
		List<String> nextLine = new ArrayList<>();
		nextLine.add(system);
		nextLine.add(bugRepId);
		nextLine.add(instanceId);

		features.forEach(f -> {
			nextLine.add(f.getId() + ":" + f.getValue());
		});

		preFeaturesWriter.writeNext(nextLine);

		// --------------------------

		List<String> nextLine2 = new ArrayList<>();
		nextLine2.add(system);
		nextLine2.add(bugRepId);
		nextLine2.add(instanceId);

		features.forEach(e -> {
			nextLine2.add(e.getName());
			nextLine2.add(e.getValue().toString());
		});

		patternsPreFeaturesWriter.writeNext(nextLine2);

	}

}
