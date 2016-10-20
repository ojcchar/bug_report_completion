package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/20/16.
 */
public class PurposeActionPMTest extends BaseTest {
	{
		pm = new PurposeActionPM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = { "To see the bug in LibO, just put =0^0 in a cell." };
		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { 
				
				"To protect the privacy of our users_ we cannot disclose any further information.",
				"To make a htpasswd file for Linux apache on WindowsXP, I used htpasswd.exe."

		};

		TestUtils.testSentences(txts, pm, 0);

	}

}