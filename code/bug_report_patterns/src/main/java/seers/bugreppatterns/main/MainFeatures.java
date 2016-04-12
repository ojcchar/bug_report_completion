package seers.bugreppatterns.main;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;

public class MainFeatures {

	private static LinkedHashMap<String, Integer> featuresMap = new LinkedHashMap<>();
	private static LinkedHashMap<String, Integer> featuresMap2 = new LinkedHashMap<>();

	public static void main(String[] args) throws Exception {

		String[] granularities = { "P", "S" };

		for (String gran : granularities) {
			File prefeaturesFile = new File("test_data/output/output-pre-features-" + gran + ".csv");
			File goldSetFile = new File("gold-set-" + gran + ".csv");

			CsvWriter featuresEBWriter = new CsvWriterBuilder(new FileWriter("features-eb-" + gran + ".txt"))
					.separator(' ').quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
			CsvWriter featuresSRWriter = new CsvWriterBuilder(new FileWriter("features-sr-" + gran + ".txt"))
					.separator(' ').quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
			try {
				File instancesFile = new File("instances-" + gran + ".txt");
				instancesFile.delete();

				List<List<String>> linesGoldSet = readGoldSetFile(goldSetFile);
				List<List<String>> featuresLines = readPrefeaturesFile(featuresMap, prefeaturesFile);

				for (List<String> lineGoldSet : linesGoldSet.subList(1, linesGoldSet.size())) {
					String system = lineGoldSet.get(0);
					String bugId = lineGoldSet.get(1);
					String instanceId = lineGoldSet.get(2);
					String eb = lineGoldSet.get(4);
					String sr = lineGoldSet.get(5);

					// instance
					String key = getKey(system, bugId, instanceId);
					FileUtils.write(instancesFile, key + "\r\n", true);

					// features
					Integer i = featuresMap.get(key);
					if (i == null) {
						System.out.println(key + " is not in the features");
						continue;
					}

					List<String> feat = featuresLines.get(i);

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
					nextLine.addAll(feat.subList(3, feat.size()));
					featuresEBWriter.writeNext(nextLine);

					nextLine = new ArrayList<>();
					nextLine.add(classSr);
					nextLine.addAll(feat.subList(3, feat.size()));
					featuresSRWriter.writeNext(nextLine);
				}
			} finally {
				featuresEBWriter.close();
				featuresSRWriter.close();
			}

		}

		// -----------------------
		File prefeaturesFile = new File("test_data/output/output-pre-features-B.csv");
		File goldSetFile = new File("all_data_only_bugs.csv");

		CsvWriter featuresEBWriter = new CsvWriterBuilder(new FileWriter("features-eb-B.txt")).separator(' ')
				.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
		CsvWriter featuresSRWriter = new CsvWriterBuilder(new FileWriter("features-sr-B.txt")).separator(' ')
				.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
		try {
			File instancesFile = new File("instances-B.txt");
			instancesFile.delete();
			List<List<String>> linesGoldSet = readGoldSetFile(goldSetFile);
			List<List<String>> featuresLines = readPrefeaturesFile(featuresMap2, prefeaturesFile);

			for (List<String> lineGoldSet : linesGoldSet.subList(1, linesGoldSet.size())) {
				String system = lineGoldSet.get(0);
				String bugId = lineGoldSet.get(1);
				String eb = lineGoldSet.get(3);
				String sr = lineGoldSet.get(4);

				// instance
				String key = getKey(system, bugId, "0");
				FileUtils.write(instancesFile, key + "\r\n", true);

				// features
				Integer i = featuresMap2.get(key);
				if (i == null) {
					System.out.println(key + " is not in the features");
					continue;
				}

				List<String> feat = featuresLines.get(i);

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
				nextLine.addAll(feat.subList(3, feat.size()));
				featuresEBWriter.writeNext(nextLine);

				nextLine = new ArrayList<>();
				nextLine.add(classSr);
				nextLine.addAll(feat.subList(3, feat.size()));
				featuresSRWriter.writeNext(nextLine);
			}
		} finally {
			featuresEBWriter.close();
			featuresSRWriter.close();
		}

	}

	private static List<List<String>> readPrefeaturesFile(LinkedHashMap<String, Integer> featuresMap,
			File prefeaturesFile) throws IOException {

		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(new FileReader(prefeaturesFile), csvParser)) {
			List<List<String>> lines = csvReader.readAll();
			for (int i = 0; i < lines.size(); i++) {
				List<String> line = lines.get(i);

				String system = line.get(0);
				String bugId = line.get(1);
				String instanceId = line.get(2);

				String key = getKey(system, bugId, instanceId);
				featuresMap.put(key, i);
			}
			return lines;
		}

	}

	private static String getKey(String system, String bugId, String instanceId) {
		return system + "-" + bugId + "-" + instanceId;
	}

	private static List<List<String>> readGoldSetFile(File goldSetFile) throws IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(new FileReader(goldSetFile), csvParser)) {
			return csvReader.readAll();
		}

	}

}
