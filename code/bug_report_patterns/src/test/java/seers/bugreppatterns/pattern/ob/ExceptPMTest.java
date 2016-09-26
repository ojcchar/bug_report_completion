package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ExceptPMTest extends BaseTest {
	public ExceptPMTest() {
		pm = new ExceptPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = { "This shouldn't be the case except for this special character.",
				"This shouldn't be the case except for this special character and except when the",
				"Except when this happens, this shouldn't be like this", };

		TestUtils.testSentences(negs, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = { "This is working well except for this special character.",
				"Except when this happens, this works", };

		TestUtils.testSentences(negs, pm, 1);
	}
}
