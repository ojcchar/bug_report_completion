package seers.bugreppatterns.pattern.eb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.bugreppatterns.entity.Paragraph;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ExpBehaviorLiteralMultiSentencePMTest {

	ExpBehaviorLiteralMultiSentencePM pm = new ExpBehaviorLiteralMultiSentencePM();

	@Test
	public void testMatchParagraph() throws Exception {

		String[] txts = { "Expected, in both cases: Undo is available. Ctrl-Z undoes the action.",
				"Expected results:\r\n1- Calendar layout is mirrored to be \"RTL\".\r\n2- Calendar buttons behavior should be toggled (i.e pressing the left arrow next to the \"year\" should increase the displayed year).\r\n3- Year, Months, days are shown in Bidi languages (ex: Arabic in case of egyptian locale).",
				"Expected Results:  \r\nWant my default settings to remain. Shouldn't have to reset every time we want\r\nto print a page. Current default almost always results in an extra line on HP\r\nprinters.",
				"Expected Results:  \r\nIf a link cannot be resolved correct, an error should be generated.\r\nIf a link cannot be resolved in time, an error should be generated.\r\nRegardless of cause, the user should be able to click on any other link, select\r\na bookmard, or RELOAD the page successfully.  When Firebird is closed, there\r\nshould be no remaining zombie threads.",
				"Expected Results:  \r\nOpened the page that normally pops up in the tab.  If this cannot be done, it\r\nshould simply pop up as it normally would.",
				"Expected Results:  .\r\nOne of the following:.\r\n* Remove the old entry.\r\n* Create a non-version specific entry (e.g. \"Mozilla Firefox\" without version).\r\nso the entry gets overwritten automatically.\r\n* Automatically uninstall the old Mozilla Firefox version before installing the.\r\nnew one",
				"expected results:\r\n\r\n1) title should be \"https://bugzilla.mozilla.org/attachment.cgi?id=262682\"",
				"Expected Results:  \r\nThe text is pasted\r\n\r\n1. Ask permission to perform paste when the user is pasting first time and remember this decision\r\n\r\n2. Allow paste for trusted websites\r\n\r\nor\r\n\r\n3. At least implement a method to *check* whether right click pasting is allowed so that the application can hide the relevant menu item",
				"Expected Results:\r\n\r\nShould at least be able to place each side of the tab bar or in the nav bar. Having static items all around Firefox make it awkward for the user.",
				"EXPECTED RESULTS\r\nA checkbox should only be enabled when there is corresponding data\r\nto be cleared.  In this case I would expect all checkboxes to be\r\ndisabled since the corresponding data was just removed (in step 4)." };

		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];

			System.out.print("Testing (positive) " + i);
			List<Sentence> sentences = TextProcessor.processText(txt);

			Paragraph paragraph = new Paragraph("0");
			paragraph.setSentences(sentences);

			int m = pm.matchParagraph(paragraph);
			assertEquals(1, m);

			System.out.println(" PASSED");

		}
	}

}
