package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NeedsToPMTest extends BaseTest {

	public NeedsToPMTest() {
		pm = new NeedsToPM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "	I have discovered a problem on demo.openmrs.org that may need to be investigated:", 
				"I need to restart or stop/start it for my friends to continue to download the"};
		TestUtils.testSentences(txts, pm, 0);
	}

}
