package seers.bugreppatterns.main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;

public class MainFeatures {

	public static void main(String[] args) throws Exception {

		String[] granularities = { "B", "P", "S" };

		processGranularities(granularities);

		System.out.println("Done!");

	}

	private static void processGranularities(String[] granularities) throws IOException {
		for (String gran : granularities) {
			System.out.println("doing " + gran);

			File prefeaturesFile = new File("test_data/output/output-pre-features-" + gran + ".csv");
			File goldSetFile = new File("gold-set-" + gran + ".csv");
			if (gran.equals("B")) {
				goldSetFile = new File("all_data_only_bugs.csv");
			}

			CsvWriter featuresEBWriter = new CsvWriterBuilder(new FileWriter("features-eb-" + gran + ".txt"))
					.separator(' ').quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
			CsvWriter featuresSRWriter = new CsvWriterBuilder(new FileWriter("features-sr-" + gran + ".txt"))
					.separator(' ').quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
			try {
				File instancesFile = new File("instances-" + gran + ".txt");
				instancesFile.delete();

				HashMap<String, Integer> goldSetMap = new LinkedHashMap<>();
				List<List<String>> linesGoldSet = readGoldSetFile2(goldSetMap, goldSetFile);
				List<List<String>> featuresLines = readPrefeaturesFile2(prefeaturesFile);

				// --------------------------------------------

				for (List<String> featureLine : featuresLines) {
					String system = featureLine.get(0);
					String bugId = featureLine.get(1);
					String instanceId = featureLine.get(2);
					List<String> featureList = featureLine.subList(3, featureLine.size());

					// instance
					String key = getKey(system, bugId, instanceId);
					FileUtils.write(instancesFile, key + "\r\n", true);

					// classes
					String eb = "";
					String sr = "";
					Integer i = goldSetMap.get(key);
					if (i != null) {
						List<String> goldSet = linesGoldSet.get(i);

						eb = goldSet.get(4);
						sr = goldSet.get(5);
					}

					// classes
					String classEb = "2";
					if (!eb.trim().isEmpty()) {
						classEb = "1";
					}
					String classSr = "2";
					if (!sr.trim().isEmpty()) {
						classSr = "1";
					}

					List<String> nextLine = new ArrayList<>();
					nextLine.add(classEb);
					nextLine.addAll(featureList);
					featuresEBWriter.writeNext(nextLine);

					nextLine = new ArrayList<>();
					nextLine.add(classSr);
					nextLine.addAll(featureList);
					featuresSRWriter.writeNext(nextLine);

				}
			} finally {
				featuresEBWriter.close();
				featuresSRWriter.close();
			}

		}
	}

	private static List<List<String>> readPrefeaturesFile2(File prefeaturesFile) throws IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(new FileReader(prefeaturesFile), csvParser)) {
			return csvReader.readAll();
		}
	}

	private static List<List<String>> readGoldSetFile2(HashMap<String, Integer> goldSetMap, File goldSetFile)
			throws IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(new FileReader(goldSetFile), csvParser)) {
			List<List<String>> lines = csvReader.readAll();
			for (int i = 1; i < lines.size(); i++) {
				List<String> line = lines.get(i);

				String system = line.get(0);
				String bugId = line.get(1);
				String instanceId = line.get(2);

				String key = getKey(system, bugId, instanceId);
				goldSetMap.put(key, i);
			}
			return lines;
		}

	}

	private static String getKey(String system, String bugId, String instanceId) {
		return system + "-" + bugId + "-" + instanceId;
	}

}
