package seers.bugreppatterns.main.coocurrence;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;

import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.utils.DataReader;

public class MainPatternsByTypeOfPattern {

	static LinkedHashMap<TextInstance, LinkedHashSet<String>> sentenceOrderedPatterns = new LinkedHashMap<>();

	public static void main(String[] args) throws Exception {
		File inputFile = new File(
				"test_data" + File.separator + "matcher" + File.separator + "type_of_info_bugs.csv");
		List<List<String>> lines = DataReader.readLines(inputFile);

		for (List<String> line : lines.subList(1, lines.size())) {

			String sys = line.get(0);
			String bugId = line.get(1);
			String infoType = line.get(2);

			TextInstance instance = new TextInstance(sys, bugId, "0");
			LinkedHashSet<String> infoTypes = sentenceOrderedPatterns.get(instance);
			if (infoTypes == null) {
				infoTypes = new LinkedHashSet<>();
				sentenceOrderedPatterns.put(instance, infoTypes);
			}

			infoTypes.add(infoType);

		}

		// ---------------------

		String outputFilePath = "test_data" + File.separator + "info-types-ordered-B.csv";
		try (CsvWriter writer = new CsvWriterBuilder(new FileWriter(outputFilePath)).separator(';').build()) {

			writer.writeNext(Arrays.asList(new String[] { "project", "bug_id", "info_types" }));

			for (Entry<TextInstance, LinkedHashSet<String>> entry : sentenceOrderedPatterns.entrySet()) {

				List<String> line = new ArrayList<>();
				TextInstance instance = entry.getKey();

				line.add(instance.getProject());
				line.add(instance.getBugId());

				line.add(entry.getValue().toString());

				writer.writeNext(line);
			}
		}
	}

}
