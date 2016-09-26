package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class CorrectIsPMTest extends BaseTest {

	public CorrectIsPMTest() {
		pm = new CorrectIsPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {};
		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "If I use the long or short id instead of the name, I get the correct logs.",
				"Image is \"-9223372036854776000\" (hex: -0x80000000000000c0L), but `docker inspect` (or`curl /containers/09906a9649d279dc7991dd1e58dbdcc372f9f9e14304549628fbcef21c398785/json`) shows the correct image hash:",
				"It seems can't find correct driver now",
				"All possible interfaces are proxied, both correct interfaces and incorrect interfaces.",
				"Current behavior: The cell color is actually set to the correct color, but it is not shown when a dark theme is used. " };
		TestUtils.testSentences(txts, pm, 0);
	}

}
