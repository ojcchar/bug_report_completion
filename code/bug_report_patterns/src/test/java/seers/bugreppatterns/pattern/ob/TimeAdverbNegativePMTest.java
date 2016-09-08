package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 8/11/16.
 */
public class TimeAdverbNegativePMTest extends BaseTest {
	{
		pm = new TimeAdverbNegativePM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
					"### Actual behavior Right now `timestamp`, being used as a sorting criteria, even when sometimes a value other than an actual timestamp is assigned to such `timestamp` attribute, which is misleading." };

		TestUtils.testSentences(sentences, pm, 1);
	}
}
