package seers.bugrepclassifier.patternbased;

import java.io.File;
import java.util.LinkedHashMap;

import seers.bugrepclassifier.BugReportClassifier;
import seers.bugrepclassifier.parsing.TextParser;
import seers.bugrepcompl.entity.regularparse.ParsedBugReport;
import seers.bugrepcompl.entity.regularparse.ParsedBugReportDescription;
import seers.bugreppatterns.entity.Document;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.AnyMatchPredictor;
import seers.bugreppatterns.pattern.predictor.PredictionOutput;
import seers.bugreppatterns.utils.ParsingUtils;

public class PatternBugReportClassifier extends BugReportClassifier {
	private TextParser txtParser;

	public PatternBugReportClassifier(File filePatterns) throws Exception {
		super(filePatterns);
		txtParser = new TextParser();
	}

	@Override
	public PredictionOutput detectInformation(String bugId,  String bugDescription) throws Exception {

		// parse the description

		ParsedBugReportDescription parsedDesc = txtParser.parseText(bugDescription);

		ParsedBugReport bugRep = new ParsedBugReport();
		bugRep.setId(bugId);
		bugRep.setDescription(parsedDesc);

		Document bugReport = ParsingUtils.parseDocument(bugRep);
		LinkedHashMap<PatternMatcher, Integer> patternMatches = new LinkedHashMap<>();

		for (PatternMatcher patternMatcher : patterns) {
			int numMatches = patternMatcher.matchDocument(bugReport);
			if (numMatches > 0) {
				patternMatches.put(patternMatcher, numMatches);
			}
		}

		AnyMatchPredictor predictor = new AnyMatchPredictor(patterns, null, null, false);

		return predictor.predictLabels(bugRep.getId(), "0", patternMatches);

	}

}
