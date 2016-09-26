package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NoNounPMTest extends BaseTest {
	public NoNounPMTest() {
		pm = new NoNounPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {};

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = { "no \"tag\" field for the image" };

		TestUtils.testSentences(sentences, pm, 1);
	}
}
