package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ExpBehaviorLiteralSentencePMTest extends BaseTest {

	public ExpBehaviorLiteralSentencePMTest() {
		pm = new ExpBehaviorLiteralSentencePM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"Expected Results:  \nshortened the box to fit within the viewable screen, or opened it in the other\ndirection (up).",
				"Expected: Hit the space where the version inof is." };

		TestUtils.testSentences(txts, pm, 1);

	}
}
