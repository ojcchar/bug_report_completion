package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class MakeImpossiblePMTest extends BaseTest {

	public MakeImpossiblePMTest() {
		pm = new MakeImpossiblePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {
		};

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
		};

		TestUtils.testSentences(sentences, pm, 1);
	}
}
