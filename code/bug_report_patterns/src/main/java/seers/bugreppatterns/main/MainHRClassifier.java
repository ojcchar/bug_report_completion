package seers.bugreppatterns.main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.processor.SystemProcessor;

public class MainHRClassifier {

	private static final Logger LOGGER = LoggerFactory.getLogger(MainHRClassifier.class);

	public static final String DATA_FOLDER = "DATA_FOLDER";
	public static final String SYSTEM = "SYSTEM";
	public static final String GRANULARITY = "GRANULARITY";
	public static final String PREDICTION_WRITER = "PREDICTION_WRITER";
	public static final String PATTERNS = "PATTERNS";
	public static final String FEATURES_WRITER = "FEATURES_WRITER";

	public static void main(String[] args) throws Exception {

		// ------------------------------------------

		String dataFolder = args[0];
		String granularity = args[1];
		String[] systems = args[2].split(",");
		String outputFolder = args[3];
		String patternsFile = args[4];

		// ------------------------------------------

		List<PatternMatcher> patterns = getPatterns(patternsFile);

		LOGGER.debug("#patterns: " + patterns.size());

		// ------------------------------------------

		try (CsvWriter csvw1 = new CsvWriterBuilder(
				new FileWriter(outputFolder + File.separator + "output-prediction-" + granularity + ".csv"))
						.separator(';').build();
				CsvWriter csvw2 = new CsvWriterBuilder(
						new FileWriter(outputFolder + File.separator + "output-pre-features-" + granularity + ".csv"))
								.separator(';').build();) {

			ThreadParameters params = new ThreadParameters();
			params.addParam(DATA_FOLDER, dataFolder);
			params.addParam(PREDICTION_WRITER, csvw1);
			params.addParam(PATTERNS, patterns);
			params.addParam(GRANULARITY, granularity);
			params.addParam(FEATURES_WRITER, csvw2);

			ThreadExecutor.executeOneByOne(Arrays.asList(systems), SystemProcessor.class, params, systems.length);

		}

		// ------------------------------------------

	}

	private static List<PatternMatcher> getPatterns(String patternsFile)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		List<String> inputPatterns = FileUtils.readLines(new File(patternsFile));
		List<String> allPatterns = FileUtils.readLines(new File("patterns.csv"));

		List<PatternMatcher> patterns = new ArrayList<>();

		for (String patternName : inputPatterns) {

			Optional<String> patternNameClassIndex = allPatterns.stream().filter(p -> p.startsWith(patternName + ";"))
					.findFirst();
			if (!patternNameClassIndex.isPresent()) {
				LOGGER.warn("Pattern not implemented: " + patternName);
			} else {

				String[] split = patternNameClassIndex.get().split(" ");

				String patternNameClass = split[0];
				Integer code = Integer.valueOf(split[1]);

				String className = patternNameClass.split(";")[1];
				if (patternName.contains("_EB_")) {
					className = "seers.bugreppatterns.pattern.eb." + className;
				} else if (patternName.contains("_SR_")) {
					className = "seers.bugreppatterns.pattern.sr." + className;
				}

				Class<?> class1 = Class.forName(className);
				PatternMatcher pattern = (PatternMatcher) class1.newInstance();
				pattern.setCode(code);
				patterns.add(pattern);
			}
		}

		return patterns;
	}

}
