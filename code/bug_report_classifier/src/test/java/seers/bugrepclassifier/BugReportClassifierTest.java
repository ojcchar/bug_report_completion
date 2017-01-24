package seers.bugrepclassifier;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import net.quux00.simplecsv.CsvParser;
import net.quux00.simplecsv.CsvParserBuilder;
import net.quux00.simplecsv.CsvReader;
import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.BugReport;
import seers.bugrepcompl.entity.Labels;
import seers.bugreppatterns.pattern.predictor.PredictionOutput;
import seers.bugreppatterns.processor.PatternFeature;

public class BugReportClassifierTest {

	@Test
	public void testDetectInformation() throws Exception {

		File filePatterns = new File("../bug_report_patterns/patterns.csv");
		BugReportClassifier classifier = new PatternBugReportClassifier(filePatterns);

		File noParsedFolder = new File("test_data/text_parser/no_parsed_bugs");
		File[] noParsedFiles = noParsedFolder.listFiles();

		for (File file : noParsedFiles) {

			BugReport bug = XMLHelper.readXML(BugReport.class, file);

			PredictionOutput output = classifier.detectInformation(bug.getId(), bug.getDescription());

			System.out.print("Testing " + bug.getId() + "... ");
			PredictionOutput expectedOutput = readExpectedOutput(file);

			boolean equals = expectedOutput.equals(output);

			if (equals) {
				System.out.println("[PASSED]");
			} else {
				System.err.println("[FAILED]");
				assertEquals(expectedOutput, output);
			}
		}

	}

	private PredictionOutput readExpectedOutput(File file) throws Exception {
		File outFile = new File("test_data/classifier/labels_patterns/" + file.getName() + ".csv");

		CsvParser csvParser = new CsvParserBuilder().multiLine(true).separator(';').build();
		try (CsvReader csvReader = new CsvReader(new InputStreamReader(new FileInputStream(outFile), "Cp1252"),
				csvParser)) {

			// -------------------------------------------------

			List<String> line = csvReader.readNext();

			String isOB = line.get(0);
			String isEB = line.get(1);
			String isSR = line.get(2);

			// -------------------------------------------------

			Labels labels = new Labels(isOB, isEB, isSR);

			List<PatternFeature> features = new ArrayList<>();
			addFeatures(features, line.get(3));

			// -------------------------------------------------

			PredictionOutput output = new PredictionOutput(labels, features);
			return output;
		}
	}

	private void addFeatures(List<PatternFeature> features, String featuresString) {
		String[] split = featuresString.split(",");
		for (String feat : split) {
			String trim = feat.trim();
			if (!trim.isEmpty()) {
				String[] split2 = trim.split(":");
				features.add(new PatternFeature(null, split2[0].trim(), Integer.valueOf(split2[1].trim())));
			}
		}
	}

}
