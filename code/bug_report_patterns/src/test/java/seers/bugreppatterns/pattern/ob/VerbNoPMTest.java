package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class VerbNoPMTest extends BaseTest {

	public VerbNoPMTest() {
		pm = new VerbNoPM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] n = {};

		TestUtils.testSentences(n, pm, 0);
	}

	@Test
	public void testPositives() throws Exception {
		String[] n = { "it returns with no error", "it returns with no name",
				"When we runs the script from an existing dos window the command prompt returns with no output.",
				"Double-clicking the background in TabCandy currently does nothing.",
				"As you can see, there's no language set and spellcheck did nothing.",
				"Media: Tapping refresh with no internet connection does nothing." };

		TestUtils.testSentences(n, pm, 1);
	}

}
