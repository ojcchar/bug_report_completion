package seers.bugreppatterns.main;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.EnumUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seers.bugreppatterns.main.HeuristicsClassifier.Predictor;
import seers.bugreppatterns.pattern.PatternMatcher;

public class MassiveClassifierRunner {

	private static final Logger LOGGER = LoggerFactory.getLogger(MassiveClassifierRunner.class);

	public static void main(String[] args) throws Exception {

		// arguments
		String dataFolder = args[0];
		String[] granularities = args[1].split(",");
		String[] systems = args[2].split(",");
		String outputFolder = args[3];
		Predictor predictionMethod = EnumUtils.getEnum(Predictor.class, args[4]);
		String pathFilePatterns = args[5];

		// read the list of patterns
		List<PatternMatcher> patterns = MainHRClassifier.loadPatterns(new File(pathFilePatterns));

		// for each patterns
		for (int i = 0; i < patterns.size(); i++) {

			// exclude the pattern from the list
			List<PatternMatcher> filteredPatterns = new LinkedList<>(patterns);
			PatternMatcher pattern = filteredPatterns.remove(i);

			LOGGER.debug("** Excluding " + pattern.getName());

			// create the output folder
			String outputFolder2 = outputFolder + File.separator + pattern.getName();
			File folder = new File(outputFolder2);
			if (!folder.exists()) {
				boolean created = folder.mkdir();
				if (!created) {
					LOGGER.warn("Could not create out folder for pattern " + pattern.getName());
					continue;
				}
			}

			// run the classifier
			for (String granularity : granularities) {

				HeuristicsClassifier classifier = new HeuristicsClassifier(dataFolder, granularity, systems,
						outputFolder2, predictionMethod, filteredPatterns);
				classifier.runClassifier();

			}
		}

	}

}
