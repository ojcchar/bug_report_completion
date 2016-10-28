package seers.bugreppatterns.main.prediction;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.AnyMatchPredictor;
import seers.bugreppatterns.pattern.predictor.LabelPredictor;
import seers.bugreppatterns.pattern.predictor.coocurrence.CooccurringPattern;
import seers.bugreppatterns.pattern.predictor.coocurrence.CoocurrencePredictor;
import seers.bugreppatterns.pattern.predictor.coocurrence.StrictCoocurrencePredictor;
import seers.bugreppatterns.processor.SystemProcessor;

public class HeuristicsClassifier {

	private static final Logger LOGGER = LoggerFactory.getLogger(HeuristicsClassifier.class);

	public enum Predictor {
		ANY_MATCH, COOCCUR, COOCCUR_STRICT1, COOCCUR_STRICT2
	}

	public static final String DATA_FOLDER = "DATA_FOLDER";
	public static final String SYSTEM = "SYSTEM";
	public static final String GRANULARITY = "GRANULARITY";
	public static final String PREDICTION_WRITER = "PREDICTION_WRITER";
	public static final String PATTERNS = "PATTERNS";
	public static final String PRE_FEATURES_WRITER = "PRE_FEATURES_WRITER";
	public static final String PREDICTOR = "PREDICTOR";
	public static final String PATTERN_PRE_FEATURES_WRITER = "PATTERN_PRE_FEATURES_WRITER";

	private String dataFolder;
	private String granularity;
	private String[] systems;
	private String outputFolder;
	private Predictor predictionMethod;
	private List<PatternMatcher> patterns;
	private String configFolder;
	private boolean includeIndivFeatures;

	public HeuristicsClassifier(String dataFolder, String granularity, String[] systems, String outputFolder,
			Predictor predictionMethod, List<PatternMatcher> patterns, String configFolder,
			boolean includeIndivFeatures) {
		super();
		this.dataFolder = dataFolder;
		this.granularity = granularity;
		this.systems = systems;
		this.outputFolder = outputFolder;
		this.predictionMethod = predictionMethod;
		this.patterns = patterns;
		this.configFolder = configFolder;
		this.includeIndivFeatures = includeIndivFeatures;
	}

	public void runClassifier() throws Exception {
		LOGGER.debug("#patterns: " + patterns.size());
		LOGGER.debug("predictor: " + predictionMethod);

		// ------------------------------------------

		try (CsvWriter predictionWriter = createWriter("output-prediction");
				CsvWriter prefeaturesWriter = createWriter("output-pre-features");
				CsvWriter patternsPreFeaturesWriter = createWriter("output-patterns");
				CsvWriter featuresDefinitionsWriter = createWriter("feature-definitions", ' ');) {

			ThreadParameters params = new ThreadParameters();
			params.addParam(DATA_FOLDER, dataFolder);
			params.addParam(PREDICTION_WRITER, predictionWriter);
			params.addParam(PATTERNS, patterns);
			params.addParam(GRANULARITY, granularity);
			params.addParam(PRE_FEATURES_WRITER, prefeaturesWriter);
			params.addParam(PATTERN_PRE_FEATURES_WRITER, patternsPreFeaturesWriter);

			LabelPredictor predictor = getPredictor();
			params.addParam(PREDICTOR, predictor);

			// ------------------------------------------------

			writeFeatures(featuresDefinitionsWriter, predictor);

			// ------------------------------------------------

			// titles
			String[] title = new String[] { "system", "bug_id", "instance_id", "is_ob", "is_eb", "is_sr" };
			predictionWriter.writeNext(Arrays.asList(title));

			ThreadExecutor.executeOneByOne(Arrays.asList(systems), SystemProcessor.class, params, systems.length * 2);

		}

		LOGGER.debug("Done " + granularity + "!");
	}

	private CsvWriter createWriter(String prefixFileName) throws IOException {
		return createWriter(prefixFileName, ';');
	}

	private CsvWriter createWriter(String prefixFileName, char separator) throws IOException {
		return new CsvWriterBuilder(
				new FileWriter(outputFolder + File.separator + prefixFileName + "-" + granularity + ".csv"))
						.separator(separator).quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
	}

	private void writeFeatures(CsvWriter featuresDefinitionsWriter, LabelPredictor predictor) {

		// ------------------------

		if (includeIndivFeatures) {
			for (PatternMatcher patternMatcher : patterns) {
				List<String> nextLine = Arrays
						.asList(new String[] { patternMatcher.getName(), patternMatcher.getCode().toString() });
				featuresDefinitionsWriter.writeNext(nextLine);
			}
		}

		for (CooccurringPattern occurrringPatterns : predictor.getCooccurringFeatures()) {
			List<String> nextLine = Arrays
					.asList(new String[] { occurrringPatterns.getName(), occurrringPatterns.getId().toString() });
			featuresDefinitionsWriter.writeNext(nextLine);
		}

	}

	private LabelPredictor getPredictor() throws IOException {
		switch (predictionMethod) {
		case ANY_MATCH:
			return new AnyMatchPredictor(patterns, granularity, includeIndivFeatures);
		case COOCCUR:
			return new CoocurrencePredictor(patterns, granularity, includeIndivFeatures, configFolder);
		case COOCCUR_STRICT1:
			return new StrictCoocurrencePredictor(patterns, granularity, includeIndivFeatures, configFolder, false);
		case COOCCUR_STRICT2:
			return new StrictCoocurrencePredictor(patterns, granularity, includeIndivFeatures, configFolder, true);
		default:
			break;
		}
		return null;
	}
}
