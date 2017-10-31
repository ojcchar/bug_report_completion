package seers.nimbus.bugparser.sentence;

import java.util.ArrayList;
import java.util.List;

import seers.bugrepcompl.entity.BugReport;
import seers.bugrepcompl.entity.regularparse.BugReportDescription;
import seers.bugrepcompl.entity.regularparse.DescriptionParagraph;
import seers.bugrepcompl.entity.regularparse.DescriptionSentence;

public class BugReportSentenceParser {

	public static seers.bugrepcompl.entity.regularparse.BugReport parseBugReport(BugReport bugRep) {

		List<List<String>> paragraphSplit = SentenceSplitter.splitIntoParagraphsAndSentences(bugRep.getDescription());
		BugReportDescription parsedDescription = createParsedDescription(paragraphSplit);

		seers.bugrepcompl.entity.regularparse.BugReport parsedBugReport = new seers.bugrepcompl.entity.regularparse.BugReport();
		parsedBugReport.setDescription(parsedDescription);
		parsedBugReport.setId(bugRep.getId());
		parsedBugReport.setTitle(bugRep.getTitle());

		return parsedBugReport;

	}

	public static seers.bugrepcompl.entity.codingparse.BugReport parseBugReportForCoding(BugReport bugRep) {

		List<List<String>> paragraphSplit = SentenceSplitter.splitIntoParagraphsAndSentences(bugRep.getDescription());
		seers.bugrepcompl.entity.codingparse.BugReportDescription parsedDescription = createParsedCodingDescription(
				paragraphSplit);

		seers.bugrepcompl.entity.codingparse.BugReport parsedBugReport = new seers.bugrepcompl.entity.codingparse.BugReport();
		parsedBugReport.setDescription(parsedDescription);
		parsedBugReport.setId(bugRep.getId());
		parsedBugReport.setTitle(
				new seers.bugrepcompl.entity.codingparse.BugReportTitle(bugRep.getTitle()));

		return parsedBugReport;
	}

	public static seers.bugrepcompl.entity.patterncoding.BugReport parseBugReportForPatternCoding(BugReport bugRep) {

		List<List<String>> paragraphSplit = SentenceSplitter.splitIntoParagraphsAndSentences(bugRep.getDescription());
		seers.bugrepcompl.entity.patterncoding.BugReportDescription parsedDescription = createParsedPatternCodingDescription(
				paragraphSplit);

		seers.bugrepcompl.entity.patterncoding.BugReport parsedBugReport = new seers.bugrepcompl.entity.patterncoding.BugReport();
		parsedBugReport.setDescription(parsedDescription);
		parsedBugReport.setId(bugRep.getId());
		parsedBugReport.setTitle(
				new seers.bugrepcompl.entity.patterncoding.BugReportTitle(null, null, null, null, bugRep.getTitle()));

		return parsedBugReport;
	}

	private static seers.bugrepcompl.entity.codingparse.BugReportDescription createParsedCodingDescription(
			List<List<String>> paragraphSplit) {
		seers.bugrepcompl.entity.codingparse.BugReportDescription desc = new seers.bugrepcompl.entity.codingparse.BugReportDescription();

		List<seers.bugrepcompl.entity.codingparse.DescriptionParagraph> paragraphs = new ArrayList<>();
		for (int i = 1; i <= paragraphSplit.size(); i++) {

			List<String> sentenceSplit = paragraphSplit.get(i - 1);
			if (sentenceSplit.isEmpty()) {
				continue;
			}

			// -----------------------

			seers.bugrepcompl.entity.codingparse.DescriptionParagraph paragraph = new seers.bugrepcompl.entity.codingparse.DescriptionParagraph();
			paragraph.setId(i + "");

			List<seers.bugrepcompl.entity.codingparse.DescriptionSentence> sentences = new ArrayList<>();

			for (int j = 1; j <= sentenceSplit.size(); j++) {
				seers.bugrepcompl.entity.codingparse.DescriptionSentence sent = new seers.bugrepcompl.entity.codingparse.DescriptionSentence();
				sent.setId(i + "." + j);
				sent.setValue(sentenceSplit.get(j - 1));
				sentences.add(sent);
			}

			paragraph.setSentences(sentences);
			paragraphs.add(paragraph);

		}

		if (paragraphs.isEmpty()) {
			return null;
		}

		desc.setParagraphs(paragraphs);

		return desc;
	}

	private static BugReportDescription createParsedDescription(List<List<String>> paragraphSplit) {

		BugReportDescription desc = new BugReportDescription();

		List<DescriptionParagraph> paragraphs = new ArrayList<>();
		for (int i = 1; i <= paragraphSplit.size(); i++) {

			List<String> sentenceSplit = paragraphSplit.get(i - 1);
			if (sentenceSplit.isEmpty()) {
				continue;
			}

			// -----------------------

			DescriptionParagraph paragraph = new DescriptionParagraph();
			paragraph.setId(i + "");

			List<DescriptionSentence> sentences = new ArrayList<>();

			for (int j = 1; j <= sentenceSplit.size(); j++) {
				DescriptionSentence sent = new DescriptionSentence();
				sent.setId(i + "." + j);
				sent.setValue(sentenceSplit.get(j - 1));
				sentences.add(sent);
			}

			paragraph.setSentences(sentences);
			paragraphs.add(paragraph);

		}

		if (paragraphs.isEmpty()) {
			return null;
		}

		desc.setParagraphs(paragraphs);

		return desc;
	}

	private static seers.bugrepcompl.entity.patterncoding.BugReportDescription createParsedPatternCodingDescription(
			List<List<String>> paragraphSplit) {
		seers.bugrepcompl.entity.patterncoding.BugReportDescription desc = new seers.bugrepcompl.entity.patterncoding.BugReportDescription();

		List<seers.bugrepcompl.entity.patterncoding.DescriptionParagraph> paragraphs = new ArrayList<>();
		for (int i = 1; i <= paragraphSplit.size(); i++) {

			List<String> sentenceSplit = paragraphSplit.get(i - 1);
			if (sentenceSplit.isEmpty()) {
				continue;
			}

			// -----------------------

			seers.bugrepcompl.entity.patterncoding.DescriptionParagraph paragraph = new seers.bugrepcompl.entity.patterncoding.DescriptionParagraph();
			paragraph.setId(i + "");

			List<seers.bugrepcompl.entity.patterncoding.DescriptionSentence> sentences = new ArrayList<>();

			for (int j = 1; j <= sentenceSplit.size(); j++) {
				seers.bugrepcompl.entity.patterncoding.DescriptionSentence sent = new seers.bugrepcompl.entity.patterncoding.DescriptionSentence();
				sent.setId(i + "." + j);
				sent.setValue(sentenceSplit.get(j - 1));
				sentences.add(sent);
			}

			paragraph.setSentences(sentences);
			paragraphs.add(paragraph);

		}

		if (paragraphs.isEmpty()) {
			return null;
		}

		desc.setParagraphs(paragraphs);

		return desc;
	}

}
