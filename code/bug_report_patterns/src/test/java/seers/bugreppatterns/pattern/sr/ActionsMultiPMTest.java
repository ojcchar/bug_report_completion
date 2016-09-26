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

		String[] txts = {
				"Paste &#x633;&#x62A;&#x200C;&#x62A;&#x631; string from Notepad into Writer. Immediate crash.",
				"Add an item to the library outline.\n" + "Choose the edit menu.\n"
						+ "Actual: Undo is not available. Ctrl-Z does not undo the action.",
				"Go to http://www.footlocker.com/product/model:148756/sku:14674001/?supercat=home/?cm=HOME\n"
						+ "Click FB Share button\n" + "Post comment and share\n"
						+ "Result: In IE8_ after posting a comment the window doesn t automatically close as indicated.",
				"Resize the firefox window so it is very thin, the location widget shrinks correctly until it gets smaller than the URL icon." };
		TestUtils.testParagraphs(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "Sent printed output to client and modified that",
				"In Reader, click on a post to view full post.", "Open the  url http://www.buyanorgan.net/test.xml",
				"Open attached file and scroll up an down the spreadsheet.", };
		TestUtils.testParagraphs(txts, pm, 0);

	}

}
