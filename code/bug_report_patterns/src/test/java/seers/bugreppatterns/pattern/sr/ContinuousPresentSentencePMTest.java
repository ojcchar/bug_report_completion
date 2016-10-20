package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/20/16.
 */
public class ContinuousPresentSentencePMTest extends BaseTest {
	{
		pm = new ContinuousPresentSentencePM();
	}
	@Test
	public void testPositives() throws Exception {
		String[] txts = {

				"I'm running this in Arch Linux, and the Docker version for this was:"

		};
		TestUtils.testSentences(txts, pm, 1);
	}

	
	@Test
	public void testNegatives() throws Exception {
		String[] txts = {

				"so we using mod_url module.",
				"unloaded as much of our code as possible). Additionally, we are seeing some",

		};
		TestUtils.testSentences(txts, pm, 0);
	}
}