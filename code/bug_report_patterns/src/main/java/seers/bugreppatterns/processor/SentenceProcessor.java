package seers.bugreppatterns.processor;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.xml.XMLHelper;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.entity.xml.BugReport;
import seers.bugreppatterns.entity.xml.DescriptionParagraph;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;

public class SentenceProcessor extends TextInstanceProcessor {

	public SentenceProcessor(ThreadParameters params) {
		super(params);
	}

	@Override
	public void executeJob() throws Exception {

		for (File file : files) {

			try {
				BugReport bugRep = XMLHelper.readXML(BugReport.class, file);
				List<DescriptionParagraph> paragraphs = bugRep.getDescription().getParagraphs();

				for (DescriptionParagraph paragraph : paragraphs) {

					Paragraph sentences = parseParagraph(bugRep.getId(), paragraph);

					for (Sentence sentence : sentences.getSentences()) {

						LinkedHashMap<PatternMatcher, Integer> patternMatches = new LinkedHashMap<>();

						for (PatternMatcher patternMatcher : patterns) {
							int matchSentence = patternMatcher.matchSentence(sentence);
							if (matchSentence > 0) {
								patternMatches.put(patternMatcher, matchSentence);
							}
						}

						writePrediction(bugRep.getId(), sentence.getId(), patternMatches);
						writeFeatures(bugRep.getId(), sentence.getId(), patternMatches);
					}
				}
			} catch (Exception e) {
				LOGGER.error("[" + system + "] Error for file: " + file);
			}
		}

	}

}
