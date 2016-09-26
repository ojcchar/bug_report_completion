package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class OnlyPMTest extends BaseTest {

	public OnlyPMTest() {
		pm = new OnlyPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = { "Unfortunately I cannot export only the diff with the database :( .",

		};
		TestUtils.testSentences(sentences, pm, 1);
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = { "It would be nice if I would have to enter all my installed JREs only once.",
				"When I opened the editor the second time, only the version were displayed on the editor.",
				"Can we get a safe way to display information only to friends of a user within an application tab?",
				"Need a safe way to display items to only friends of the user on application tab",
				"Moving a bookmark inside the bookmark menu make folders (possibly only those with subfolder) inaccessible.",
				"Expected Results:  only the first 2 tabs should be reloaded.",
				"There is an important discrepancy and I think only the javadoc makes sense.",
				"Read-only and immutable entities that are in persistent state when deleted are updated before being deleted.",
				"First, if I edit the sentence at C:43, \"SENAST KOLLAD 23 NOVEMBER\" to \"SENAST KOLLAD 26 NOVEMBER\" by only changing the 3 to a 6, then save the document, close it, and reopen it, all the comments are gone.", };
		TestUtils.testSentences(sentences, pm, 0);
		@SuppressWarnings("unused")
		String[] failingSentences = {
				// TODO: the next one shouldn't pass, but the last part should
				// be "readonly" or "read-only"
				"Pivot filter not working on read only file",

		};
	}
}
