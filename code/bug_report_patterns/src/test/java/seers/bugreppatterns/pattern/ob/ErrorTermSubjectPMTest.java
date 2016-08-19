package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ErrorTermSubjectPMTest extends BaseTest {

	public ErrorTermSubjectPMTest() {
		pm = new ErrorTermSubjectPM();
	}
	

	@Test
	public void testNegative() throws Exception {
		String[] negatives = { "There are still issues with this approach ." };

		TestUtils.testSentences(negatives, pm, 0);
	}
}
