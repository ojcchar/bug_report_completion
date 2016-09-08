package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class InsteadOfOBPMTest extends BaseTest {
	public InsteadOfOBPMTest() {
		pm = new InsteadOfOBPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = { "Upon reloading the page (which calls for header.html), the file header.htm is loaded instead."};

		TestUtils.testSentences(sentences, pm, 1);
	}
}
