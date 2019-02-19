package seers.bugreppatterns.main.features;

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

		String[] granularities = { "SP-F" };

		for (String granularity : granularities) {

			try {
				processGranularity(ourData, goldSetFolderPrefix, outputFolderPrefix, granularity);

			} catch (Exception e) {
				System.out.println("Couldn't process granularity: " + granularity);
				e.printStackTrace();
			}

		}

		System.out.println("Done!");

	}

	private static void processGranularity(boolean ourData, String goldSetFolderPrefix, String outputFolderPrefix,
			String granularity) throws IOException {
		System.out.println("Processing " + granularity);

		// -------------------------------------------------------
		// output file and gold set

		File prefeaturesFile = new File(goldSetFolderPrefix + "output-pre-features-" + granularity + ".csv");
		File goldSetFile = new File(goldSetFolderPrefix + "gold-set-" + granularity + ".csv");
		if (granularity.equals("B")) {
			if (!ourData) {
				goldSetFile = new File(goldSetFolderPrefix + "all_data_only_bugs_davies.csv");
			}
		}

		// -------------------------------------------------------

		// instances file
		File instancesFile = new File(outputFolderPrefix + "instances-" + granularity + ".txt");
		instancesFile.delete();

		// read goldset and prefeatures
		HashMap<String, Integer> goldSetMap = new LinkedHashMap<>();
		List<List<String>> linesGoldSet = readGoldSetFile2(goldSetMap, goldSetFile);
		List<List<String>> featuresLines = readPrefeaturesFile2(prefeaturesFile);

		try (CsvWriter featuresEBWriter = new CsvWriterBuilder(
				new FileWriter(outputFolderPrefix + "features-eb-" + granularity + ".txt")).separator(' ')
						.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
				CsvWriter featuresSRWriter = new CsvWriterBuilder(
						new FileWriter(outputFolderPrefix + "features-sr-" + granularity + ".txt")).separator(' ')
								.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
			 CsvWriter featuresOBWriter = new CsvWriterBuilder(
					 new FileWriter(outputFolderPrefix + "features-ob-" + granularity + ".txt")).separator(' ')
					 .quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();) {

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
				String ob = "";
				Integer i = goldSetMap.get(key);
				if (i != null) {
					List<String> goldSet = linesGoldSet.get(i);

					ob = goldSet.get(3);
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
				String classOb = "2";
				if (!ob.trim().isEmpty()) {
					classOb = "1";
				}

				// file writing

				List<String> nextLine = new ArrayList<>();
				nextLine.add(classEb);
				nextLine.addAll(featureList);
				featuresEBWriter.writeNext(nextLine);

				nextLine = new ArrayList<>();
				nextLine.add(classSr);
				nextLine.addAll(featureList);
				featuresSRWriter.writeNext(nextLine);

				nextLine = new ArrayList<>();
				nextLine.add(classOb);
				nextLine.addAll(featureList);
				featuresOBWriter.writeNext(nextLine);

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
