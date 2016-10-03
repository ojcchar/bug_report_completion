package seers.bugreppatterns.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
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

		String goldSetFolderPrefix = "";
		String outputFolderPrefix = "";
		boolean ourData = true;

		// command line arguments
		if (args.length > 0) {
			goldSetFolderPrefix = args[0] + File.separator;
			outputFolderPrefix = args[1] + File.separator;
			ourData = "y".equalsIgnoreCase(args[2]);
		}

		String[] granularities = { "B", "P", "S" };
		processGranularities(granularities, ourData, goldSetFolderPrefix, outputFolderPrefix);

		System.out.println("Done!");

	}

	private static void processGranularities(String[] granularities, boolean ourData, String goldSetFolderPrefix,
			String outputFolderPrefix) throws IOException {
		for (String granularity : granularities) {

			System.out.println("Processing " + granularity);

			// -------------------------------------------------------
			// output file and gold set

			File prefeaturesFile = new File(outputFolderPrefix + "output-pre-features-" + granularity + ".csv");
			File goldSetFile = new File(goldSetFolderPrefix + "gold-set-" + granularity + ".csv");
			if (granularity.equals("B")) {
				if (!ourData) {
					goldSetFile = new File(goldSetFolderPrefix + "all_data_only_bugs_davies.csv");
				}
			}

			// -------------------------------------------------------

			try (CsvWriter featuresEBWriter = new CsvWriterBuilder(
					new FileWriter("features-eb-" + granularity + ".txt")).separator(' ')
							.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
					CsvWriter featuresSRWriter = new CsvWriterBuilder(
							new FileWriter("features-sr-" + granularity + ".txt")).separator(' ')
									.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();) {

				// instances file
				File instancesFile = new File("instances-" + granularity + ".txt");
				instancesFile.delete();

				// read goldset and prefeatures
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

					// file writin

					List<String> nextLine = new ArrayList<>();
					nextLine.add(classEb);
					nextLine.addAll(featureList);
					featuresEBWriter.writeNext(nextLine);

					nextLine = new ArrayList<>();
					nextLine.add(classSr);
					nextLine.addAll(featureList);
					featuresSRWriter.writeNext(nextLine);

				}
			}

		}
	}

	private static List<List<String>> readPrefeaturesFile2(File prefeaturesFile) throws IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(prefeaturesFile), "Cp1252"),
				csvParser)) {
			return csvReader.readAll();
		}
	}

	private static List<List<String>> readGoldSetFile2(HashMap<String, Integer> goldSetMap, File goldSetFile)
			throws IOException {
		CsvParser csvParser = new CsvParserBuilder().separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(goldSetFile), "Cp1252"),
				csvParser)) {
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
