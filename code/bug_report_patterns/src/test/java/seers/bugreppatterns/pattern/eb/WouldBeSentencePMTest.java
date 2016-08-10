package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class WouldBeSentencePMTest extends BaseTest {

	public WouldBeSentencePMTest() {
		pm = new WouldBeSentencePM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = { "The error message would be more helpful if it said:" };
		TestUtils.testSentences(txts, pm, 1);

	}

}
