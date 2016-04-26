package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class MakeSensePMTest extends BaseTest {

	public MakeSensePMTest() {
		pm = new MakeSensePM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = { "I'm thinking that this is a bug as it makes much more sense to call post_config",
				"Maybe it would make sense to kill the process group instead (using the cgi pgid)" };
		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = {
				"When using the Add Import... editor context menu item inner classes are offered as potential\nchoices even though they don't make sense in context.",
				"Another point is that it makes no sense to possibly *invite* the client\nfor sending the body just to discard it." };
		TestUtils.testSentences(txts, pm, 0);

	}
}
