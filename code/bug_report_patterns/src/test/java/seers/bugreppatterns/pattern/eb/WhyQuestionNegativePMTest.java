package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class WhyQuestionNegativePMTest extends BaseTest {

	public WhyQuestionNegativePMTest() {
		pm = new WhyQuestionNegativePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = { "I don't understand why I can't do this", "why is it so hard? ", "why?" };

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = { "why shouldn't I be able to to this?", "Why don't we allow to do this other thing?" };

		TestUtils.testSentences(sentences, pm, 1);
	}

}
