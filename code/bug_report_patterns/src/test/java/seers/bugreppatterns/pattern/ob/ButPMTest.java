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

				"I can, however, remove the mistake identifier via the long form by voiding it.", };
		TestUtils.testSentences(sentences, pm, 1);
	}
}
