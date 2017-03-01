package seers.bugrepcompl.xmlcoding;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.PatternEntry;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.patterncoding.BugReport;
import seers.bugrepcompl.entity.patterncoding.DescriptionParagraph;
import seers.bugrepcompl.entity.patterncoding.DescriptionSentence;
import seers.bugrepcompl.utils.DataReader;

public class CodingCheckerAndSyncerMain {

	private static String patternCodingFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/final_data_pattern_coding";
	private static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/final_data";
	private static String bugGoldSetFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/gold-set-B-coded_by_us.csv";
	private static String patternsFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/pattern_list.csv";
	private static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data";
	private static String bugTypesPath = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/bug_types.csv";

	private static Set<String> patternList;
	private static Set<String> patternsUsed = new HashSet<>();
	static boolean includeTitle = false;
	static boolean writeSync = false;
	private static HashMap<TextInstance, String> typeOfIssues;

	public static void main(String[] args) throws Exception {
		HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(bugGoldSetFile);
		patternList = DataReader.readPatternList(patternsFile);
		typeOfIssues = DataReader.readTypeOfIssues(bugTypesPath);

		// -----------------------------------------

		try (CsvWriter bpWr = new CsvWriterBuilder(new FileWriter(outputFolder + File.separator + "bugs_patterns.csv"))
				.separator(';').build();
				CsvWriter spWr = new CsvWriterBuilder(
						new FileWriter(outputFolder + File.separator + "sent_parag_patterns.csv")).separator(';')
								.build();
				CsvWriter tiWr = new CsvWriterBuilder(new FileWriter(outputFolder + File.separator + "bug_types2.csv"))
						.separator(';').build();) {

			List<String> headers = Arrays.asList("system", "bug_id", "sys_bug", "instance_id", "sys_bug_instance",
					"pattern", "info_type", "pattern_type", "bug_type");
			bpWr.writeNext(headers);
			spWr.writeNext(headers);

			tiWr.writeNext(Arrays.asList("system", "bug_id", "sys_bug", "bug_type", "is_ob", "is_eb", "is_sr"));

			Set<Entry<TextInstance, Labels>> bugInstances = goldSet.entrySet();
			for (Entry<TextInstance, Labels> bugEntry : bugInstances) {
				TextInstance bugInstance = bugEntry.getKey();
				Labels labels = bugEntry.getValue();
				try {

					// System.out.println("Processing " + bugInstance + " ");

					String project = bugInstance.getProject();
					String bugId = bugInstance.getBugId();

					// -----------------------------------------

					File xmlFile = new File(
							patternCodingFolder + File.separator + project + File.separator + bugId + ".parse.xml");
					BugReport codedBug = XMLHelper.readXML(BugReport.class, xmlFile);

					String bugType = typeOfIssues.get(bugInstance);
					if (bugType == null) {
						throw new RuntimeException("No bug type");
					}
					List<Set<PatternEntry>> patterns = checkCodecBug(codedBug, bugInstance, bugType);

					writeEntries(bpWr, patterns.get(0));
					writeEntries(spWr, patterns.get(1));
					tiWr.writeNext(Arrays.asList(project, bugId, project + ";" + bugId, bugType, labels.getIsOB(),
							labels.getIsEB(), labels.getIsSR()));

					if (writeSync) {

						seers.bugrepcompl.entity.shortcodingparse.BugReport shortCodedBug = codedBug.toShortCodedBug();

						File xmlFile2 = new File(
								codedDataFolder + File.separator + project + File.separator + bugId + ".parse.xml");
						XMLHelper.writeXML(seers.bugrepcompl.entity.shortcodingparse.BugReport.class, shortCodedBug,
								xmlFile2);
					}

				} catch (Exception e) {
					System.err.println("Error for " + bugInstance);
					e.printStackTrace();
				}
			}
		}

		Set<String> patternsNotUsed = new HashSet<>(patternList);
		patternsNotUsed.removeAll(patternsUsed);

		System.out.println("Patterns not used: " + patternsNotUsed);

		System.out.println("Done");
	}

