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
				// "If I just add in the text \"blabla\" somewhere, save, close,
				// open, the comments are all gone." ,
				"If I show a comment, then hide it, it will still be there when I open the document again." };
		TestUtils.testParagraphs(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "Feel free to close either of these issues if they're dupes.",
				"will create a picture post_ but if you change the picture url to:",
				"However, I'll understand it if you feel this isn't that important.",
				"But I think there should be a better solution, or at least a warning in the docs not to use % characters while setting HTTP headers from a .",
				"The existing util_ldap.c considers it an error in util_ldap_cache_checkuserid if it searches for a user and gets back more than one entry.",
				"The mod_reqtimeout module is not dropping connections and returning 408 when dealing with \"slow http header\" or \"slow http body\" requests." };
		TestUtils.testParagraphs(txts, pm, 0);

	}

}
