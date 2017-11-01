package seers.bugreppatterns.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seers.bugrepcompl.entity.codingparse.LabeledDescriptionParagraph;
import seers.bugrepcompl.entity.codingparse.LabeledDescriptionSentence;
import seers.bugrepcompl.entity.regularparse.ParsedBugReport;
import seers.bugrepcompl.entity.regularparse.ParsedBugReportDescription;
import seers.bugrepcompl.entity.regularparse.ParsedDescriptionParagraph;
import seers.bugrepcompl.entity.regularparse.ParsedDescriptionSentence;
import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.entity.Paragraph;
import seers.textanalyzer.entity.Sentence;

public class ParsingUtils {

	private static final Logger LOGGER = LoggerFactory.getLogger(ParsingUtils.class);

	public static Document parseDocument(ParsedBugReport bugRep) {
		return parseDocument(" -- ", bugRep);
	}

	public static Document parseDocument(String system, ParsedBugReport bugRep) {
		Document doc = new Document(bugRep.getId());
		ParsedBugReportDescription description = bugRep.getDescription();
		if (description==null) {
			return doc;
		}
		List<ParsedDescriptionParagraph> paragraphs = description.getParagraphs();
		for (ParsedDescriptionParagraph par : paragraphs) {
			Paragraph paragraph = parseParagraph(bugRep.getId(), par);
			if (paragraph.isEmpty()) {
				LOGGER.warn("[" + system + "] Bug " + bugRep.getId() + ", paragraph " + paragraph.getId()
						+ " is empty, skipping it!");
				continue;
			}
			doc.addParagraph(paragraph);
		}
		return doc;
	}

	public static Paragraph parseParagraph(String bugId, ParsedDescriptionParagraph paragraph) {

		Paragraph par = new Paragraph(paragraph.getId());

		List<ParsedDescriptionSentence> elements = paragraph.getSentences();
		if (elements != null) {
			for (ParsedDescriptionSentence descSentence : elements) {

				String sentenceId = descSentence.getId();
				String sentenceTxt = descSentence.getValue();

				Sentence sentence = SentenceUtils.parseSentence(sentenceId, sentenceTxt);
				if (sentence != null) {
					par.addSentence(sentence);
				}

			}
		}

		return par;
	}

	public static Paragraph parseParagraph(String bugId, LabeledDescriptionParagraph paragraph) {
		Paragraph par = new Paragraph(paragraph.getId());

		List<LabeledDescriptionSentence> elements = paragraph.getSentences();
		if (elements != null) {
			for (LabeledDescriptionSentence descSentence : elements) {

				String sentenceId = descSentence.getId();
				String sentenceTxt = descSentence.getValue();

				Sentence sentence = SentenceUtils.parseSentence(sentenceId, sentenceTxt);
				if (sentence != null) {
					par.addSentence(sentence);
				}

			}
		}

		return par;
	}

}
