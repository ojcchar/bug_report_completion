package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class VeryAdjectivePMTest extends BaseTest {

	public VeryAdjectivePMTest() {
		pm = new VeryAdjectivePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = { "It should be  Publikování na mé Zdi  - i.e. the very last character is  i _ not  í .", };

		TestUtils.testSentences(negs, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = {};

		TestUtils.testSentences(negs, pm, 1);
	}
}
