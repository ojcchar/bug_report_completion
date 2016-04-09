package seers.bugreppatterns.pattern.eb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ExpBehaviorLiteralSentencePMTest {

	ExpBehaviorLiteralSentencePM pm = new ExpBehaviorLiteralSentencePM();

	public void testMatchSentence() throws Exception {

		String txt = "Expected Results:  \r\n" + "The location entered manually in the location bar should load.";

		List<Sentence> sentences = TextProcessor.processText(txt);
		int m = pm.matchSentence(sentences.get(0));
		assertEquals(1, m);

		// ------------------

		txt = "Expected behavior\r\n" + "-----------------\r\n"
				+ "Request is dropped and a 408 status code is returned.";

		sentences = TextProcessor.processText(txt);
		m = pm.matchSentence(sentences.get(0));
		assertEquals(1, m);

	}

	@Test
	public void testMatches() throws Exception {
		String[] txts = { "expected result:\r\nNo NullPointerException thrown out",
				"Expected result:\r\nReport can show in Web Viewer/HTML/PDF",
				"Expected result:\r\nIt works without any problem", "Expected result:\r\nNo exception.",
				"Expected result:\r\nThe reopen will not change the password in Property Binding",
				"Expected result:\r\nThe poke has successfully been removed",
				"Expected Results:  \r\nThe location entered manually in the location bar should load.",
				"Expected Results:  \r\nTry to resolve the word, and if not then an I'm feeling lucky search of the word.",
				"Expected Results:  \r\nIt should have rendered it identically each time - one way or the other.",
				"Expected Results:  \r\nSelected the previous tab",
				"Expected Results:  \r\nThey should look different for clarity.",
				"Expected Results:  \r\nthe obvious, position the cursor",
				"Expected Results:  \r\nopen the mail client specified in mozex",
				"Expected Results:  \r\nbar should have been on top of frame as in ie",
				"Expected: \r\nThe  profile pictures  album is renamed",
				"Expected Results:  \r\nShow down arrow at all times.",
				"Expected Results:  \r\nI see nothing since I've already cleared all private data.",
				"Expected results\r\n- text is selected, no spew to console",
				"Expected Results:  \r\nOpening by any shortcut should only open set homepage",
				"Expected Results:  \r\nThe Download Actions dialog contains many items, including the newly added one, and functioning buttons.",
				"Expected Results:  \r\nThe feed should load as Live Bookmarks.",
				"Expected Results:  \r\nNot kept the marquee.",
				"Expected result:\r\nTree table should be accessible with keyboard navigation.",
				"Expected Results:  \r\nFolders should still be accesssible without restarting firefox.",
				"Expected Results:  \r\nNo error should appear.",
				"Expected Results:  \r\nMake the symbol and not show a tab.", "Expected Results:  \r\nwork normally",
				"Expected result:\r\nI expected it to read \"www2.example.org:443\"\r\nOr I expected a critical error during start time of Apache because the\r\nconfiguration file is arguably inconsistent (see below)",
				"Expected behavior\r\n-----------------\r\nRequest is dropped and a 408 status code is returned.",
				"Expected Results:  \r\nhttp://news.bbc.co.uk/ would be loaded",
				"Expected Results:  \r\nA focus indicator in blue around the button in pressed state.",
				"Expected Results:  \r\nAsked for the information for the new site.",
				"Expected Results:  \r\nThe numbers in the absolute div should show up 300px down on the page, which would be inside the container div.",
				"Expected Results:  \r\nStart of firmware uploading process.",
				"Expected Results:  \r\nBoth windows should scale separately.",
				"Expected Results:  \r\nI expect to use the browser to look at more pdfs and maybe other websites also.",
				"Expected Results:  \r\nAs in previous versions of firefox the installed binary and shared libraries should contain all debug symbols.",
				"Expected Results:  \r\nIt would have installed the missing plugin, since it prompted for a legitimate installation...",
				"Expected Results:  \r\nPop up a message box to prompt the user that error occur.",
				"Expected Results:  \r\nbrowser should open.",
				"Expected Results:  \r\nThe Flash event MouseEvent.MOUSE_OUT shoudn't be fired on a click.",
				"Expected Results:  \r\ndo not close... stay opened and showing the pages",
				"Expected: the browser goes back a page without graphic defect",
				"Expected Results:\r\n \"Edit\" menuitem should be enabled if one of cut/copy/paste is available.",
				"Expected Results:  \r\nThe Bookmark All Tabs option SHOULD be there.  ",
				"Expected Results:  \r\nthis has happened twice", "Expected Results:  \r\nThe post should not load.",
				"Expected Results:  \r\nonly the first 2 tabs should be reloaded.",
				"Expecting Results:  \r\nonly the first 2 tabs should be reloaded." };

		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];

			System.out.print("Testing (positive) " + i);
			List<Sentence> sentences = TextProcessor.processText(txt);
			int m = pm.matchSentence(sentences.get(0));
			assertEquals(1, m);

			System.out.println(" PASSED");

		}
	}

	@Test
	public void testNotMatches() throws Exception {
		String[] txts = { "expected result:\r\n", "Expectation result:\r\nIt works without any problem", };

		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];

			System.out.print("Testing (negative) " + i);
			List<Sentence> sentences = TextProcessor.processText(txt);
			int m = pm.matchSentence(sentences.get(0));
			assertEquals(0, m);

			System.out.println(" PASSED");

		}
	}

}
