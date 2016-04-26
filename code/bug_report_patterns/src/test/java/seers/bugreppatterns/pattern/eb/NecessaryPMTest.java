package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NecessaryPMTest extends BaseTest {

	public NecessaryPMTest() {
		pm = new NecessaryPM();
	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = {
				"Certainly, this could be worked around with subrequests, however I would\nprefer not to have to deal with the overhead of a subreq on every transaction\n(which is what would be necessary in _my_ particular case, others may have\nbetter solutions).",
				"The following are a list of changes, some of which may be beyond\nthe scope of what was necessary and violate various development API\nintegrity rules." };
		TestUtils.testSentences(txts, pm, 0);

	}
}
