package seers.bugreppatterns.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.EnumUtils;
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

public class MainHRClassifier {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainHRClassifier.class);

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

	public static void main(String[] args) throws Exception {

		// ------------------------------------------

		String dataFolder = args[0];
		String granularity = args[1];
		String[] systems = args[2].split(",");
		String outputFolder = args[3];
		Predictor predictionMethod = EnumUtils.getEnum(Predictor.class, args[4]);
		String pathFilePatterns = args[5];

		// ------------------------------------------

		List<PatternMatcher> patterns = loadPatterns(new File(pathFilePatterns));

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

		// ------------------------------------------

	}

	private static LabelPredictor getPredictor(Predictor predictionMethod) {
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

	private static List<PatternMatcher> loadPatterns(File filePatterns)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		List<String> allPatterns = FileUtils.readLines(filePatterns);

		List<PatternMatcher> patterns = new ArrayList<>();

		for (String patternNameClassIndex : allPatterns) {

			String[] split = patternNameClassIndex.split(" ");

			String patternNameClass = split[0];
			Integer code = Integer.valueOf(split[1]);

			String[] split2 = patternNameClass.split(";");
			String patternName = split2[0];
			String className = split2[1];

			if (patternName.contains("_EB_")) {
				className = "seers.bugreppatterns.pattern.eb." + className;
			} else if (patternName.contains("_SR_")) {
				className = "seers.bugreppatterns.pattern.sr." + className;
			} else if (patternName.contains("_OB_")) {
				className = "seers.bugreppatterns.pattern.ob." + className;
			}

			Class<?> class1 = Class.forName(className);
			PatternMatcher pattern = (PatternMatcher) class1.newInstance();
			pattern.setCode(code);
			pattern.setName(patternName);

			patterns.add(pattern);
		}

		return patterns;
	}

}
