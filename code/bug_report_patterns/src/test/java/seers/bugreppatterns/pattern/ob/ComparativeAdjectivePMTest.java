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
		String[] negs = { " Long tap on note > send note > crash"

		};

		TestUtils.testSentences(negs, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = {
				"Resize the firefox window so it is very thin, the location widget shrinks correctly until it gets smaller than the URL icon." };

		TestUtils.testSentences(negs, pm, 1);
	}

}
