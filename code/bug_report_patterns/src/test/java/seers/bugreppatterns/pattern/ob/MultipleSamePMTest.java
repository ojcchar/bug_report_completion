package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class MultipleSamePMTest extends BaseTest {

	public MultipleSamePMTest() {
		pm = new MultipleSamePM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = { "multiple times for the same port." };
		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "Add support for multiple entity names used with the same Java class",
				"To avoid multiple people working on the same page, remove the \"cleanup\" label before starting work.",
				"We are using Spring Data Jpa 1.9.4 and have multiple entity classes that share the same Primary Key class.",
				"the same bookmark in multiple places in the bookmark menu.",
				"Same Sequence is created and dropped multiple times" };
		TestUtils.testSentences(txts, pm, 0);
	}
}
