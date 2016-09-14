package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ProblemInPMTest extends BaseTest {

    public ProblemInPMTest() {
        pm = new ProblemInPM();
    }

	@Test
	public void testNegative() throws Exception {
		String[] negatives = {
				"Right \nnow, it seem to look only for mozilla and netscape.",
				"Unhandled exception caught in event loop.",
				};

		TestUtils.testSentences(negatives, pm, 0);
		
	}
}
