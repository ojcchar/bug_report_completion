package seers.bugreppatterns.main.statistics;

import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.utils.DataReader;

public class PatternDistributionMain {

	static String patternsDistributionFile = "C:/Users/ojcch/Documents/Google Drive/Research/Bug reports autocompletion/analysis/pattern distribution/all_data/output-patterns-B.csv";
	static String ebListOfTypeOfBugFromEvaluationFile = "C:/Users/ojcch/Documents/Google Drive/Research/Bug reports autocompletion/analysis/false_pos_neg_analysis/Best_SVM_cross_projects/list_EB_bugs_predicted_SVM_patterns_cross.csv";
	static String srListOfTypeOfBugFromEvaluationFile = "C:/Users/ojcch/Documents/Google Drive/Research/Bug reports autocompletion/analysis/false_pos_neg_analysis/Best_SVM_cross_projects/list_SR_bugs_predicted_SVM_patterns_ngrams_cross.csv";

	// # data set not coded by us
	static String bugGoldSetFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/gold-set-B-not_coded_by_us.csv";
	static String outputFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/bugs_patterns_distro_not_coded_by_us_cross.csv";
	static String outputFile2 = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/bugs_patterns_distro_not_coded_by_us_pairs_cross.csv";

	// # data set coded by us
	// static String bugGoldSetFile =
	// "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/gold-set-B-coded_by_us.csv";
	// static String outputFile =
	// "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/bugs_patterns_distro_coded_by_us.csv";
	// static String outputFile2 =
	// "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/bugs_patterns_distro_coded_by_us_pairs.csv";

	public static void main(String[] args) throws Exception {

		HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(bugGoldSetFile);
		HashMap<TextInstance, Set<String>> distro = readPatternsDistro(patternsDistributionFile);
		HashMap<TextInstance, String> bugTypesEb = readBugTypesFromEvaluation(ebListOfTypeOfBugFromEvaluationFile);
		HashMap<TextInstance, String> bugTypesSr = readBugTypesFromEvaluation(srListOfTypeOfBugFromEvaluationFile);

		// -----------------------------------------

		try (CsvWriter wr = new CsvWriterBuilder(new FileWriter(outputFile)).separator(';').build();
				CsvWriter wr2 = new CsvWriterBuilder(new FileWriter(outputFile2)).separator(';').build();) {

			List<String> headers = Arrays.asList("system", "bug_id", "sys_bug", "instance_id", "sys_bug_instance",
					"pattern", "info_type", "pattern_type", "is_ob", "is_eb", "is_sr", "bug_type_eb", "bug_type_sr");
			wr.writeNext(headers);

			headers = Arrays.asList("system", "bug_id", "sys_bug", "instance_id", "sys_bug_instance", "pattern1",
					"info_type1", "pattern_type1", "pattern2", "info_type2", "pattern_type2", "is_ob", "is_eb", "is_sr",
					"bug_type_eb", "bug_type_sr");
			wr2.writeNext(headers);

			Set<Entry<TextInstance, Labels>> bugInstances = goldSet.entrySet();
			for (Entry<TextInstance, Labels> bugEntry : bugInstances) {
				TextInstance bugInstance = bugEntry.getKey();
				Labels labels = bugEntry.getValue();
				try {

					// System.out.println("Processing " + bugInstance + " ");

					String project = bugInstance.getProject();
					String bugId = bugInstance.getBugId();

					// -----------------------------------------

					TextInstance instance = new TextInstance(project, bugId, "0");
					Set<String> patterns = distro.get(instance);
					List<String> patternList = new ArrayList<>(patterns);

					String bugTypeEb = bugTypesEb.get(instance);
					String bugTypeSr = bugTypesSr.get(instance);

					for (int i = 0; i < patternList.size(); i++) {
						String pattern = patternList.get(i);

						String patternType = pattern.substring(0, 1);
						String infoType = pattern.substring(2, 4);

						wr.writeNext(Arrays.asList(project, bugId, project + ";" + bugId, "0",
								project + ";" + bugId + ";0", pattern, infoType, patternType, labels.getIsOB(),
								labels.getIsEB(), labels.getIsSR(), bugTypeEb, bugTypeSr));

						for (int j = i + 1; j < patternList.size(); j++) {
							String pattern2 = patternList.get(j);

							String patternType2 = pattern.substring(0, 1);
							String infoType2 = pattern.substring(2, 4);
							
							if (pattern.compareTo(pattern2) > 0 ) {

								wr2.writeNext(Arrays.asList(project, bugId, project + ";" + bugId, "0",
										project + ";" + bugId + ";0", pattern, infoType, patternType, pattern2, infoType2,
										patternType2, labels.getIsOB(), labels.getIsEB(), labels.getIsSR(), bugTypeEb,
										bugTypeSr));
							}else{

								wr2.writeNext(Arrays.asList(project, bugId, project + ";" + bugId, "0",
										project + ";" + bugId + ";0", pattern2, infoType2, patternType2, pattern, infoType,
										patternType, labels.getIsOB(), labels.getIsEB(), labels.getIsSR(), bugTypeEb,
										bugTypeSr));
							}

						}
					}

				} catch (Exception e) {
					System.err.println("Error for " + bugInstance);
					e.printStackTrace();
				}
			}
		}

		System.out.println("Done");
	}

	private static HashMap<TextInstance, String> readBugTypesFromEvaluation(String bugTypesFile) throws IOException {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(bugTypesFile), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			HashMap<TextInstance, String> entries = new HashMap<>();

			allLines.subList(1, allLines.size()).forEach(line -> {

				String project = line.get(0);
				String bugId = line.get(1);
				String instanceId = "0";
				String type = line.get(3);

				TextInstance instance = new TextInstance(project, bugId, instanceId);
				entries.put(instance, type);
			});

			return entries;
		}
	}

	private static HashMap<TextInstance, Set<String>> readPatternsDistro(String inputFile) throws IOException {
		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(inputFile), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			HashMap<TextInstance, Set<String>> entries = new HashMap<>();

			allLines.forEach(line -> {

				String project = line.get(0);
				String bugId = line.get(1);
				String instanceId = line.get(2);

				TextInstance instance = new TextInstance(project, bugId, instanceId);

				Set<String> patterns = new LinkedHashSet<>();

				for (int i = 3; i < line.size();) {
					String pattern = line.get(i);
					patterns.add(pattern);
					i = i + 2;
				}

				entries.put(instance, patterns);
			});

			return entries;
		}
	}

}
