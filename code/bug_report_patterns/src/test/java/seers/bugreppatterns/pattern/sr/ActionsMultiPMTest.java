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
		
//				"Paste &#x633;&#x62A;&#x200C;&#x62A;&#x631; string from Notepad into Writer. Immediate crash.",
//				"Add an item to the library outline.\n" + "Choose the edit menu.\n"
//						+ "Actual: Undo is not available. Ctrl-Z does not undo the action.",
//				"Go to http://www.footlocker.com/product/model:148756/sku:14674001/?supercat=home/?cm=HOME\n"
//						+ "Click FB Share button\n" + "Post comment and share\n"
//						+ "Result: In IE8_ after posting a comment the window doesn t automatically close as indicated.",
//				"Resize the firefox window so it is very thin. The location widget shrinks correctly until it gets smaller than the URL icon.",
//				"touch foo\n" + "touch goo\n" + "[visit /yoo?hoo=%3f in a browser]\n"
//						+ "[note the broken links to \"foo%3fhoo=%253f\" and \"goo%3fhoo=%253f\"]",
						"The report works fine from the preview and from the HTML view. Select to run the report from the Web Viewer and then select the print report icon. In the dialog that comes up, select the PDF print format option and select OK. This results in the ArrayIndexOutOfBoundsException. Attaching a sample report to illustrate the problem. This report design works fine in BIRT 2.1.2 and does not work in 2.3 or 2.3.1."
						
		};
		TestUtils.testParagraphs(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = {

				"Sent printed output to client and modified that", "In Reader, click on a post to view full post.",
				"Open the  url http://www.buyanorgan.net/test.xml",
				"Open attached file and scroll up an down the spreadsheet.",
				"The end user should be-able to not allow certain permissions but still continue to allow others.. ",
				"I just want to tell you I have this too.. I experience this bug as VERY annoying.. I changed for that my preferred browser to GChrome, I am sorry.. My 3.5.2 is (when Java is on) often slow in showing the typed letters.. Maybe there is a link with the number of add-ons I have , now about 36.. Please do add back also the closing x in the first tab, because my setting is \\show always tabbar\").. It was still there in FF 3.5.. Maybe the general closing of FF is related to this removal.. Maybe also related to \"This is annoying\"-dialogbox after restart.. Please revert those \"bad improvements\". ",
				"Hey_ 3 days ago I signed up at http://www.239ipadtesters.info/ as a tester and today I got my iPad. All you need to do is to tell them your opinion about iPad and you can keep it forever. You should hurry since i highly doubt this is gonna last forever."

		};
		TestUtils.testParagraphs(txts, pm, 0);

	}

}