	private static void writeEntries(CsvWriter wr, Set<PatternEntry> list) {
		for (PatternEntry instancePattern : list) {
			wr.writeNext(Arrays.asList(instancePattern.instance.getProject(), instancePattern.instance.getBugId(),
					instancePattern.instance.getProject() + ";" + instancePattern.instance.getBugId(),
					instancePattern.instance.getInstanceId(),
					instancePattern.instance.getProject() + ";" + instancePattern.instance.getBugId() + ";"
							+ instancePattern.instance.getInstanceId(),
					instancePattern.pattern, instancePattern.infoType, instancePattern.patternType,
					instancePattern.bugType));
		}
	}

	private static List<Set<PatternEntry>> checkCodecBug(BugReport codedBug, TextInstance bugInstance, String bugType) {

		Set<PatternEntry> bugPatterns = new LinkedHashSet<>();
		Set<PatternEntry> sentParPatterns = new LinkedHashSet<>();

		// ----------------------------------
		// title

		Set<String> patterns = CodingXMLNormalizationMain.pattenrsToSet(codedBug.getTitle().getPatterns());


		if (includeTitle) {
			patternsUsed.addAll(patterns);
			
			if (patterns.stream().anyMatch(p -> !patternList.contains(p))) {
				System.err.println("Wrong patterns: " + bugInstance + ", 0.1, " + patterns);
			}
			
			for (String pattern : patterns) {

				String patternType = pattern.substring(0, 1);
				String infoType = pattern.substring(2, 4);

				sentParPatterns
						.add(new PatternEntry(new TextInstance(bugInstance.getProject(), bugInstance.getBugId(), "0.1"),
								pattern, infoType, patternType, bugType, -1));

				bugPatterns.add(new PatternEntry(bugInstance, pattern, infoType, patternType, bugType, -1));
			}
		}
		// ----------------------------------
		// paragraphs

		List<DescriptionParagraph> paragraphs = codedBug.getDescription().getParagraphs();

		for (DescriptionParagraph descriptionParagraph : paragraphs) {
			patterns = CodingXMLNormalizationMain.pattenrsToSet(descriptionParagraph.getPatterns());

			patternsUsed.addAll(patterns);
			if (patterns.stream().anyMatch(p -> !patternList.contains(p))) {
				System.err.println(
						"Wrong patterns: " + bugInstance + ", " + descriptionParagraph.getId() + ", " + patterns);
			}

			for (String pattern : patterns) {

				String patternType = pattern.substring(0, 1);
				String infoType = pattern.substring(2, 4);

				sentParPatterns.add(new PatternEntry(new TextInstance(bugInstance.getProject(), bugInstance.getBugId(),
						descriptionParagraph.getId()), pattern, infoType, patternType, bugType, -1));

				bugPatterns.add(new PatternEntry(bugInstance, pattern, infoType, patternType, bugType, -1));
			}

			// ----------------------------------
			// sentences

			List<DescriptionSentence> sentences = descriptionParagraph.getSentences();
			for (DescriptionSentence descriptionSentence : sentences) {
				patterns = CodingXMLNormalizationMain.pattenrsToSet(descriptionSentence.getPatterns());

				patternsUsed.addAll(patterns);
				if (patterns.stream().anyMatch(p -> !patternList.contains(p))) {
					System.err.println(
							"Wrong patterns: " + bugInstance + ", " + descriptionSentence.getId() + ", " + patterns);
				}

				for (String pattern : patterns) {

					String patternType = pattern.substring(0, 1);
					String infoType = pattern.substring(2, 4);

					sentParPatterns
							.add(new PatternEntry(new TextInstance(bugInstance.getProject(), bugInstance.getBugId(),
									descriptionSentence.getId()), pattern, infoType, patternType, bugType, -1));

					bugPatterns.add(new PatternEntry(bugInstance, pattern, infoType, patternType, bugType, -1));
				}
			}
		}

		// ----------------------------------

		return Arrays.asList(bugPatterns, sentParPatterns);

	}

}
