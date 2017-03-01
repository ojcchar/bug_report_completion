package seers.bugrepcompl.xmlcoding;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.utils.DataReader;

public class PatternFrequencyMain2 {

	static String[] typesOfInfo = { "OB", "EB", "SR" };
	static String[] systems = { "eclipse", "facebook", "firefox", "httpd", "docker", "hibernate", "libreoffice",
			"openmrs", "wordpress-android" };
	private static String bugPatternFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/bugs_patterns.csv";
	private static String bugGoldSetFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/gold-set-B-coded_by_us.csv";
	private static String patternsFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/pattern_list.csv";

	public static void main(String[] args) throws Exception {

		// --------------------------

		Set<String> patternList = DataReader.readPatternList(patternsFile);

		HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(bugGoldSetFile);

		// --------------------------

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(bugPatternFile), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();
			List<List<String>> subList = allLines.subList(1, allLines.size());

			for (String system : systems) {
				
				Set<Entry<TextInstance, Labels>> goldSetProject = goldSet.entrySet().stream()
						.filter(e -> e.getKey().getProject().equals(system)).collect(Collectors.toSet());
				
				List<List<String>> systemLines = subList.stream().filter(l -> l.get(0).equals(system)).collect(Collectors.toList());

				HashMap<String, HashMap<String, Integer>> typesInfoIndivDistro = new HashMap<>();

				for (String typeOfInfo : typesOfInfo) {

					HashMap<String, Integer> patternFreq = new HashMap<>();
					typesInfoIndivDistro.put(typeOfInfo, patternFreq);

					// -----------------------------

					List<String> patternsForTypeInfo = patternList.stream().filter(p -> p.contains("_" + typeOfInfo + "_"))
							.collect(Collectors.toList());
					Collections.sort(patternsForTypeInfo, (p1, p2) -> p1.compareTo(p2));

					// -----------------------------

					int numBugsTypeOfInfo = getNumBugsTypeOfInfo(goldSetProject, typeOfInfo);

					for (String pattern : patternsForTypeInfo) {
						int numBugs = getNumberOfBugs(typeOfInfo, pattern, systemLines);
						patternFreq.put(pattern, numBugs);
					}

					// -----------------------------

					List<Entry<String, Integer>> listPattFreq = new ArrayList<>(patternFreq.entrySet());
					Collections.sort(listPattFreq, (e1, e2) -> e2.getValue().compareTo(e1.getValue()));

					// -----------------------------

					List<String> acummulatePatt = new ArrayList<>();
					int rank = 1;
					for (Entry<String, Integer> patt : listPattFreq) {
						acummulatePatt.add(patt.getKey());
						int numBugs = getNumberOfBugs(typeOfInfo, acummulatePatt, systemLines);

						System.out.println(typeOfInfo + ";" + system + ";" + numBugsTypeOfInfo + ";" +patt.getKey() + ";" + patt.getValue() + ";"
								+ ((float) patt.getValue() / numBugsTypeOfInfo) + ";" + numBugs + ";"
								+ ((float) numBugs / numBugsTypeOfInfo)+ ";" + rank++);
					}

				}
			}
		}

	}

	private static int getNumberOfBugs(String typeOfInfo, List<String> patterns, List<List<String>> lines) {

		HashSet<TextInstance> patternBugs = new HashSet<>();

		lines.forEach(line -> {

			String project = line.get(0);
			String bugId = line.get(1);
			String pattern = line.get(5).trim();

			TextInstance instance = new TextInstance(project, bugId, "0");

			if (patterns.contains(pattern)) {
				patternBugs.add(instance);
			}
		});

		return patternBugs.size();
	}

	private static int getNumberOfBugs(String typeOfInfo, String pattern, List<List<String>> lines) {
		return getNumberOfBugs(typeOfInfo, Arrays.asList(pattern), lines);
	}

	private static int getNumBugsTypeOfInfo(Set<Entry<TextInstance, Labels>> goldSetProject, String typeOfInfo) {
		long num = -1;
		switch (typeOfInfo) {
		case "OB":
			num = goldSetProject.stream().filter(e -> !e.getValue().getIsOB().trim().isEmpty()).count();
			break;
		case "EB":
			num = goldSetProject.stream().filter(e -> !e.getValue().getIsEB().trim().isEmpty()).count();
			break;
		case "SR":
			num = goldSetProject.stream().filter(e -> !e.getValue().getIsSR().trim().isEmpty()).count();
			break;
		default:
			break;
		}
		return (int) num;
	}

}
