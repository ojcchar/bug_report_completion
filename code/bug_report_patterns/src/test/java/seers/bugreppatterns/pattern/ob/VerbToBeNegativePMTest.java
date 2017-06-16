package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class VerbToBeNegativePMTest extends BaseTest {

	public VerbToBeNegativePMTest() {
		pm = new VerbToBeNegativePM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"It's a simple error where double quotes instead of single quotes are used within a jsp page.",
				"Many of these are spam.", 
				"There's a problem with relaxing scheduling rules, and allowing some build"
		};
		TestUtils.testSentences(txts, pm, 1);
	}
}
