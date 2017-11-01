package seers.nimbus.bugparser.sentence;

import java.util.ArrayList;
import java.util.List;

import seers.bugrepcompl.entity.BugReport;
import seers.bugrepcompl.entity.codingparse.LabeledBugReport;
import seers.bugrepcompl.entity.codingparse.LabeledBugReportDescription;
import seers.bugrepcompl.entity.codingparse.LabeledBugReportTitle;
import seers.bugrepcompl.entity.codingparse.LabeledDescriptionParagraph;
import seers.bugrepcompl.entity.codingparse.LabeledDescriptionSentence;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReport;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReportDescription;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReportTitle;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionParagraph;
import seers.bugrepcompl.entity.patterncoding.PatternLabeledDescriptionSentence;
import seers.bugrepcompl.entity.regularparse.ParsedBugReport;
import seers.bugrepcompl.entity.regularparse.ParsedBugReportDescription;
import seers.bugrepcompl.entity.regularparse.ParsedDescriptionParagraph;
import seers.bugrepcompl.entity.regularparse.ParsedDescriptionSentence;

public class BugReportSentenceParser {

	public static ParsedBugReport parseBugReport(BugReport bugRep) {

		List<List<String>> paragraphSplit = SentenceSplitter.splitIntoParagraphsAndSentences(bugRep.getDescription());
		ParsedBugReportDescription parsedDescription = createParsedDescription(paragraphSplit);

		ParsedBugReport parsedBugReport = new ParsedBugReport();
		parsedBugReport.setDescription(parsedDescription);
		parsedBugReport.setId(bugRep.getId());
		parsedBugReport.setTitle(bugRep.getTitle());

		return parsedBugReport;

	}

	public static LabeledBugReport parseBugReportForCoding(BugReport bugRep) {

		List<List<String>> paragraphSplit = SentenceSplitter.splitIntoParagraphsAndSentences(bugRep.getDescription());
		LabeledBugReportDescription parsedDescription = createParsedCodingDescription(
				paragraphSplit);

		LabeledBugReport parsedBugReport = new LabeledBugReport();
		parsedBugReport.setDescription(parsedDescription);
		parsedBugReport.setId(bugRep.getId());
		parsedBugReport.setTitle(
				new LabeledBugReportTitle(bugRep.getTitle()));

		return parsedBugReport;
	}

	public static PatternLabeledBugReport parseBugReportForPatternCoding(BugReport bugRep) {

		List<List<String>> paragraphSplit = SentenceSplitter.splitIntoParagraphsAndSentences(bugRep.getDescription());
		PatternLabeledBugReportDescription parsedDescription = createParsedPatternCodingDescription(
				paragraphSplit);

		PatternLabeledBugReport parsedBugReport = new PatternLabeledBugReport();
		parsedBugReport.setDescription(parsedDescription);
		parsedBugReport.setId(bugRep.getId());
		parsedBugReport.setTitle(
				new PatternLabeledBugReportTitle(null, null, null, null, bugRep.getTitle()));

		return parsedBugReport;
	}

	private static LabeledBugReportDescription createParsedCodingDescription(
			List<List<String>> paragraphSplit) {
		LabeledBugReportDescription desc = new LabeledBugReportDescription();

		List<LabeledDescriptionParagraph> paragraphs = new ArrayList<>();
		for (int i = 1; i <= paragraphSplit.size(); i++) {

			List<String> sentenceSplit = paragraphSplit.get(i - 1);
			if (sentenceSplit.isEmpty()) {
				continue;
			}

			// -----------------------

			LabeledDescriptionParagraph paragraph = new LabeledDescriptionParagraph();
			paragraph.setId(i + "");

			List<LabeledDescriptionSentence> sentences = new ArrayList<>();

			for (int j = 1; j <= sentenceSplit.size(); j++) {
				LabeledDescriptionSentence sent = new LabeledDescriptionSentence();
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

	private static ParsedBugReportDescription createParsedDescription(List<List<String>> paragraphSplit) {

		ParsedBugReportDescription desc = new ParsedBugReportDescription();

		List<ParsedDescriptionParagraph> paragraphs = new ArrayList<>();
		for (int i = 1; i <= paragraphSplit.size(); i++) {

			List<String> sentenceSplit = paragraphSplit.get(i - 1);
			if (sentenceSplit.isEmpty()) {
				continue;
			}

			// -----------------------

			ParsedDescriptionParagraph paragraph = new ParsedDescriptionParagraph();
			paragraph.setId(i + "");

			List<ParsedDescriptionSentence> sentences = new ArrayList<>();

			for (int j = 1; j <= sentenceSplit.size(); j++) {
				ParsedDescriptionSentence sent = new ParsedDescriptionSentence();
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

	private static PatternLabeledBugReportDescription createParsedPatternCodingDescription(
			List<List<String>> paragraphSplit) {
		PatternLabeledBugReportDescription desc = new PatternLabeledBugReportDescription();

		List<PatternLabeledDescriptionParagraph> paragraphs = new ArrayList<>();
		for (int i = 1; i <= paragraphSplit.size(); i++) {

			List<String> sentenceSplit = paragraphSplit.get(i - 1);
			if (sentenceSplit.isEmpty()) {
				continue;
			}

			// -----------------------

			PatternLabeledDescriptionParagraph paragraph = new PatternLabeledDescriptionParagraph();
			paragraph.setId(i + "");

			List<PatternLabeledDescriptionSentence> sentences = new ArrayList<>();

			for (int j = 1; j <= sentenceSplit.size(); j++) {
				PatternLabeledDescriptionSentence sent = new PatternLabeledDescriptionSentence();
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
