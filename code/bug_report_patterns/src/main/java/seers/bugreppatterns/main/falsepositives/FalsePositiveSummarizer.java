package seers.bugreppatterns.main.falsepositives;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.threads.ThreadExecutor;
import seers.appcore.threads.processor.ThreadParameters;

public class FalsePositiveSummarizer {

	public static void main(String[] args) throws Exception {

		String sampleFilePath = "test_data\\output\\eval\\data_all_sr_sample_B.csv";
		String outputPatternsPath = "test_data\\output\\output-patterns-P.csv";
		String[] patternTypes = { "SR" };
		String[] inputPatterns = { };
		String outputFile = "false-positives.csv";
		if (args.length != 0) {
			sampleFilePath = args[0];
			outputPatternsPath = args[1];
			patternTypes = args[2].split(",");
			inputPatterns = args[3].split(",");
		}

		List<List<String>> falsePositivesEntries = readFalsePositives(sampleFilePath);
		ConcurrentHashMap<String, HashMap<BugInstance, List<String>>> predictions = readPatternsPredictions(
				outputPatternsPath, patternTypes, inputPatterns);

		HashMap<String, Set<String>> predictionsByBug = new HashMap<>();

		try (CsvWriter cw = new CsvWriterBuilder(new FileWriter(outputFile)).separator(';').build()) {

			for (List<String> fpEntry : falsePositivesEntries) {

				String system = fpEntry.get(0);
				String bugId = fpEntry.get(1);

				String sysBugIdKey = getSysBugIdKey(system, bugId);
				HashMap<BugInstance, List<String>> sentences = predictions.get(sysBugIdKey);

				for (Entry<BugInstance, List<String>> sentence : sentences.entrySet()) {
					for (String pattern : sentence.getValue()) {
						BugInstance instance = sentence.getKey();

						List<String> asList = Arrays.asList(new String[] { system, bugId, instance.id, pattern, instance.text });
						cw.writeNext(asList);

						Set<String> patterns = predictionsByBug.get(sysBugIdKey);
						if (patterns == null) {
							patterns = new HashSet<>();
							predictionsByBug.put(sysBugIdKey, patterns);
						}
						patterns.add(pattern);
					}
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

	public static String getSysBugIdKey(String system, String bugId) {
		return system + ";" + bugId;
	}

	private static ConcurrentHashMap<String, HashMap<BugInstance, List<String>>> readPatternsPredictions(
			String outputPatternsPath, String[] patternTypes, String[] inputPatterns) throws Exception {

		ConcurrentHashMap<String, HashMap<BugInstance, List<String>>> predictions = new ConcurrentHashMap<>();
		List<List<String>> allLines = null;

		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(
				new InputStreamReader(new FileInputStream(outputPatternsPath), "Cp1252"), csvParser)) {
			allLines = csvReader.readAll();
		}

		ThreadParameters params = new ThreadParameters();
		params.addParam("predictions", predictions);
		params.addParam("patternTypes", patternTypes);
		params.addParam("inputPatterns", inputPatterns);

		ThreadExecutor.executePaginated(allLines, FalsePositivesProcessor.class, params);

		return predictions;
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

	public static class BugInstance {
		String id;
		String text;

		public BugInstance(String id, String text) {
			super();
			this.id = id;
			this.text = text;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((id == null) ? 0 : id.hashCode());
			result = prime * result + ((text == null) ? 0 : text.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			BugInstance other = (BugInstance) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			if (text == null) {
				if (other.text != null)
					return false;
			} else if (!text.equals(other.text))
				return false;
			return true;
		}

	}
}
