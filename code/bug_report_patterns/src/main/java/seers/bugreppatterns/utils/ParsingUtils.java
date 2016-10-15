package seers.bugreppatterns.utils;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.entity.xml.BugReport;
import seers.bugreppatterns.entity.xml.DescriptionParagraph;
import seers.bugreppatterns.entity.xml.DescriptionSentence;
import seers.textanalyzer.entity.Sentence;

public class ParsingUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ParsingUtils.class);
	
	
	public static Document parseDocument(String system, BugReport bugRep) {
		Document doc = new Document(bugRep.getId());
		List<DescriptionParagraph> paragraphs = bugRep.getDescription().getParagraphs();
		for (DescriptionParagraph par : paragraphs) {
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
	
	public static Paragraph parseParagraph(String bugId, DescriptionParagraph paragraph) {

		Paragraph par = new Paragraph(paragraph.getId());

		List<DescriptionSentence> elements = paragraph.getSentences();
		if (elements != null) {
			for (DescriptionSentence descSentence : elements) {

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
