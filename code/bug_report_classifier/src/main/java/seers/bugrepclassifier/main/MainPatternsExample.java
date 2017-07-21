package seers.bugrepclassifier.main;

import java.io.File;

import seers.bugrepclassifier.BugReportClassifier;
import seers.bugrepclassifier.patternbased.PatternBugReportClassifier;
import seers.bugreppatterns.pattern.predictor.PredictionOutput;

public class MainPatternsExample {

	public static void main(String[] args) throws Exception {

		File filePatterns = new File("../bug_report_patterns/patterns.csv");
		BugReportClassifier classifier = new PatternBugReportClassifier(filePatterns);

		String[] bugDescriptions = { "This is not working", "This should be like this",
				"when I run this command, there is a crash", "Force Close when enter word with apostrophe",
				"During multiple input of identical values e.g. 10 km (by day) and 10 litres some calculations fail.",
				"Tap on change date range->select range option", " Long tap on note > send note > crash" };

		for (String bugDescription : bugDescriptions) {
			PredictionOutput output = classifier.detectInformation("dummyId", bugDescription);
			System.out.println(bugDescription + " --> " + output);
		}

	}

}
