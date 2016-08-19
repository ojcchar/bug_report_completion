package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ConsiderActionPMTest extends BaseTest {

	public ConsiderActionPMTest() {
		pm = new ConsiderActionPM();
	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "Have you considered using liblxc.so instead of parsing the output of lxc commands?",
				"I consider this port 'internal' to the image's configuration; it's not something which I want a user at container runtime to have to worry about." };
		TestUtils.testSentences(txts, pm, 0);

	}
}
