package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ErrorTermSubjectPMTest extends BaseTest {

	public ErrorTermSubjectPMTest() {
		pm = new ErrorTermSubjectPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"Unavailability of a method which tells if a user has given an extended permission to an application",
				"the issues are" };

		TestUtils.testSentences(sentences, pm, 1);
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = { "There are still issues with this approach ." };

		TestUtils.testSentences(sentences, pm, 0);
	}
}
