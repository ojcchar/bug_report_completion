package seers.bugrepcompl.xmlcoding;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
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
	private static Set<String> patternList;
	private static Set<String> patternsUsed = new HashSet<>();
	static boolean includeTitle = false;

	public static void main(String[] args) throws Exception {
		HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(bugGoldSetFile);
		patternList = DataReader.readPatternList(patternsFile);

		// -----------------------------------------

		try (CsvWriter bpWr = new CsvWriterBuilder(new FileWriter(outputFolder + File.separator + "bugs_patterns.csv"))
				.separator(';').quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
				CsvWriter spWr = new CsvWriterBuilder(
						new FileWriter(outputFolder + File.separator + "sent_parag_patterns.csv")).separator(';')
								.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();) {

			List<String> headers = Arrays.asList("system", "bug_id", "instance_id", "pattern");
			bpWr.writeNext(headers);
			spWr.writeNext(headers);

			Set<Entry<TextInstance, Labels>> bugInstances = goldSet.entrySet();
			for (Entry<TextInstance, Labels> bugEntry : bugInstances) {
				TextInstance bugInstance = bugEntry.getKey();
				try {

					// System.out.println("Processing " + bugInstance + " ");

					String project = bugInstance.getProject();
					String bugId = bugInstance.getBugId();

					// -----------------------------------------

					File xmlFile = new File(
							patternCodingFolder + File.separator + project + File.separator + bugId + ".parse.xml");
					BugReport codedBug = XMLHelper.readXML(BugReport.class, xmlFile);

					List<List<InstancePattern>> patterns = checkCodecBug(codedBug, bugInstance);

					writeEntries(bpWr, patterns.get(0));
					writeEntries(spWr, patterns.get(1));

					seers.bugrepcompl.entity.shortcodingparse.BugReport shortCodedBug = codedBug.toShortCodedBug();

					File xmlFile2 = new File(
							codedDataFolder + File.separator + project + File.separator + bugId + ".parse.xml");
					XMLHelper.writeXML(seers.bugrepcompl.entity.shortcodingparse.BugReport.class, shortCodedBug,
							xmlFile2);

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

	private static void writeEntries(CsvWriter wr, List<InstancePattern> list) {
		for (InstancePattern instancePattern : list) {
			wr.writeNext(Arrays.asList(instancePattern.instance.getProject(), instancePattern.instance.getBugId(),
					instancePattern.instance.getInstanceId(), instancePattern.pattern));
		}
	}

	private static List<List<InstancePattern>> checkCodecBug(BugReport codedBug, TextInstance bugInstance) {

		List<InstancePattern> bugPatterns = new ArrayList<>();
		List<InstancePattern> sentParPatterns = new ArrayList<>();

		Set<String> bugPatternsSet = new LinkedHashSet<>();

		// ----------------------------------
		// title

		Set<String> patterns = CodingXMLNormalizationMain.pattenrsToSet(codedBug.getTitle().getPatterns());

		patternsUsed.addAll(patterns);

		if (patterns.stream().anyMatch(p -> !patternList.contains(p))) {
			System.err.println("Wrong patterns: " + bugInstance + ", 0.1, " + patterns);
		}

		if (includeTitle) {
			bugPatternsSet.addAll(patterns);
			for (String pattern : patterns) {
				sentParPatterns.add(new InstancePattern(
						new TextInstance(bugInstance.getProject(), bugInstance.getBugId(), "0.1"), pattern));
			}
		}
		// ----------------------------------
		// paragraphs

		List<DescriptionParagraph> paragraphs = codedBug.getDescription().getParagraphs();

		for (DescriptionParagraph descriptionParagraph : paragraphs) {
			patterns = CodingXMLNormalizationMain.pattenrsToSet(descriptionParagraph.getPatterns());

			patternsUsed.addAll(patterns);
			bugPatternsSet.addAll(patterns);
			if (patterns.stream().anyMatch(p -> !patternList.contains(p))) {
				System.err.println(
						"Wrong patterns: " + bugInstance + ", " + descriptionParagraph.getId() + ", " + patterns);
			}

			for (String pattern : patterns) {
				sentParPatterns.add(new InstancePattern(new TextInstance(bugInstance.getProject(),
						bugInstance.getBugId(), descriptionParagraph.getId()), pattern));
			}

			// ----------------------------------
			// sentences
			
			List<DescriptionSentence> sentences = descriptionParagraph.getSentences();
			for (DescriptionSentence descriptionSentence : sentences) {
				patterns = CodingXMLNormalizationMain.pattenrsToSet(descriptionSentence.getPatterns());

				patternsUsed.addAll(patterns);
				bugPatternsSet.addAll(patterns);
				if (patterns.stream().anyMatch(p -> !patternList.contains(p))) {
					System.err.println(
							"Wrong patterns: " + bugInstance + ", " + descriptionSentence.getId() + ", " + patterns);
				}

				for (String pattern : patterns) {
					sentParPatterns.add(new InstancePattern(
							new TextInstance(bugInstance.getProject(), bugInstance.getBugId(), descriptionSentence.getId()),
							pattern));
				}
			}
		}


		// ----------------------------------

		for (String pattern : bugPatternsSet) {
			bugPatterns.add(new InstancePattern(bugInstance, pattern));
		}

		return Arrays.asList(bugPatterns, sentParPatterns);

	}

	public static class InstancePattern {
		public TextInstance instance;
		public String pattern;

		public InstancePattern(TextInstance instance, String pattern) {
			super();
			this.instance = instance;
			this.pattern = pattern;
		}

	}

}
