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
				"Resize the firefox window so it is very thin, the location widget shrinks correctly until it gets smaller than the URL icon." };
		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "Sent printed output to client and modified that",
				"In Reader, click on a post to view full post.", "Open the url http://www.buyanorgan.net/test.xml" };
		TestUtils.testSentences(txts, pm, 0);

	}

}
