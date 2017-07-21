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
				"Select column > Edit > Copy = spinning multicolor disk eventually leads to \"unresponsive\" in Activity Monitor.",
				"Running gmail - compose mail - attach file/browse - select a file and Mozilla closes",
				"Tap on change date range->select range option", "Long tap on note > send note > crash" };
		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "make[2]: Leaving directory `/usr/local/httpd-2.0-APACHE_2_0_BRANCH/support'",
				"Note that a kill -HUP will prompt again.", "return r->status;",
				// "\"Edit this Patient | Edit this Patient (Short Form)\"",
				"- use the `-Wl,--rpath -Wl,LIBDIR' linker flag" };
		TestUtils.testSentences(txts, pm, 0);
	}
}
