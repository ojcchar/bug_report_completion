package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ConditionalAffirmativePMTest extends BaseTest {

	public ConditionalAffirmativePMTest() {
		pm = new ConditionalAffirmativePM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = {
				"If you go to http://www.new.facebook.com/developers/ at bottom right is a tab called  Status _ which show the Status of the Platform to developers.", };
		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "Feel free to close either of these issues if they're dupes.",

				"will create a picture post_ but if you change the picture url to:",
				"However, I'll understand it if you feel this isn't that important.",
				"But I think there should be a better solution, or at least a warning in the docs not to use % characters while setting HTTP headers from a .",
				"The existing util_ldap.c considers it an error in util_ldap_cache_checkuserid if it searches for a user and gets back more than one entry.",
				"The mod_reqtimeout module is not dropping connections and returning 408 when dealing with \"slow http header\" or \"slow http body\" requests." };
		TestUtils.testSentences(txts, pm, 0);

	}

}
