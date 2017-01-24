package seers.bugrepclassifier;

import java.io.File;

import seers.bugreppatterns.pattern.predictor.PredictionOutput;

public class MainExample {

	public static void main(String[] args) throws Exception {

		File filePatterns = new File("../bug_report_patterns/patterns.csv");
		BugReportClassifier classifier = new PatternBugReportClassifier(filePatterns);

		String[] bugDescriptions = { "This is not working", "This should be like this",
				"when I run this command, there is a crash" };

		for (String bugDescription : bugDescriptions) {
			PredictionOutput output = classifier.detectInformation("dummyId", bugDescription);
			System.out.println(bugDescription + " --> " + output);
		}

	}

}
