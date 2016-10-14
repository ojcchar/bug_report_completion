package seers.bugreppatterns.main;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;

public class FalsePositiveSummarizer {

	public static void main(String[] args) throws Exception {

		String sampleFilePath = "test_data\\output\\eval\\data_all_sr_sample_B.csv";
		String outputPatternsPath = "test_data\\output\\output-patterns-P.csv";
		String[] patternTypes = { "SR" };
		if (args.length != 0) {
			sampleFilePath = args[0];
			outputPatternsPath = args[1];
			patternTypes = args[2].split(",");
		}

		List<List<String>> falsePositivesEntries = readFalsePositives(sampleFilePath);
		HashMap<String, HashMap<String, List<String>>> predictions = readPatternsPredictions(outputPatternsPath,
				patternTypes);

		HashMap<String, Set<String>> predictionsByBug = new HashMap<>();

		for (List<String> fpEntry : falsePositivesEntries) {

			String system = fpEntry.get(0);
			String bugId = fpEntry.get(1);

			String sysBugIdKey = getSysBugIdKey(system, bugId);
			HashMap<String, List<String>> sentences = predictions.get(sysBugIdKey);

			for (Entry<String, List<String>> sentence : sentences.entrySet()) {
				for (String pattern : sentence.getValue()) {
					System.out.println(system + ";" + bugId + ";" + sentence.getKey() + ";" + pattern);

					Set<String> patterns = predictionsByBug.get(sysBugIdKey);
					if (patterns == null) {
						patterns = new HashSet<>();
						predictionsByBug.put(sysBugIdKey, patterns);
					}
					patterns.add(pattern);
				}
			}

		}

		// --------------------------------
		
		System.out.println("BY BUG---------------------------");

		for (Entry<String, Set<String>> fpEntry : predictionsByBug.entrySet()) {

			Set<String> patterns = fpEntry.getValue();

			for (String pattern : patterns) {

				System.out.println(fpEntry.getKey() + ";" + pattern);
			}

		}

	}

	private static String getSysBugIdKey(String system, String bugId) {
		return system + ";" + bugId;
	}

	private static HashMap<String, HashMap<String, List<String>>> readPatternsPredictions(String outputPatternsPath,
			String[] patternTypes) throws IOException {

		HashMap<String, HashMap<String, List<String>>> predictions = new HashMap<>();
		List<List<String>> allLines = null;

		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(
				new InputStreamReader(new FileInputStream(outputPatternsPath), "Cp1252"), csvParser)) {
			allLines = csvReader.readAll();
		}

		for (List<String> line : allLines) {

			String system = line.get(0);
			String bugId = line.get(1);

			// -----------------------------------------

			String sysBugIdKey = getSysBugIdKey(system, bugId);

			HashMap<String, List<String>> sentences = predictions.get(sysBugIdKey);
			if (sentences == null) {
				sentences = new HashMap<>();
				predictions.put(sysBugIdKey, sentences);
			}

			// -----------------------------------------

			String instanceId = line.get(2);

			List<String> patterns = sentences.get(instanceId);
			if (patterns == null) {
				patterns = new ArrayList<>();
				sentences.put(instanceId, patterns);
			}

			// -----------------------------------------

			List<String> patternList = line.subList(3, line.size());

			for (int i = 0; i < patternList.size(); i += 2) {
				String pattern = patternList.get(i);
				if (!pattern.isEmpty() && matchPatternTypes(pattern, patternTypes)) {
					patterns.add(pattern);
				}
			}
		}

		return predictions;
	}

	private static boolean matchPatternTypes(String pattern, String[] patternTypes) {
		return Arrays.stream(patternTypes).anyMatch(pattType -> pattern.contains("_" + pattType + "_"));
	}

	private static List<List<String>> readFalsePositives(String sampleFilePath) throws IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(sampleFilePath), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			List<List<String>> falsePositivesEntries = allLines.stream().filter(line -> line.get(5).equals("fp"))
					.collect(Collectors.toList());

			return falsePositivesEntries;

		}
	}

}
