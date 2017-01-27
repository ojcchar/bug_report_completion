package seers.bugrepcompl.xmlcoding;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import net.quux00.simplecsv.CsvWriter;
import net.quux00.simplecsv.CsvWriterBuilder;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.Labels;
import seers.bugrepcompl.entity.TextInstance;
import seers.bugrepcompl.entity.shortcodingparse.BugReport;
import seers.bugrepcompl.entity.shortcodingparse.BugReportDescription;
import seers.bugrepcompl.entity.shortcodingparse.DescriptionParagraph;
import seers.bugrepcompl.entity.shortcodingparse.DescriptionSentence;

public class GoldSetMain {

	static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data_after_conflicts";
	static String completeSamplePath = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/sample/andi_students/complete_sample_seers50.csv";
	static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/final_bug_data";
	private static List<String> allowedCoders = new ArrayList<>(new HashSet<String>(
			Arrays.asList("alex", "juan", "laura", "fiorella", "jing", "oscar", "alejo", "ana", "daniel", "lau")));

	public static void main(String[] args) throws Exception {

		// read complete sample
		List<SampleEntry> sample = AgreementMain.readCompleteSample(completeSamplePath, allowedCoders);

		int numOfBugs = 0;

		// final data dir
		File finalDataFolder = new File(outputFolder + File.separator + "final_data");
		if (!finalDataFolder.exists()) {
			finalDataFolder.mkdir();
		}

		try (CsvWriter gsWr = new CsvWriterBuilder(new FileWriter(outputFolder + File.separator + "gold_set.csv"))
				.separator(';').quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
				CsvWriter nbWr = new CsvWriterBuilder(new FileWriter(outputFolder + File.separator + "no_bugs.csv"))
						.separator(';').quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();) {

			writeHeaders(gsWr, nbWr);

			// for each entry in complete sample
			for (SampleEntry sampleEntry : sample) {

				TextInstance bugInstance = sampleEntry.getInstance();
				String project = bugInstance.getProject();
				String bugId = bugInstance.getBugId();
				String coder1 = sampleEntry.getCoder1();

				try {

					File xmlFile = new File(
							codedDataFolder + File.separator + project + File.separator + bugId + ".parse.xml");
					BugReport bugRep = XMLHelper.readXML(BugReport.class, xmlFile);

					CodedBug codedBug = AgreementMain.analyzeBugRep(bugRep);

					if (codedBug.isBug()) {

						cleanBugReport(bugRep, bugInstance);

						CodedBug codedBug2 = AgreementMain.analyzeBugRep(bugRep);

						if (!codedBug.equals(codedBug2)) {
							throw new RuntimeException("Coding is not the same after cleaning");
						}

						File systemFolder = new File(finalDataFolder + File.separator + project);
						if (!systemFolder.exists()) {
							systemFolder.mkdir();
						}
						File bugFile = new File(systemFolder + File.separator + bugId + ".parse.xml");
						XMLHelper.writeXML(BugReport.class, bugRep, bugFile);
					}

					writeOutputs(gsWr, nbWr, bugInstance, codedBug, coder1);

					numOfBugs++;
				} catch (Exception e) {
					System.err.println("Error for entry " + sampleEntry);
					e.printStackTrace();
				}

			}

			System.out.println("# of bugs processed: " + numOfBugs);

		}
	}

	private static void writeOutputs(CsvWriter gsWr, CsvWriter nbWr, TextInstance bugInstance, CodedBug codedBug,
			String coder1) {
		Labels labels = codedBug.getLabels();
		String codedBy = allowedCoders.contains(coder1) ? "seers" : "davies";

		if (codedBug.isBug()) {
			List<String> entry = Arrays.asList(bugInstance.getProject(), bugInstance.getBugId(), "0",
					labels.getIsOB().toLowerCase(), labels.getIsEB().toLowerCase(), labels.getIsSR().toLowerCase(),
					codedBy);
			gsWr.writeNext(entry);

		} else {
			List<String> entry = Arrays.asList(bugInstance.getProject(), bugInstance.getBugId());
			nbWr.writeNext(entry);
		}
	}

