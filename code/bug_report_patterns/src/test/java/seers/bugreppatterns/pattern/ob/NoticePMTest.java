package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NoticePMTest extends BaseTest {
	public NoticePMTest() {
		pm = new NoticePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = { "I should notice that", "I noticed" };

		TestUtils.testSentences(negs, pm, 0);
	}
}
