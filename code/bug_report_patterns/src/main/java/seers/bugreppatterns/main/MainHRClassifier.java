package seers.bugreppatterns.main;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.EnumUtils;

import seers.bugreppatterns.main.HeuristicsClassifier.Predictor;
import seers.bugreppatterns.pattern.PatternMatcher;

public class MainHRClassifier {

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

		// ------------------------------------------

		HeuristicsClassifier classifier = new HeuristicsClassifier(dataFolder, granularity, systems, outputFolder,
				predictionMethod, patterns);
		classifier.runClassifier();

		// ------------------------------------------

	}

	public static List<PatternMatcher> loadPatterns(File filePatterns)
			throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {

		List<String> allPatterns = FileUtils.readLines(filePatterns);

		List<PatternMatcher> patterns = new ArrayList<>();
		HashMap<String, Integer> patternMapping = new HashMap<>();

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

			Integer num = patternMapping.get(className);
			if (num == null) {
				patternMapping.put(className, 1);
			} else {
				patternMapping.put(className, num + 1);
			}

			patterns.add(pattern);
		}

		List<Entry<String, Integer>> invalidMappings = patternMapping.entrySet().stream()
				.filter(entry -> entry.getValue() > 1).collect(Collectors.toList());

		if (!invalidMappings.isEmpty()) {
			throw new RuntimeException("The following classes have more than 1 pattern assigned: " + invalidMappings);
		}

		return patterns;
	}

}
