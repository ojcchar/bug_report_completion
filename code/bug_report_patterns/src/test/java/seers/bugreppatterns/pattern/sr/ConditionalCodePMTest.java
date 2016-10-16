package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/20/16.
 */
public class ConditionalCodePMTest extends BaseTest {
	{
		pm = new ConditionalCodePM();
	}
	
	@Test
	public void testNegatives() throws Exception{
		String[] txts = {
				"This is usualy after renaming or deleting resources from the project with background automatic publish is running."
		};
		TestUtils.testSentences(txts, pm, 0);
	}
}