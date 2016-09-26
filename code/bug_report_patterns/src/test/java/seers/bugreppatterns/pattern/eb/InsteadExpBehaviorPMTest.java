package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class InsteadExpBehaviorPMTest extends BaseTest {

	public InsteadExpBehaviorPMTest() {
		pm = new InsteadExpBehaviorPM();
	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = {
				"If there is a duplicate, please close your issue and add a comment to the existing issue instead",
				"Have you considered using liblxc.so instead of parsing the output of lxc commands?",
				"npm won't run install after USER switch but works with RUN su instead",
				"They have been ignored, or old ones used instead." };
		TestUtils.testSentences(txts, pm, 0);

		@SuppressWarnings("unused")
		String[] trickyTxts = {
				"The same thing also happens when I make an API request directly via `curl(1)` instead of using the Docker CLI." };

	}

}
