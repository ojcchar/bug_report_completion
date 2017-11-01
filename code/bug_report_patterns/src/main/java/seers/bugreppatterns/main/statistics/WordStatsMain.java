package seers.bugreppatterns.main.statistics;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReport;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledBugReportDescription;
import seers.bugrepcompl.entity.shortcodingparse.ShortLabeledDescriptionSentence;
import seers.bugrepcompl.utils.DataReader;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class WordStatsMain {

	// no preprocessed data
	// private static String codedDataFolder =
	// "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/final_data";

	// preprocessed data
	private static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/data/code_preprocessing_coded";

	private static String bugGoldSetFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/gold-set-B-all_data.csv";
	static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/analysis";

	static HashMap<TextInstance, InstanceStats> bugStats = new HashMap<>();

	public static void main(String[] args) throws Exception {
		HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(bugGoldSetFile);

		Set<Entry<TextInstance, Labels>> bugInstances = goldSet.entrySet();

		try (CsvWriter writer = new CsvWriterBuilder(new FileWriter(outputFolder + File.separator + "words-stats.csv"))
				.separator(';').build();) {
			for (Entry<TextInstance, Labels> bugEntry : bugInstances) {

				TextInstance bugInstance = bugEntry.getKey();
				try {

					System.out.println("Processing " + bugInstance + " ");

					String project = bugInstance.getProject();
					String bugId = bugInstance.getBugId();

					File xmlFile = new File(
							codedDataFolder + File.separator + project + File.separator + bugId + ".parse.xml");
					ShortLabeledBugReport bugRep = XMLHelper.readXML(ShortLabeledBugReport.class, xmlFile);

					processBug(bugRep, bugInstance, bugEntry.getValue());

				} catch (Exception e) {
					System.err.println("Error for " + bugInstance);
					e.printStackTrace();
				}

			}

			writeStats(writer);

		}
	}

	private static void writeStats(CsvWriter writer) throws Exception {

		writer.writeNext(Arrays.asList("system", "bug_id", "num_words", "num_unique_words", "num_unique_lemmas",
				"used_for_patterns"));

		Set<Entry<TextInstance, InstanceStats>> entrySet = bugStats.entrySet();

		for (Entry<TextInstance, InstanceStats> entry : entrySet) {
			TextInstance key = entry.getKey();
			InstanceStats value2 = entry.getValue();
			String[] value = new String[] { key.getProject(), key.getBugId(), value2.numWords + "",
					value2.numUniqueWords + "", value2.numUniqueLemma + "", value2.usedForPatterns };
			writer.writeNext(Arrays.asList(value));
		}
	}

	private static void processBug(ShortLabeledBugReport bugRep, TextInstance bugInstance, Labels bugLabels) {

		ShortLabeledBugReportDescription description = bugRep.getDescription();
		if (description == null) {
			return;
		}

		List<ShortLabeledDescriptionSentence> allSentences = description.getAllSentences();

		InstanceStats stats = bugStats.get(bugInstance);
		if (stats == null) {
			stats = new InstanceStats();
			bugStats.put(bugInstance, stats);
		}

		Set<String> uniqueWords = new HashSet<>();
		Set<String> uniqueLemmas = new HashSet<>();

		for (ShortLabeledDescriptionSentence sentence : allSentences) {
			Sentence parsedSentence = SentenceUtils.parseSentence(sentence.getId(), sentence.getValue());

			if (parsedSentence == null) {
				continue;
			}
			List<Token> tokens = parsedSentence.getTokens();

			stats.numWords += tokens.size();

			for (Token token : tokens) {
				uniqueWords.add(token.getWord().toLowerCase().trim());
				uniqueLemmas.add(token.getLemma().toLowerCase().trim());
			}

		}

		stats.numUniqueWords = uniqueWords.size();
		stats.numUniqueLemma = uniqueLemmas.size();
		stats.usedForPatterns = bugLabels.getCodedBy();
		uniqueWords = null;
		uniqueLemmas = null;
	}

	public static class InstanceStats {
		public int numWords;
		public int numUniqueWords;
		public int numUniqueLemma;
		public String usedForPatterns;

	}

}
