package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ComparativeAdjectivePMTest extends BaseTest {
	public ComparativeAdjectivePMTest() {
		pm = new ComparativeAdjectivePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = {};

		TestUtils.testSentences(negs, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = {};

		TestUtils.testSentences(negs, pm, 1);
	}

}
