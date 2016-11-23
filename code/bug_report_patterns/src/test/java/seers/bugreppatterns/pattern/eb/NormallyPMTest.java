package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NormallyPMTest extends BaseTest {

	public NormallyPMTest() {
		pm = new NormallyPM();
	}
	

	@Test
	public void testPositive() throws Exception {
		String[] sentences = { };

		TestUtils.testSentences(sentences, pm, 1);
	}

}
