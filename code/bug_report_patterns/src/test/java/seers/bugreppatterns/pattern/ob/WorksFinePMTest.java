package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class WorksFinePMTest extends BaseTest {
	public WorksFinePMTest() {
		pm = new WorksFinePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = {"It doesn't work correctly", "it hasn't been working correctly", "it hasn't worked"};

		TestUtils.testSentences(negs, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = {"it works", "it has been working"};

		TestUtils.testSentences(negs, pm, 1);
	}

}
