package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ButPMTest extends BaseTest {

	public ButPMTest() {
		pm = new ButPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"In this example the `strace` output shows that `\"Entrypoint\": [\"/bin/bash\"-l\"]` was sent to the server, but inspecting the resulting image shows `\"Entrypoint\": null`.",
				"The program should output 1 but it outputs null instead."
		};
		TestUtils.testSentences(sentences, pm, 1);
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = { "But I can't do this.", };
		TestUtils.testSentences(sentences, pm, 0);
	}
}
