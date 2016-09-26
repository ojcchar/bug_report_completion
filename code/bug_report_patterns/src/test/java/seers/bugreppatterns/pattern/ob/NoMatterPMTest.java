package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NoMatterPMTest extends BaseTest {
	public NoMatterPMTest() {
		pm = new NoMatterPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"Now, in 4.1 it always starts with slide 1, regardless what I try to set in the options." };

		TestUtils.testSentences(sentences, pm, 1);
	}
}
