package seers.bugreppatterns.processor;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.List;

import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.regularparse.ParsedBugReport;
import seers.bugrepcompl.entity.regularparse.ParsedBugReportDescription;
import seers.bugrepcompl.entity.regularparse.ParsedDescriptionParagraph;
import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.PredictionOutput;
import seers.bugreppatterns.utils.ParsingUtils;
import seers.textanalyzer.entity.Sentence;

public class SentenceProcessor extends TextInstanceProcessor {

	public SentenceProcessor(ThreadParameters params) {
		super(params);
	}

	@Override
	public void executeJob() throws Exception {

		for (File file : files) {

			try {
				ParsedBugReport bugRep = XMLHelper.readXML(ParsedBugReport.class, file);

				ParsedBugReportDescription description = bugRep.getDescription();
				if (description == null) {
					continue;
				}
				List<ParsedDescriptionParagraph> paragraphs = description.getParagraphs();
				if (paragraphs == null) {
					continue;
				}

				for (ParsedDescriptionParagraph paragraph : paragraphs) {

					Paragraph par = ParsingUtils.parseParagraph(bugRep.getId(), paragraph);

					if (par.isEmpty()) {
						continue;
					}

					for (Sentence sentence : par.getSentences()) {

						LinkedHashMap<PatternMatcher, Integer> patternMatches = new LinkedHashMap<>();

						for (PatternMatcher patternMatcher : patterns) {
							int matchSentence = patternMatcher.matchSentence(sentence);
							if (matchSentence > 0) {
								patternMatches.put(patternMatcher, matchSentence);
							}
						}

						PredictionOutput predictionOutput = predictor.predictLabels(bugRep.getId(), sentence.getId(),
								patternMatches);

						writePreFeatures(bugRep.getId(), sentence.getId(), predictionOutput.getFeatures());
						writePrediction(bugRep.getId(), sentence.getId(), predictionOutput.getLabels());
					}
				}
			} catch (Exception e) {
				LOGGER.error("[" + system + "] Error for file: " + file, e);
			}
		}

	}

}
