package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class WhyPMTest extends BaseTest {

	public WhyPMTest() {
		pm = new WhyPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {};
		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {};
		TestUtils.testSentences(txts, pm, 0);
	}

}
