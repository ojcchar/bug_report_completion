package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class PassiveVoicePMTest extends BaseTest {
	public PassiveVoicePMTest() {
		pm = new PassiveVoicePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = {};

		TestUtils.testSentences(negs, pm, 0);
	}
}
