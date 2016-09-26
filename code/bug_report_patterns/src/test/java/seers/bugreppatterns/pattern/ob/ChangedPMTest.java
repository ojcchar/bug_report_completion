package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ChangedPMTest extends BaseTest {

	public ChangedPMTest() {
		pm = new ChangedPM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "I changed something", "I have changed this",
				"I changed the Readonly properties of a file to Yes.",
				"When a \"static final\" variable changed to \"static\" two problems are reported.",
				"After step4, the encrypted code has been changed, so that the report has a dirty mark and after step6, a Exception thrown out and it will block all the UI operation."};
		TestUtils.testSentences(txts, pm, 0);
	}

}
