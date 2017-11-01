package seers.bugrepcompl.xmlcoding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;

public class FinalCodingMain1 {

	// //new data
	// static String conflictsFile =
	// "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/agreement/conflicts.csv";
	// static String completeSamplePath =
	// "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/sample/andi_students/complete_sample_seers50.csv";
	// static String conflictSolutionsFolder =
	// "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/conflicts_solved";
	// static String codedDataFolder =
	// "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data";
	// static String outputFolder =
	// "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data_after_conflicts";
	// private static List<String> allowedCoders = new ArrayList<>(
	// new HashSet<String>(Arrays.asList("alex", "juan", "laura", "fiorella",
	// "jing", "oscar", "alejo")));

	// old data
	static String conflictsFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/agreement_old_data/conflicts/conflicts.csv";
	static String completeSamplePath = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/sample/andi_students/old_data_coding.csv";
	static String conflictSolutionsFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/conflicts_solved_old";
	static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_old_data - Copy";
	static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data_after_conflicts_old";
	private static List<String> allowedCoders = new ArrayList<>(new HashSet<String>(
			Arrays.asList("juan", "laura", "fiorella", "jing", "oscar", "alejo", "ana", "daniel", "davies", "lau")));

	public static void main(String[] args) throws Exception {

		// read conflicts
		List<List<String>> conflicts = ConflictsPackagerMain.readConflicts(conflictsFile);
		HashMap<TextInstance, String> conflictReviewers = getConflictReviewers(conflicts);

		// read complete sample
		List<SampleEntry> sample = AgreementMain.readCompleteSample(completeSamplePath, allowedCoders);

		int numOfEntries = 0;
		int numOfConflicts = 0;

		// for each entry in complete sample
		for (SampleEntry sampleEntry : sample) {

			TextInstance bugInstance = sampleEntry.getInstance();
			String project = bugInstance.getProject();
			String bugId = bugInstance.getBugId();
			String coder1 = sampleEntry.getCoder1();
			String coder2 = sampleEntry.getCoder2();

			String reviewer = conflictReviewers.get(bugInstance);

			try {
				File srcFile = null;
				// if not in conflicts
				if (reviewer == null) {
					// copy the file from coded data
					srcFile = new File(codedDataFolder + File.separator + coder1 + File.separator + "bugs_parsed"
							+ File.separator + project + File.separator + bugId + ".parse.xml");
					if (!srcFile.exists()) {
						srcFile = new File(codedDataFolder + File.separator + coder2 + File.separator + "bugs_parsed"
								+ File.separator + project + File.separator + bugId + ".parse.xml");
					}

				} else {
					// if in conflicts

					// copy the file from conflicts solutions
					String filePrefix = conflictSolutionsFolder + File.separator + reviewer + File.separator + project
							+ File.separator;
					srcFile = new File(filePrefix + bugId + ".parse.xml");
					File srcFile1 = new File(filePrefix + bugId + "_1.parse.xml");
					File srcFile2 = new File(filePrefix + bugId + "_2.parse.xml");

					if ((srcFile.exists() && srcFile1.exists()) || (srcFile2.exists() && srcFile1.exists())
							|| (srcFile.exists() && srcFile2.exists())) {
						throw new RuntimeException("Multiple files exist for reviewer: " + reviewer);
					}

					if (!srcFile.exists()) {
						srcFile = srcFile1;
						if (!srcFile.exists()) {
							srcFile = srcFile2;
						}
					}

					numOfConflicts++;
				}

				// check if the xml is valid
				XMLHelper.readXML(ShortLabeledBugReport.class, srcFile);

				File destFile = new File(
						outputFolder + File.separator + project + File.separator + bugId + ".parse.xml");
				FileUtils.copyFile(srcFile, destFile);

				numOfEntries++;
			} catch (Exception e) {
				System.err.println("Error for entry " + sampleEntry + ", reviewer: " + reviewer);
				e.printStackTrace();
			}

		}

		System.out.println("# of entries processed: " + numOfEntries);
		System.out.println("# of conflicts processed: " + numOfConflicts + "/" + numOfEntries);
	}

	private static HashMap<TextInstance, String> getConflictReviewers(List<List<String>> conflicts) {

		HashMap<TextInstance, String> reviewers = new LinkedHashMap<>();

		for (List<String> conflict : conflicts) {

			String project = conflict.get(0);
			String bugId = conflict.get(1);
			String reviewer = conflict.get(12);

			TextInstance instance = new TextInstance(project, bugId, "0");
			reviewers.put(instance, reviewer);

		}
		return reviewers;
	}

}
