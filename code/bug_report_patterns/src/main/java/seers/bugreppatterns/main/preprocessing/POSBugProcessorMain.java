package seers.bugreppatterns.main.preprocessing;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.regularparse.ParsedBugReport;
import seers.bugrepcompl.entity.regularparse.ParsedDescriptionParagraph;
import seers.bugrepcompl.entity.regularparse.ParsedDescriptionSentence;
import seers.bugrepcompl.utils.DataReader;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class POSBugProcessorMain {

//	//old data (no double coding)
//	private static String xmlBugDir = "C:/Users/ojcch/Documents/Dropbox/Research/BUG_REPORT_PROJECT_JING/Data/final_data/fixed_parsed_data";
//	private static String goldSetFile = "C:/Users/ojcch/Documents/Dropbox/Research/BUG_REPORT_PROJECT_JING/Data/final_data/gold_sets4/gold-set-B-all_data.csv";
//	private static String outputDir = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/data/preprocessed_data/no_preprocessing_but_pos";
	
	//new data
//	private static String xmlBugDir = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/final_bug_data_old/regular_parsed_data";
//	private static String goldSetFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/final_bug_data_old/gold_set.csv";
//	private static String outputDir = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/final_bug_data_old/regular_parsed_data_with_pos";
	
	//all data code removed
	private static String xmlBugDir = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/code_preprocessing_original";
	private static String goldSetFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/gold-set-B-all_data.csv";
	private static String outputDir = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/final_bug_data/code_preprocessing_original_pos";

	public static void main(String[] args) throws Exception {

		HashMap<TextInstance, Labels> goldSet = DataReader.readGoldSet(goldSetFile);

		Set<TextInstance> bugInstances = goldSet.keySet();

		for (TextInstance bugInstance : bugInstances) {

			System.out.println("Processing " + bugInstance);

			ParsedBugReport bugReport = readBug(xmlBugDir, bugInstance);
			ParsedBugReport copyBug = new ParsedBugReport(bugReport);

			String title = getProcessedSentence("0", copyBug.getTitle());
			copyBug.setTitle(title);

			// ----------------

			if (copyBug.getDescription() != null) {
				List<ParsedDescriptionParagraph> paragraphs = copyBug.getDescription().getParagraphs();
				for (ParsedDescriptionParagraph descriptionParagraph : paragraphs) {
					preprocessParagraph(descriptionParagraph);
				}
			}

			writeBug(copyBug, outputDir, bugInstance);

		}
	}


	public static void writeBug(ParsedBugReport bugPreprocessed, String outputFolder, TextInstance bug) throws Exception {
		File projectFolder = new File(outputFolder + File.separator + bug.getProject() + "_parse");
		if (!projectFolder.exists()) {
			projectFolder.mkdir();
		}

		File outputFile = new File(projectFolder.getAbsolutePath() + File.separator + bug.getBugId() + ".xml.parse");
		XMLHelper.writeXML(ParsedBugReport.class, bugPreprocessed, outputFile);

	}

	private static String getProcessedSentence(String sentenceId, String sentenceTxt) {
		Sentence sentence = SentenceUtils.parseSentence(sentenceId, sentenceTxt);
		
		if (sentence==null) {
			return "";
		}
		
		String stncTxt = TextProcessor.getStringFromTermsAndPos(sentence, false);
		return stncTxt;
	}


	public static ParsedBugReport readBug(String inputFolder, TextInstance bug) throws Exception {
		String filepath = inputFolder + File.separator + bug.getProject() + "_parse" + File.separator + bug.getBugId()
				+ ".xml.parse";
		ParsedBugReport xmlBug = XMLHelper.readXML(ParsedBugReport.class, filepath);
		return xmlBug;
	}

	private static void preprocessParagraph(ParsedDescriptionParagraph descriptionParagraph) {

		List<ParsedDescriptionSentence> sentences = descriptionParagraph.getSentences();

		if (sentences == null) {
			return;
		}

		for (int i = 0; i < sentences.size(); i++) {
			ParsedDescriptionSentence descriptionSentence = sentences.get(i);

			String stncProcessed = getProcessedSentence(descriptionSentence.getId(), descriptionSentence.getValue());
			descriptionSentence.setValue(stncProcessed);

		}

	}

}
