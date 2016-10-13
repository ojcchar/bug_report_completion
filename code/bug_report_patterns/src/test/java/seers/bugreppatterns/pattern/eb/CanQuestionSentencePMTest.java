package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class CanQuestionSentencePMTest extends BaseTest {

	public CanQuestionSentencePMTest() {
		pm = new CanQuestionSentencePM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "can you help us to look into this?" };
		TestUtils.testSentences(txts, pm, 0);
	}

}
