package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class LabeledListPMTest extends BaseTest {

	public LabeledListPMTest() {
		pm = new LabeledListPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"Steps to Reproduce:\n" + "1. At the site <http://www.nm-msp.org/>, click on 'Find a Course'.\n" + "\n"
						+ "2. You'll be presented with a chance to enter a ZIP and mileage radius: enter\n"
						+ "87110 and leave the default radius at '50'.\n" + "\n"
						+ "3. This takes you to a page of Albuquerque classes. On the left hand side of\n"
						+ "each class listing is a tiny magnifying glass icon.\n" + "\n"
						+ "4. If I click on this in Firefox, I am looped back to the same page. In IE or\n"
						+ "Safari, I am taken to a map and class description.",
						
						"Steps to reproduce:\n"+
								"1. Open LibreOffice from the LibreOffice.app icon. The start center opens.\n"+
								"2a. WITHOUT clicking on anything or defocusing the window, press Cmd+O to open a file.\n"+
								"2b. OR choose File -> Open from the menu.\n"+
								"3. A dialog saying \"LibreOffice 5.0 - Fatal Error\" and \"Given module is not a frame nor a window, controller or model.\" opens. When you close the dialog, LO closes."
		};

		TestUtils.testParagraphs(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = {
				"Steps to reproduce:\n" + "Tap on a media item in the list\n"
						+ "In the detail tap into the title field.\n" + "Edit the title text\n"
						+ "Tap the Next button on the keyboard.\n"
						+ "The title field looses focus and the keyboard remains visible but the caption field is not being edited. ",
				"**Steps to reproduce the issue:**\n" + " Execute `BUILDFLAGS=-race make test-unit`\n"
						+ " (Tested on 29dbcbad878483d9239d6a432c85620aced895c4)",
				"Steps to reproduce: Just editing any file in Calc and Writer", "Steps to reproduce: \n\n",
				"1 The user starts Eclipse on a workspace which already has a connection profile defined.  They have not set a password for the database.  The Data Source Explorer has not been activated, so the connection profiles have not been loaded.\n2 The user brings up the wizard to create a new JPA project, chooses the connection from the dropdown, and clicks connect.  This fails, because the password hasn't been set.\n3 They cancel the wizard, go to the DSE view and set the password, and connect to the database.\n4 They go back into the wizard to create a new JPA project, but the connection still fails, with an error indicating the password wasn't specified.",
				"- Typed in an identifier for an interface that didn't exist.\n- Quick Fix (Ctrl-1), accidentally chose Create Class\n- I'm pretty sure I hit Enter twice but then was able to select Cancel\n- Within the editor, I couldn't open the new class but I could no longer create it again because it already existed\n- Checking the filesystem, the Java file was there\n- Deleting the file fixed the problem",
				"1 The user starts Eclipse on a workspace which already has a connection profile defined.  They have not set a password for the database.  The Data Source Explorer has not been activated, so the connection profiles have not been loaded.\n2 The user brings up the wizard to create a new JPA project, chooses the connection from the dropdown, and clicks connect.  This fails, because the password hasn't been set.\n3 They cancel the wizard, go to the DSE view and set the password, and connect to the database.\n4 They go back into the wizard to create a new JPA project, but the connection still fails, with an error indicating the password wasn't specified."
						+ "Steps to reproduce:  \n\n",
				"Steps to reproduce  \n\n",
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
						+ "\"test\" and line are overlapping each other.\n" + "There is also a red triangle.",
				"",
				"Steps to reproduce:. 1 ....Not sure. 2 ..... 3 ....." 
		
		};

		TestUtils.testParagraphs(txts, pm, 0);
	}
}
