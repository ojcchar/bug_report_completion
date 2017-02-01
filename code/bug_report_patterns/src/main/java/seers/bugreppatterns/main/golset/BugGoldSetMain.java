package seers.bugreppatterns.main.golset;

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
import seers.bugrepcompl.xmlcoding.AgreementMain;
import seers.bugrepcompl.xmlcoding.CodedBug;
import seers.bugrepcompl.xmlcoding.SampleEntry;

public class BugGoldSetMain {

	//new data
	static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data_after_conflicts";
	static String completeSamplePath = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/sample/andi_students/complete_sample_seers50.csv";
	static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/final_bug_data";
	private static List<String> allowedCoders = new ArrayList<>(new HashSet<String>(
			Arrays.asList("alex", "juan", "laura", "fiorella", "jing", "oscar", "alejo", "ana", "daniel", "lau")));

	//old data
//	static String codedDataFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/coded_data_after_conflicts_old";
//	static String completeSamplePath = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/sample/andi_students/old_data_coding.csv";
//	static String outputFolder = "C:/Users/ojcch/Documents/Projects/Bug_autocompletion/coding_final_round/coding/final_bug_data_old";
//	private static List<String> allowedCoders = new ArrayList<>(new HashSet<String>(
//			Arrays.asList("alex", "juan", "laura", "fiorella", "jing", "oscar", "alejo", "ana", "daniel", "lau", "davies")));

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
						.separator(';').quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
				CsvWriter bndWr = new CsvWriterBuilder(
						new FileWriter(outputFolder + File.separator + "bugs_no_description.csv")).separator(';')
								.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();
				CsvWriter csWr = new CsvWriterBuilder(
						new FileWriter(outputFolder + File.separator + "changed_sentences.csv")).separator(';')
								.quoteChar(CsvWriter.NO_QUOTE_CHARACTER).build();) {

			writeHeaders(gsWr, nbWr, bndWr, csWr);

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

					boolean bugHasDescription = true;

					CodedBug codedBug = AgreementMain.analyzeBugRep(bugRep);

					List<List<String>> changedSentences = null;
					if (codedBug.isBug()) {

						changedSentences = cleanBugReport(bugRep, bugInstance);

						if (bugRep.getDescription() == null || bugRep.getDescription().getParagraphs() == null
								|| bugRep.getDescription().getParagraphs().isEmpty()) {
							bugHasDescription = false;
						} else {

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
					}

					writeOutputs(gsWr, nbWr, bndWr, csWr, bugInstance, codedBug, coder1, bugHasDescription,
							changedSentences);

					numOfBugs++;
				} catch (Exception e) {
					System.err.println("Error for entry " + sampleEntry);
					e.printStackTrace();
				}

			}

			System.out.println("# of bugs processed: " + numOfBugs);

		}
	}

	private static void writeOutputs(CsvWriter gsWr, CsvWriter nbWr, CsvWriter bndWr, CsvWriter csWr,
			TextInstance bugInstance, CodedBug codedBug, String coder1, boolean bugHasDescription,
			List<List<String>> changedSentences) {
		Labels labels = codedBug.getLabels();
		String codedBy = allowedCoders.contains(coder1) ? "seers" : "davies";

		if (codedBug.isBug()) {

			if (bugHasDescription) {
				List<String> entry = Arrays.asList(bugInstance.getProject(), bugInstance.getBugId(), "0",
						labels.getIsOB().toLowerCase(), labels.getIsEB().toLowerCase(), labels.getIsSR().toLowerCase(),
						codedBy);
				gsWr.writeNext(entry);
			} else {

				List<String> entry = Arrays.asList(bugInstance.getProject(), bugInstance.getBugId());
				bndWr.writeNext(entry);
			}

		} else {
			List<String> entry = Arrays.asList(bugInstance.getProject(), bugInstance.getBugId());
			nbWr.writeNext(entry);
		}

		if (changedSentences != null) {
			csWr.writeAll(changedSentences);
		}
	}

	private static void writeHeaders(CsvWriter gsWr, CsvWriter nbWr, CsvWriter bndWr, CsvWriter csWr) {

		List<String> header = Arrays.asList("system", "bug_id", "instance_id", "is_ob", "is_eb", "is_sr", "coded_by");
		gsWr.writeNext(header);

		header = Arrays.asList("system", "bug_id");
		nbWr.writeNext(header);
		bndWr.writeNext(header);

		header = Arrays.asList("system", "bug_id", "old_id", "new_id");
		csWr.writeNext(header);
	}

	private static List<List<String>> cleanBugReport(BugReport bugRep, TextInstance bugInstance) {

		BugReportDescription description = bugRep.getDescription();

		if (description == null) {
			return null;
		}

		List<DescriptionParagraph> paragraphs = description.getParagraphs();
		if (paragraphs == null) {
			return null;
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
		List<List<String>> changedLines = new ArrayList<>();

		Integer paragraphId = 1;
		for (DescriptionParagraph paragraph : paragraphs) {

			String parId = paragraph.getId();

			if (!parId.equals(paragraphId.toString())) {
				changedLines.add(
						Arrays.asList(bugInstance.getProject(), bugInstance.getBugId(), parId, paragraphId.toString()));
			}

			paragraph.setId(paragraphId.toString());

			Integer sentId = 1;
			for (DescriptionSentence sent : paragraph.getSentences()) {

				String oldSentId = sent.getId();
				String newSentId = paragraphId.toString() + "." + sentId.toString();

				if (!oldSentId.equals(newSentId)) {
					changedLines
							.add(Arrays.asList(bugInstance.getProject(), bugInstance.getBugId(), oldSentId, newSentId));
				}

				sent.setId(newSentId);
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

		return changedLines;

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
