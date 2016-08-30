package seers.bugreppatterns.pattern.sr;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ActionsSeparatorPMTest extends BaseTest {

	public ActionsSeparatorPMTest() {
		pm = new ActionsSeparatorPM();
	}

	// @Test
	public void testPositives() throws Exception {
		String[] txts = {
				// "How to reproduce:\nSelect menu Data | Pivot Table | Create
				// and in the title there is name \"Data Pilot\".",
				"Select column > Edit > Copy = spinning multicolor disk eventually leads to \"unresponsive\" in Activity Monitor.",
				"If you go into My Site -> Comments -> Single Comment and reply to a comment and go back to the Comments page, the new comment is not added to the list." };
		TestUtils.testSentences(txts, pm, 1);
	}
}
