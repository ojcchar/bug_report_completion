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
		String[] sentences = {};

		TestUtils.testSentences(sentences, pm, 0);
	}
	
	@Test
	public void testPositive() throws Exception {
		String[] sentences = {"The class name BBB is highlighted in editor correctly."};

		TestUtils.testSentences(sentences, pm, 1);
	}
}
