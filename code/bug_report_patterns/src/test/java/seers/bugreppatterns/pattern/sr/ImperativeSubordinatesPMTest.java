package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ImperativeSubordinatesPMTest extends BaseTest {

	public ImperativeSubordinatesPMTest() {
		pm = new ImperativeSubordinatesPM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = {
				// "Resize the firefox window so it is very thin, the location
				// widget shrinks correctly until it gets smaller than the URL
				// icon.,"

				// "In Reader, click on a post to view full post.",
				"Do your mind about reblogging and modify physical back button."
				// "Open the url http://www.buyanorgan.net/test.xml",
				// "Example: run container on some already bind port."
		};
		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "Sent printed output to client and modify that" };
		TestUtils.testSentences(txts, pm, 0);

	}

}
