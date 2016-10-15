package seers.bugreppatterns.main;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.xml.XMLHelper;
import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.entity.xml.BugReport;
import seers.bugreppatterns.utils.ParsingUtils;
import seers.textanalyzer.entity.Sentence;

public class FalsePositiveSummarizer {

	public static void main(String[] args) throws Exception {

		String sampleFilePath = "test_data\\output\\eval\\data_all_sr_sample_B.csv";
		String outputPatternsPath = "test_data\\output\\output-patterns-S.csv";
		String[] patternTypes = { "SR" };
		String outputFile = "false-positives.csv";
		if (args.length != 0) {
			sampleFilePath = args[0];
			outputPatternsPath = args[1];
			patternTypes = args[2].split(",");
		}

		List<List<String>> falsePositivesEntries = readFalsePositives(sampleFilePath);
		HashMap<String, HashMap<BugInstance, List<String>>> predictions = readPatternsPredictions(outputPatternsPath,
				patternTypes);

		HashMap<String, Set<String>> predictionsByBug = new HashMap<>();

		CsvWriter cw = new CsvWriterBuilder(new FileWriter(outputFile)).separator(';').build();

		for (List<String> fpEntry : falsePositivesEntries) {

			String system = fpEntry.get(0);
			String bugId = fpEntry.get(1);

			String sysBugIdKey = getSysBugIdKey(system, bugId);
			HashMap<BugInstance, List<String>> sentences = predictions.get(sysBugIdKey);

			for (Entry<BugInstance, List<String>> sentence : sentences.entrySet()) {
				for (String pattern : sentence.getValue()) {
					BugInstance instance = sentence.getKey();

					cw.writeNext(Arrays.asList(new String[] { system, bugId, instance.id, pattern, instance.text }));

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

	private static HashMap<String, HashMap<BugInstance, List<String>>> readPatternsPredictions(
			String outputPatternsPath, String[] patternTypes) throws Exception {

		HashMap<String, HashMap<BugInstance, List<String>>> predictions = new HashMap<>();
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

			HashMap<BugInstance, List<String>> sentences = predictions.get(sysBugIdKey);
			if (sentences == null) {
				sentences = new HashMap<>();
				predictions.put(sysBugIdKey, sentences);
			}

			// -----------------------------------------

			String instanceId = line.get(2);
			BugInstance instance = new FalsePositiveSummarizer.BugInstance(instanceId, "");

			List<String> patterns = sentences.get(instanceId);
			if (patterns == null) {
				patterns = new ArrayList<>();
				sentences.put(instance, patterns);

				setBugInstanceText(system, bugId, instanceId, instance);
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

	private static HashMap<String, Document> bugReportsHash = new HashMap<>();

	private static void setBugInstanceText(String system, String bugId, String instanceId, BugInstance instance)
			throws Exception {

		String sysBugIdKey = getSysBugIdKey(system, bugId);
		Document bugRepDoc = bugReportsHash.get(sysBugIdKey);

		if (bugRepDoc == null) {
			String file = "test_data\\data\\" + system + "_parse\\" + bugId + ".xml.parse";
			BugReport bugRep = XMLHelper.readXML(BugReport.class, file);
			bugRepDoc = ParsingUtils.parseDocument(system, bugRep);
			bugReportsHash.put(sysBugIdKey, bugRepDoc);
		}

		if (instanceId.contains(".")) {
			Optional<Sentence> first = bugRepDoc.getSentences().stream().filter(sent -> sent.getId().equals(instanceId))
					.findFirst();
			instance.text = first.get().getText().replace("\n", "\\n");
		} else {
			final StringBuffer buffer = new StringBuffer();
			List<Sentence> sentences = bugRepDoc.getParagraphs().stream().filter(p -> p.getId().equals(instanceId))
					.findFirst().get().getSentences();
			sentences.forEach(s -> {
				buffer.append(s.getText());
				buffer.append(". ");
			});
			instance.text = buffer.toString().replace("\n", "\\n");
		}

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
