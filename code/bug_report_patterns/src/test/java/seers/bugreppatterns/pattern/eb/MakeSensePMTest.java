package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class MakeSensePMTest extends BaseTest {

	public MakeSensePMTest() {
		pm = new MakeSensePM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = { "I'm thinking that this is a bug as it makes much more sense to call post_config",
				"Maybe it would make sense to kill the process group instead (using the cgi pgid)",
				"Preventing the two from being used together would seem to make sense.",
				"Maybe it would make sense to kill the process group instead (using the cgi pgid)" };
		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = {
				"When using the Add Import... editor context menu item inner classes are offered as potential\nchoices even though they don't make sense in context.",
				"Another point is that it makes no sense to possibly *invite* the client\nfor sending the body just to discard it.",
				"http://docs.docker.com/docker-hub/accounts/Sam%20Alba%3Csam@docker.com%3E, does not work and even if it did, it would not make sense.",
				"This doesn't seem to make sense, as creating a TTY handles all three signals - I shouldn't need/want to attach them separately afterwards.",
				"In brief, it doesn't seem to make sense that --tty and --attach can be used together, and doing so cause unexpected results.",
				"The best I can do right now is redirect stderr and stdout to /dev/null to suppress them, and generate a unique tag name myself to pass in with `-t` (parsing stdout just doesn't make sense).",
				"I understand why a upper limit make sense, but a 4 character lower limit?" };
		TestUtils.testSentences(txts, pm, 0);

	}
}
