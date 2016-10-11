package seers.bugreppatterns.main;

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
import seers.bugreppatterns.pattern.predictor.CombinationPredictor;
import seers.bugreppatterns.pattern.predictor.LabelPredictor;
import seers.bugreppatterns.processor.SystemProcessor;

public class HeuristicsClassifier {

	private static final Logger LOGGER = LoggerFactory.getLogger(HeuristicsClassifier.class);

	public enum Predictor {
		ANY_MATCH, COMBIN
	}

	public static final String DATA_FOLDER = "DATA_FOLDER";
	public static final String SYSTEM = "SYSTEM";
	public static final String GRANULARITY = "GRANULARITY";
	public static final String PREDICTION_WRITER = "PREDICTION_WRITER";
	public static final String PATTERNS = "PATTERNS";
	public static final String FEATURES_WRITER = "FEATURES_WRITER";
	public static final String PREDICTOR = "PREDICTOR";
	public static final String FEATURES_WRITER2 = "FEATURES_WRITER2";

	private String dataFolder;
	private String granularity;
	private String[] systems;
	private String outputFolder;
	private Predictor predictionMethod;
	private List<PatternMatcher> patterns;
	

	public HeuristicsClassifier(String dataFolder, String granularity, String[] systems, String outputFolder,
			Predictor predictionMethod, List<PatternMatcher> patterns) {
		super();
		this.dataFolder = dataFolder;
		this.granularity = granularity;
		this.systems = systems;
		this.outputFolder = outputFolder;
		this.predictionMethod = predictionMethod;
		this.patterns = patterns;
	}

	public void runClassifier() throws Exception, IOException {
		LOGGER.debug("#patterns: " + patterns.size());

		// ------------------------------------------

		try (CsvWriter csvw1 = new CsvWriterBuilder(
				new FileWriter(outputFolder + File.separator + "output-prediction-" + granularity + ".csv"))
						.separator(';').build();
				CsvWriter csvw2 = new CsvWriterBuilder(
						new FileWriter(outputFolder + File.separator + "output-pre-features-" + granularity + ".csv"))
								.separator(';').build();
				CsvWriter csvw3 = new CsvWriterBuilder(
						new FileWriter(outputFolder + File.separator + "output-patterns-" + granularity + ".csv"))
								.separator(';').build();) {

			ThreadParameters params = new ThreadParameters();
			params.addParam(DATA_FOLDER, dataFolder);
			params.addParam(PREDICTION_WRITER, csvw1);
			params.addParam(PATTERNS, patterns);
			params.addParam(GRANULARITY, granularity);
			params.addParam(FEATURES_WRITER, csvw2);
			params.addParam(FEATURES_WRITER2, csvw3);

			LabelPredictor predictor = getPredictor(predictionMethod);
			params.addParam(PREDICTOR, predictor);

			// titles
			String[] title = new String[] { "system", "bug_id", "instance_id", "is_ob", "is_eb", "is_sr" };
			csvw1.writeNext(Arrays.asList(title));

			ThreadExecutor.executeOneByOne(Arrays.asList(systems), SystemProcessor.class, params, systems.length * 2);

		}

		LOGGER.debug("Done " + granularity + "!");
	}

	private LabelPredictor getPredictor(Predictor predictionMethod) {
		switch (predictionMethod) {
		case ANY_MATCH:
			return new AnyMatchPredictor();
		case COMBIN:
			return new CombinationPredictor();
		default:
			break;
		}
		return null;
	}
}
