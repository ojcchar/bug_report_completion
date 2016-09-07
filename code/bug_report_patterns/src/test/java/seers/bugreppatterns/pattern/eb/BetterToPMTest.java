package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class BetterToPMTest extends BaseTest {

	public BetterToPMTest() {
		pm = new BetterToPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {};
		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {"Something is definitely broken, and even if this doesn't look dangerous, it's better to be checked and fixed."};
		TestUtils.testSentences(txts, pm, 0);
	}

}
