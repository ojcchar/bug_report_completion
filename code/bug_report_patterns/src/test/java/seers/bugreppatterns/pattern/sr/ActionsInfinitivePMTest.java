package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/12/16.
 */
public class ActionsInfinitivePMTest extends BaseTest {
	{
		pm = new ActionsInfinitivePM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] paragraphs = {
				
				"\"step1: open liber office and select liber draw\n" + "step2: Draw rectangle image\n"
						+ "step3: select the rectangular image and go to modify meanu\n" + "step4: click convert\n"
						+ "step5: click convert to polygon\n" + "step6: rectangle should get converted to polygon\"",
				"- creating new table\n" + "- fill Cell \"A1\" with i.e. \"Text1\"\n"
						+ "- autofill 10 lines down (Text1, Text2, Text3 ...)\n"
						+ "- insert comment to one cell, i.e. A5 (\"Text5\")\n" + "- show comment\n"
						+ "- insert comment to another cell, i.e. A7, and also show comment\n"
						+ "- hide line 7 (comment remains visible)\n" + "- copy whole table with context menu\n"
						+ "- now hide line 5 in new created table\n" + "- all comments double (see atachement)",
				"1. inside a method type \"new Runnable(\"\n" + "2. press Ctrl+SPACE\n"
						+ "==> no method comment for run()",
				"1:New a report,\n" + "2:Add a chart in the body.\n"
						+ "3:Select the chart, and switch to the script editor.\n"
						+ "4:Type \"abc\" , then save the report design.\n"
						+ "5:Watch the outline view, the script node of the chart show.\n"
						+ "6:Delete the \"abc\", then save the report design.",
				"1. check out a bundle from CVS, e.g. org.eclipse.core.filebuffers\n"
						+ "2. File > Export... > Deployable plug-ins and fragments\n" + "==> NPE:",
				"1 New Text Document.\n" + "2 Insert -> Footer -> Default Style.\n"
						+ "3 Table -> Insert Table -> 1x1.\n" + "4 Place cursor after table.\n"
						+ "5 Type 3 \"_\" and return (if Apply Border set in AutoCorrect Options).\n"
						+ "6 Place cursor in paragraph above line.\n" + "7 Type \"test\".\n"
						+ "8 Save, close, and reopen document.\n" + "Alternate:\n" + "5 Type \"test\".\n"
						+ "6 Format -> Paragraph -> Borders -> add bottom line in User-defined box.\n"
						+ "7 Save, close, and reopen document.\n" + "Result:\n"
						+ "\"test\" and line are overlapping each other.\n" + "There is also a red triangle."
						
		};
		TestUtils.testParagraphs(paragraphs, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] paragraphs = { "creating new table\n" + "fill Cell \"A1\" with i.e. \"Text1\"\n"
				+ "autofill 10 lines down (Text1, Text2, Text3 ...)\n"
				+ "insert comment to one cell, i.e. A5 (\"Text5\")\n" + "show comment\n"
				+ "insert comment to another cell, i.e. A7, and also show comment\n"
				+ "hide line 7 (comment remains visible)\n" + "copy whole table with context menu\n"
				+ "now hide line 5 in new created table\n" + "all comments double (see atachement)",
				"Steps to Reproduce:. 1 see beneath. 2. 3. ","#### API Request with `docker commit`. "};
		TestUtils.testParagraphs(paragraphs, pm, 0);
	}
}
