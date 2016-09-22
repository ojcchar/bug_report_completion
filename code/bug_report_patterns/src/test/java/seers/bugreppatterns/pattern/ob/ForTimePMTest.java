package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ForTimePMTest extends BaseTest {

	public ForTimePMTest() {
		pm = new ForTimePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = {"20 mins for now", "10 mins from now"};

		TestUtils.testSentences(negs, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = {"I've been waiting for 3 days",
				"This application has been Pending since February 18th"};

		TestUtils.testSentences(negs, pm, 1);
	}
}