	private static void writeHeaders(CsvWriter gsWr, CsvWriter nbWr) {

		List<String> header = Arrays.asList("system", "bug_id", "instance_id", "is_ob", "is_eb", "is_sr", "coded_by");
		gsWr.writeNext(header);

		header = Arrays.asList("system", "bug_id");
		nbWr.writeNext(header);
	}

	private static void cleanBugReport(BugReport bugRep, TextInstance bugInstance) {

		BugReportDescription description = bugRep.getDescription();

		if (description == null) {
			return;
		}

		List<DescriptionParagraph> paragraphs = description.getParagraphs();
		if (paragraphs == null) {
			return;
		}
		List<DescriptionParagraph> paragraphsToDelete = new ArrayList<>();

		boolean bugChanged = false;
		boolean mergedLabels = false;

		for (DescriptionParagraph paragraph : paragraphs) {

			List<DescriptionSentence> sentences = paragraph.getSentences();

			// detect empty paragraphs
			if (sentences == null || sentences.isEmpty()) {
				paragraphsToDelete.add(paragraph);
				bugChanged = true;
			} else {

				// --------------------

				// detect empty sentences
				List<DescriptionSentence> sentencesToDelete = new ArrayList<>();
				for (DescriptionSentence sent : sentences) {
					if (sent.getValue().trim().isEmpty()) {
						sentencesToDelete.add(sent);
						bugChanged = true;
					}
				}

				// ----------------------
				// remove empty sentences

				sentences.removeAll(sentencesToDelete);

				// ---------------------
				// identify empty paragraph if it ends up with no sentences

				if (sentences.isEmpty()) {
					paragraphsToDelete.add(paragraph);
					bugChanged = true;
				} else {
					// (paragraphs with 1 sentence) --> merge the labels of the
					// paragraph and sentence

					DescriptionSentence firstSent = sentences.get(0);
					if (sentences.size() == 1 && (!firstSent.getOb().equals(paragraph.getOb())
							|| !firstSent.getEb().equals(paragraph.getEb())
							|| !firstSent.getSr().equals(paragraph.getSr()))) {
						mergeLabels(paragraph, sentences.get(0));
						bugChanged = true;
						mergedLabels = true;
					}
				}

			}
		}

		// ----------------------------------
		// remove empty paragraphs

		paragraphs.removeAll(paragraphsToDelete);

		// ----------------------------------
		// re-number the sentences

		Integer paragraphId = 1;
		for (DescriptionParagraph paragraph : paragraphs) {
			paragraph.setId(paragraphId.toString());

			Integer sentId = 1;
			for (DescriptionSentence sent : paragraph.getSentences()) {
				sent.setId(paragraphId.toString() + "." + sentId.toString());
				sentId++;
			}

			paragraphId++;
		}

		// ---------------------------------------

		if (bugChanged) {
			if (mergedLabels) {
				System.out.println("Bug merged labels: " + bugInstance);
			} else {
				System.out.println("Bug changed: " + bugInstance);
			}
		}

	}

	private static void mergeLabels(DescriptionParagraph paragraph, DescriptionSentence sent) {

		Labels labels = new Labels(paragraph.getOb(), paragraph.getEb(), paragraph.getSr());
		Labels label = new Labels(sent.getOb(), sent.getEb(), sent.getSr());
		Labels mergedLabels = AgreementMain.mergeLabels(labels, label);

		paragraph.setOb(mergedLabels.getIsOB());
		paragraph.setEb(mergedLabels.getIsEB());
		paragraph.setSr(mergedLabels.getIsSR());

		sent.setOb(mergedLabels.getIsOB());
		sent.setEb(mergedLabels.getIsEB());
		sent.setSr(mergedLabels.getIsSR());
	}

}
