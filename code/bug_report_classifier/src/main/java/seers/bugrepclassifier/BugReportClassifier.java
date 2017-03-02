package seers.bugrepclassifier;

import java.io.File;
import java.util.List;

import seers.bugreppatterns.main.prediction.MainHRClassifier;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.predictor.PredictionOutput;

public abstract class BugReportClassifier {

	protected List<PatternMatcher> patterns;

	public BugReportClassifier(File filePatterns) throws Exception {

		// load patterns

		patterns = MainHRClassifier.loadPatterns(filePatterns);

	}

	public abstract PredictionOutput detectInformation(String bugId, String bugDescription) throws Exception;

}
