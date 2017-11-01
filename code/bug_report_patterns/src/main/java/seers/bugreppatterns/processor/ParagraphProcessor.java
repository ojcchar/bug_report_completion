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

public class ParagraphProcessor extends TextInstanceProcessor {

	public ParagraphProcessor(ThreadParameters params) {
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

					LinkedHashMap<PatternMatcher, Integer> patternMatches = new LinkedHashMap<>();

					Paragraph parsedParagraph = ParsingUtils.parseParagraph(bugRep.getId(), paragraph);

					if (parsedParagraph.isEmpty()) {
						continue;
					}

					for (PatternMatcher patternMatcher : patterns) {
						int numMatches = patternMatcher.matchParagraph(parsedParagraph);
						if (numMatches > 0) {
							patternMatches.put(patternMatcher, numMatches);
						}
					}

					PredictionOutput predictionOutput = predictor.predictLabels(bugRep.getId(), paragraph.getId(),
							patternMatches);

					writePreFeatures(bugRep.getId(), paragraph.getId(), predictionOutput.getFeatures());
					writePrediction(bugRep.getId(), paragraph.getId(), predictionOutput.getLabels());
				}
			} catch (Exception e) {
				LOGGER.error("[" + system + "] Error for file: " + file, e);
			}
		}

	}

}
