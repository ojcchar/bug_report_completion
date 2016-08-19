package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ImperativeSentencePMTest extends BaseTest {

	public ImperativeSentencePMTest() {
		pm = new ImperativeSentencePM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = { "Send printed output to client", "Expected behavior: Send printed output to client" };
		TestUtils.testSentences(txts, pm, 1);

	}

}
