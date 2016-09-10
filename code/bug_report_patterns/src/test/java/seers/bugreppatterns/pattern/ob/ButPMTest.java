package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ButPMTest extends BaseTest {

	public ButPMTest() {
		pm = new ButPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {

		};
		TestUtils.testSentences(sentences, pm, 1);
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = { "But I can't do this.", };
		TestUtils.testSentences(sentences, pm, 0);
	}
}
