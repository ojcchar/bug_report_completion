package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class RequirePMTest extends BaseTest {

	public RequirePMTest() {
		pm = new RequirePM();
	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "I can clarify and/or provide more information if required.",
				"However_ I have noticed that if a user is not logged into facebook_ it will throw this error:  Fatal error: Uncaught Exception: 453: A session key is required for calling this method thrown in /home/howl/public_html/libs/facebook." };
		TestUtils.testSentences(txts, pm, 0);

	}
}
