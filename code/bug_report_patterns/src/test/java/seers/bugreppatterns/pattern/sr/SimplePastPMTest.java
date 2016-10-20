package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/14/16.
 */
public class SimplePastPMTest extends BaseTest {
	{
		pm = new SimplePastPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] texts = { 
				"I entered the following status message via the Facebook web app: ",
				"For giggles, I removed the file extension from an image before uploading it to the media library.",
				"I just tried out the Patient Summary module in a demo and I got the stack trace below.",
				"I set FlushMode to Get to avoid writing of second level cache and than called flush; everything was added to the second level cache."		
		};

		TestUtils.testSentences(texts, pm, 1);
	}
	
	

	@Test
	public void testNegative() throws Exception {
		String[] texts = { 
				"The fix for TRUNK-3934 checks whether the folder ~/." 
				};

		TestUtils.testSentences(texts, pm, 0);
	}

}