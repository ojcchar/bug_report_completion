package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/17/16.
 */
public class ImperativeSequencePMTest extends BaseTest {
	{
		pm = new ImperativeSequencePM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] texts = {
				"Create a remote connection, open the connection, add a file from outside of geclipse's connections (e.g. using the terminal) to the directory, use the \"refresh\" action in the context menu on the connection - the busy cursor will show up for a short time but the content will not change.",
				"Launch application", "create a post with two smilies, then view it in the reader.",
				"Change your mind about reblogging and press physical back button." };

		TestUtils.testSentences(texts, pm, 1);
	}

	@Test
	public void testNegative() throws Exception {
		String[] texts = {

				"Am not sure if the Problem is on the PHP or the Apache side....",
				"Installed Apache on WIN2K ADVANCED SERVER with SP3.",
//				"See what \\nhappens:"

		};

		TestUtils.testSentences(texts, pm, 0);
	}

}