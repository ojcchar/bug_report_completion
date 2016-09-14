package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ActionsMultiPMTest extends BaseTest {

	public ActionsMultiPMTest() {
		pm = new ActionsMultiPM();
	}
	

	@Test
	public void testPositives() throws Exception {

		String[] txts = { "Go to main menu | Navigate | Open Type\n"
				+ "Type BBB, then double click on the matching result.\n"
				+ "The class name BBB is highlighted in editor correctly.\n"
				+ "Enter a few (10) new lines above class BBB body.\n" + "Save the changes.\n"
				+ "Invoke again Open Type (Ctrl+Shift+T), type again BBB, then double click at the matched class BBB in the list of Open Type dialog." };
		TestUtils.testParagraphs(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { 
//				"Sent printed output to client and modified that",
//				"In Reader, click on a post to view full post.", "Open the url http://www.buyanorgan.net/test.xml",
				"Open attached file and scroll up an down the spreadsheet.",
//				"Resize the firefox window so it is very thin, the location widget shrinks correctly until it gets smaller than the URL icon." 
				};
		TestUtils.testParagraphs(txts, pm, 0);

	}

}
