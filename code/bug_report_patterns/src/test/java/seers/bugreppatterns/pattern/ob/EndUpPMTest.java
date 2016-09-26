package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class EndUpPMTest extends BaseTest {

	public EndUpPMTest() {
		pm = new EndUpPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"Some of the time it does not clean up and remove the files, so the directory ends up being full of these files (mostly of 0 size!).",
				"Publishing a post with a video ends up in a Android notification stating: &gt; Media Error &gt; Get the VideoPress upgrade to upload video!",
				"Actual Results:  Bookmark ended up in bookmark folder, but only after the error message",
				"This ends up in an exception: immutable natural identifier was modified..." };
		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "I d be fine with it if they at least ended up on the right page after sending the invites.",
				"It would also be nice if when one included the \"--enable-static\" option one really ended up with a *STATIC* firefox (which would load faster than those that may require searching through many directories many times for shared object libraries).", };
		TestUtils.testSentences(txts, pm, 0);
	}

}
