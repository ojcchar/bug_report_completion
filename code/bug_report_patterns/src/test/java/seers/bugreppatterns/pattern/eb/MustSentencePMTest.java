package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class MustSentencePMTest extends BaseTest {

	public MustSentencePMTest() {
		pm = new MustSentencePM();
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
"The directory submission form must be buggy_ or there is a bug somewhere else in your system."
				
		};
		TestUtils.testSentences(txts, pm, 0);

		@SuppressWarnings("unused")
		String[] difficultOnes = {
				":-( I know there must be a solution, because some providers run PHP as CGI without problems, but I don't know what it could be." };
	}

}
