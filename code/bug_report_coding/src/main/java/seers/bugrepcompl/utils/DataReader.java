package seers.bugrepcompl.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.bugrepcompl.entity.CodedDataEntry;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;

public class DataReader {

	public static List<List<String>> readLines(File inputFile) throws IOException {
		char separator = ';';
		return readLines(inputFile, separator);
	}

	public static List<List<String>> readLines(File inputFile, char separator) throws IOException {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(separator).build();
		try (CsvReader csvReader = new CsvReader(new FileReader(inputFile), csvParser)) {
			return csvReader.readAll();
		}
	}

	public static List<CodedDataEntry> readCodedData(String dataFilePath) throws IOException {

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(dataFilePath), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			List<CodedDataEntry> entries = new ArrayList<>();

			allLines.subList(1, allLines.size()).forEach(line -> {

				if (line.isEmpty() || isLineEmpty(line)) {
					return;
				}

				String project = line.get(0).trim();
				String bugId = line.get(1).trim();
				String sentenceId = line.get(2).trim();
				String paragraph = line.get(3).trim();
				String sentence = line.get(4).trim();
				boolean isObsBehavior = line.get(5).trim().equalsIgnoreCase("x");
				boolean isExpecBehavior = line.get(6).trim().equalsIgnoreCase("x");
				boolean isStepsToRepro = line.get(7).trim().equalsIgnoreCase("x");
				String pattern1 = line.get(8).trim();
				String pattern2 = line.get(9).trim();
				String pattern3 = line.get(10).trim();
				String pattern4 = line.get(11).trim();

				String mainCoder = line.get(13).trim();
				String instanceId = line.get(14).trim();
				String[] patternsNoTesting = line.get(15).split(",");

				CodedDataEntry entry = new CodedDataEntry(project, bugId, sentenceId, paragraph, sentence,
						isObsBehavior, isExpecBehavior, isStepsToRepro, pattern1, pattern2, pattern3, pattern4,
						mainCoder, instanceId, patternsNoTesting);
				entries.add(entry);

			});

			return entries;
		}
	}

	private static boolean isLineEmpty(List<String> line) {

		if (line.get(0).isEmpty() || line.get(1).isEmpty() || line.get(2).isEmpty()) {
			return true;
		}

		for (String field : line) {
			if (!field.trim().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static HashMap<TextInstance, String> readTypeOfIssues(String dataFilePath) throws IOException {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(dataFilePath), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			HashMap<TextInstance, String> entries = new HashMap<>();

			allLines.subList(1, allLines.size()).forEach(line -> {

				String project = line.get(0);
				String bugId = line.get(1);
				String instanceId = "0";
				String bugType = line.get(4).trim();

				if (bugType.isEmpty()) {
					return;
				}

				TextInstance instance = new TextInstance(project, bugId, instanceId);
				entries.put(instance, bugType);
			});

			return entries;
		}
	}

	public static HashMap<TextInstance, Labels> readGoldSet(String goldSetPath) throws IOException {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(goldSetPath), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			HashMap<TextInstance, Labels> entries = new HashMap<>();

			allLines.subList(1, allLines.size()).forEach(line -> {

				String project = line.get(0);
				String bugId = line.get(1);
				String instanceId = line.get(2);
				String isOB = line.get(3).trim();
				String isEB = line.get(4).trim();
				String isSR = line.get(5).trim();
				String codedBy = line.get(6).trim();

				Labels labels = new Labels(isOB, isEB, isSR);
				labels.setCodedBy(codedBy);
				TextInstance instance = new TextInstance(project, bugId, instanceId);

				entries.put(instance, labels);
			});

			return entries;
		}
	}

	public static Set<String> readPatternList(String patternsFile) throws IOException {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(patternsFile), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			Set<String> entries = new LinkedHashSet<>();

			allLines.subList(1, allLines.size()).forEach(line -> {

				String pattern = line.get(3).trim();
				
				if (!pattern.isEmpty()) {
					entries.add(pattern);
				}

			});

			return entries;
		}
	}

}
