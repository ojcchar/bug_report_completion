package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class NegativeConditionalPMTest extends BaseTest {

	public NegativeConditionalPMTest() {
		pm = new NegativeConditionalPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {

		};

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"The existing util_ldap.c considers it an error in util_ldap_cache_checkuserid if it searches for a user and gets back more than one entry.",
				"Container random port change when added to another network",
				"The temporary table code use to add the same Column(s) objects to the temporary Table leading to potential aliases clashes if the exact same column insertion sequence were not followed."
		};
		TestUtils.testSentences(sentences, pm, 1);
	}
}
