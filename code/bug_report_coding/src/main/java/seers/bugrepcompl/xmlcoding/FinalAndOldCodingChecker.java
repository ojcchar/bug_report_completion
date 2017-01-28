package seers.bugrepcompl.xmlcoding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.shortcodingparse.BugReport;
import seers.bugrepcompl.entity.shortcodingparse.DescriptionParagraph;
import seers.bugrepcompl.entity.shortcodingparse.DescriptionSentence;

public class FinalAndOldCodingChecker {

	static String oldDataCodingFile = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/sample/andi_students/old_data_coding.csv";
	static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_old_data - Copy";
	static String finalCodingFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data_after_conflicts_old";
	// no davies
	private static List<String> allowedCoders = new ArrayList<>(new HashSet<String>(
			Arrays.asList("juan", "laura", "fiorella", "jing", "oscar", "alejo", "ana", "daniel", "lau")));

	public static void main(String[] args) throws Exception {

		// read coded by seers
		List<SampleEntry> sample = AgreementMain.readCompleteSample(oldDataCodingFile, allowedCoders);
		int numConflicts = 0;
		int numComparisons = 0;

		for (SampleEntry sampleEntry : sample) {

			TextInstance bugInstance = sampleEntry.getInstance();
			String project = bugInstance.getProject();
			String bugId = bugInstance.getBugId();
			String coder1 = sampleEntry.getCoder1();

			File oldFile = new File(codedDataFolder + File.separator + coder1 + File.separator + "bugs_parsed"
					+ File.separator + project + File.separator + bugId + ".parse.xml");
			File finalFile = new File(
					finalCodingFolder + File.separator + project + File.separator + bugId + ".parse.xml");

			if (finalFile.exists()) {
				numComparisons++;

				BugReport oldCodingBug = XMLHelper.readXML(BugReport.class, oldFile);
				BugReport finalBug = XMLHelper.readXML(BugReport.class, finalFile);

				boolean conflict = checkConflict(oldCodingBug, finalBug);
				if (conflict) {
					numConflicts++;
					System.err.println("Conflict: " + sampleEntry);
				}

			} else {
				System.out.println("No comparison for " + sampleEntry);
			}

		}
		System.out.println("# conflicts: " + numConflicts + "/" + numComparisons);

	}

	private static boolean checkConflict(BugReport oldCodingBug, BugReport finalBug) {

		if (!oldCodingBug.getId().equals(finalBug.getId())) {
			return true;
		}

		if (!oldCodingBug.getTitle().equals(finalBug.getTitle())) {
			return true;
		}

		if (!oldCodingBug.getNoBug().equals(finalBug.getNoBug())) {
			return true;
		}

		List<DescriptionParagraph> oldParagraphs = oldCodingBug.getDescription().getParagraphs();
		List<DescriptionParagraph> finalParagraphs = finalBug.getDescription().getParagraphs();

		boolean conflict = checkConflictsInParagraphs(oldParagraphs, finalParagraphs);
		if (conflict) {
			return true;
		}

		List<DescriptionSentence> allOldSentences = oldCodingBug.getDescription().getAllSentences();
		List<DescriptionSentence> allFinalSentences = finalBug.getDescription().getAllSentences();

		boolean conflict2 = checkConflicts(allOldSentences, allFinalSentences);
		if (conflict2) {
			return true;
		}

		return false;
	}

	private static boolean checkConflicts(List<DescriptionSentence> allOldSentences,
			List<DescriptionSentence> allFinalSentences) {

		if (allOldSentences.size() != allFinalSentences.size()) {
			return true;
		}

		for (int i = 0; i < allOldSentences.size(); i++) {
			DescriptionSentence oldSent = allOldSentences.get(i);
			DescriptionSentence finalSent = allFinalSentences.get(i);

			if (!oldSent.getId().equals(finalSent.getId())) {
				return true;
			}

			if (!oldSent.getValue().equals(finalSent.getValue())) {
				return true;
			}

			if (!oldSent.getOb().equals(finalSent.getOb())) {
				return true;
			}

			if (!oldSent.getEb().equals(finalSent.getEb())) {
				return true;
			}

			if (!oldSent.getSr().equals(finalSent.getSr())) {
				return true;
			}
		}

		return false;
	}

	private static boolean checkConflictsInParagraphs(List<DescriptionParagraph> oldParagraphs,
			List<DescriptionParagraph> finalParagraphs) {

		if (oldParagraphs.size() != finalParagraphs.size()) {
			return true;
		}

		for (int i = 0; i < oldParagraphs.size(); i++) {
			DescriptionParagraph oldPar = oldParagraphs.get(i);
			DescriptionParagraph finalPar = finalParagraphs.get(i);

			if (!oldPar.getId().equals(finalPar.getId())) {
				return true;
			}

			if (!oldPar.getOb().equals(finalPar.getOb())) {
				return true;
			}

			if (!oldPar.getEb().equals(finalPar.getEb())) {
				return true;
			}

			if (!oldPar.getSr().equals(finalPar.getSr())) {
				return true;
			}
		}

		return false;
	}

}
