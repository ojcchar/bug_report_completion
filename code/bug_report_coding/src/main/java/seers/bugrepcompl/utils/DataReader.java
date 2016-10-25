package seers.bugrepcompl.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.bugrepcompl.entity.CodedDataEntry;

public class DataReader {

	public static List<CodedDataEntry> readCodedData(String fileCoder1) throws IOException {

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(fileCoder1), "Cp1252"),
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
						isObsBehavior, isExpecBehavior, isStepsToRepro, pattern1, pattern2, pattern3, pattern4,mainCoder, instanceId, patternsNoTesting);
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

}
