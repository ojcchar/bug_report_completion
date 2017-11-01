package seers.bugrepclassifier;

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import seers.appcore.xml.XMLHelper;
import seers.bugrepclassifier.parsing.TextParser;
import seers.bugrepcompl.entity.BugReport;
import seers.bugrepcompl.entity.regularparse.ParsedBugReportDescription;

public class TextParserTest {

	@Test
	public void testParseText() throws Exception {
		TextParser parser = new TextParser();

		File noParsedFolder = new File("test_data/text_parser/no_parsed_bugs");
		File[] noParsedFiles = noParsedFolder.listFiles();

		for (File file : noParsedFiles) {
			
			BugReport bug = XMLHelper.readXML(BugReport.class, file);
			ParsedBugReportDescription parsedDesc = parser.parseText(bug.getDescription());

			File parsedFile = new File("test_data/text_parser/parsed_bugs/" + file.getName() + ".parse");
			seers.bugrepcompl.entity.regularparse.ParsedBugReport parsedBug = XMLHelper
					.readXML(seers.bugrepcompl.entity.regularparse.ParsedBugReport.class, parsedFile);

			System.out.print("Testing " + bug.getId() + "... ");

			ParsedBugReportDescription expectedDescr = parsedBug.getDescription();
			boolean equals = expectedDescr.equals(parsedDesc);
			
			if (equals) {
				System.out.println("[PASSED]");
			} else {
				System.err.println("[FAILED]");
				assertEquals(expectedDescr, parsedDesc);
			}
		}

	}

}
