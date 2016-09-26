package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ActionsSeparatorPMTest extends BaseTest {

	public ActionsSeparatorPMTest() {
		pm = new ActionsSeparatorPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"How to reproduce:\nSelect menu Data | Pivot Table | Create and in the title there is name \"Data Pilot\".",
				"Select column > Edit > Copy = spinning multicolor disk eventually leads to \"unresponsive\" in Activity Monitor.", };
		TestUtils.testSentences(txts, pm, 1);
	}
}
