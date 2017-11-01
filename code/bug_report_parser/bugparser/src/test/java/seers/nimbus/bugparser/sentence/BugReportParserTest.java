package seers.nimbus.bugparser.sentence;

import java.io.File;

import org.junit.Test;

import seers.appcore.xml.XMLHelper;
import seers.bugrepcompl.entity.BugReport;
import seers.nimbus.bugparser.sentence.BugReportSentenceParser;

public class BugReportParserTest {

	private String inFile = "test_data" + File.separator + "sentence_splitting" + File.separator + "steps2repro.xml";

	@Test
	public void testParseBugReport() throws Exception {

		BugReport bugRep = XMLHelper.readXML(BugReport.class, inFile);

		seers.bugrepcompl.entity.regularparse.ParsedBugReport parsedBugReport = BugReportSentenceParser
				.parseBugReport(bugRep);
		System.out.println(parsedBugReport);
	}

	@Test
	public void testParseBugReportForCoding() throws Exception {

		BugReport bugRep = XMLHelper.readXML(BugReport.class, inFile);

		seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReport parsedBugReport = BugReportSentenceParser
				.parseBugReportForPatternCoding(bugRep);
		System.out.println(parsedBugReport);
	}

	@Test
	public void testParseBugReport2() throws Exception {

		String[] files = { "AnagramSolver_2.xml", "ATimeTracker_24.xml" };

		for (String file : files) {
			String inFile2 = "test_data" + File.separator + "sentence_splitting" + File.separator + file;
			BugReport bugRep = XMLHelper.readXML(BugReport.class, inFile2);

			seers.bugrepcompl.entity.patterncoding.PatternLabeledBugReport parsedBugReport = BugReportSentenceParser
					.parseBugReportForPatternCoding(bugRep);
			System.out.println(parsedBugReport);
			System.out.println("-------------");
		}

	}

}
