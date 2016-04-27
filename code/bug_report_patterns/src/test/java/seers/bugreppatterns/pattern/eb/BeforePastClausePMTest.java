package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class BeforePastClausePMTest extends BaseTest {

	public BeforePastClausePMTest() {
		pm = new BeforePastClausePM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = {
				"Before that_ I was able to display the login and like buttons on the test page I set up for Facebook integration on our news site."

		};
		TestUtils.testSentences(txts, pm, 1);

	}
}
