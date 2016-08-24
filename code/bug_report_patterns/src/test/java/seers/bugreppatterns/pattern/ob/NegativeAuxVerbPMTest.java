package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NegativeAuxVerbPMTest extends BaseTest {

	public NegativeAuxVerbPMTest() {
		pm = new NegativeAuxVerbPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = { "last friday 27th june this is not working_" };

		TestUtils.testSentences(negs, pm, 1);
	}
}
