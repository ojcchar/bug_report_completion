package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/22/16.
 */
public class ActionsPresentPerfectPMTest extends BaseTest {
	{
		pm = new ActionsPresentPerfectPM();
	}
	
	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"I have modified and removed a picture",
				"In the test document, I have modified Title style as Title1, Area fill is set to Gray6 and Font effects have set font to Yellow. ",

		};
		TestUtils.testSentences(txts, pm, 1);
	}
	
	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
				

		};
		TestUtils.testSentences(txts, pm, 0);
	}
	
}