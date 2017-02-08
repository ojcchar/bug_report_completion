package seers.bugrepcompl.xmlcoding;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.utils.DataReader;

public class PatternFrequencyMain {

//	static List<String> patterns = Arrays.asList("P_SR_LABELED_LIST");
	
//	 static List<String> patterns = Arrays.asList("S_OB_NEG_AUX_VERB"
//	 , "S_OB_BUT_NEG", "S_OB_COND_NEG",
//	 "S_OB_VERB_ERROR", "S_OB_NEG_VERB", "S_OB_NEG_ADV_ADJ"
//	 );
	 
//	static List<String> patterns = Arrays.asList( "S_OB_COND_POS", "S_OB_OBS_BEHAVIOR", "S_OB_BUT", "P_OB_OBSERVED_BEHAVIOR_MULTI", "S_OB_WORKS_FINE", "S_OB_INSTEAD_OF");
	 
//	static List<String> patterns = Arrays.asList( "S_EB_SHOULD");
	
	static List<String> patterns = Arrays.asList( "S_EB_SHOULD", "S_EB_EXP_BEHAVIOR", "P_EB_EXP_BEHAVIOR_MULTI", "S_EB_INSTEAD_OF_EXP_BEHAVIOR", "S_EB_EXPECTED", "S_EB_WOULD_BE");
	
	static String typeOfInfo;
	static {
		typeOfInfo = patterns.get(0).substring(1, 5);
	}
	private static String bugPatternFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/bugs_patterns.csv";
	private static String bugGoldSetFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/gold-set-B-coded_by_us.csv";

	public static void main(String[] args) throws Exception {

		HashSet<TextInstance> allBugs = new HashSet<>();
		HashSet<TextInstance> patternBugs = new HashSet<>();
		HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(bugGoldSetFile);
		HashSet<String> noPatterns = new HashSet<>();

		int numBugs = goldSet.size();
		int numBugsTypeOfInfo = getNumBugsTypeOfInfo(goldSet);

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(bugPatternFile), "Cp1252"),
				csvParser)) {

			List<List<String>> allLines = csvReader.readAll();

			allLines.subList(1, allLines.size()).forEach(line -> {

				String project = line.get(0);
				String bugId = line.get(1);
				String pattern = line.get(5).trim();

				TextInstance instance = new TextInstance(project, bugId, "0");
				allBugs.add(instance);

				if (patterns.contains(pattern)) {

					patternBugs.add(instance);
				} else {
					noPatterns.add(pattern);
				}
			});

		}

		System.out.println("total # of bugs: " + numBugs);
		System.out.println("# of bugs: " + patternBugs.size());
		System.out.println("% of bugs: " + ((float) patternBugs.size() / numBugs));
		System.out.println("----------------------------------");
		System.out.println("# of bugs " + typeOfInfo + ": " + numBugsTypeOfInfo);
		System.out.println("% of bugs: " + ((float) patternBugs.size() / numBugsTypeOfInfo));

		System.out.println("----------------------------------");
		System.out.println(noPatterns);

	}

	private static int getNumBugsTypeOfInfo(HashMap<TextInstance, Labels> goldSet) {
		long num=-1;
		switch (typeOfInfo) {
		case "_OB_":
			num = goldSet.entrySet().stream().filter(e -> !e.getValue().getIsOB().trim().isEmpty()).count();
			break;
		case "_EB_":
			num = goldSet.entrySet().stream().filter(e -> !e.getValue().getIsEB().trim().isEmpty()).count();
			break;
		case "_SR_":
			num = goldSet.entrySet().stream().filter(e -> !e.getValue().getIsSR().trim().isEmpty()).count();
			break;
		default:
			break;
		}
		return (int) num;
	}

}
