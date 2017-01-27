package seers.bugreppatterns.processor;

import java.io.File;
import java.util.LinkedHashMap;

import seers.appcore.threads.processor.ThreadParameters;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.regularparse.BugReport;
import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.PredictionOutput;
import seers.bugreppatterns.utils.ParsingUtils;

public class BugReportProcessor extends TextInstanceProcessor {

	public BugReportProcessor(ThreadParameters params) {
		super(params);
	}

	@Override
	public void executeJob() throws Exception {

		// System.out.println("Processing " + files.size() + " files");
		for (File file : files) {

			try {

				BugReport bugRep = XMLHelper.readXML(BugReport.class, file);
				Document bugReport = ParsingUtils.parseDocument(system, bugRep);
				LinkedHashMap<PatternMatcher, Integer> patternMatches = new LinkedHashMap<>();

				// int numOfSentences = bugReport.getNumOfSentences();
				// int numOfTokens = bugReport.getNumOfTokensInSentences();

				for (PatternMatcher patternMatcher : patterns) {
					int numMatches = patternMatcher.matchDocument(bugReport);
					if (numMatches > 0) {
						patternMatches.put(patternMatcher, numMatches);
					}
				}

				PredictionOutput predictionOutput = predictor.predictLabels(bugRep.getId(), "0", patternMatches);
				
				writePreFeatures(bugRep.getId(), "0", predictionOutput.getFeatures());
				writePrediction(bugRep.getId(), "0", predictionOutput.getLabels());

			} catch (Exception e) {
				LOGGER.error("[" + system + "] Error for file: " + file + ", " + e.getClass().getSimpleName() + ": "
						+ e.getMessage(), e);
			}
		}
	}

}
