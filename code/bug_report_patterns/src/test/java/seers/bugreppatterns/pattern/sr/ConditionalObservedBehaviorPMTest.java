package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 9/26/16.
 */
public class ConditionalObservedBehaviorPMTest extends BaseTest {

	{
		pm = new ConditionalObservedBehaviorPM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = {
//				"If you go to http://www.new.facebook.com/developers/ at bottom right is a tab called  Status _ which show the Status of the Platform to developers.",
//				"While running 1.8-dev in a virtual appliance, Shazin and I ran into this bug.",
//				"If I try to add this image to a gallery, the app crashes.",
//				"When I visit stats, the progress indicator at the top of the screen below the title bar sometimes doesn't stop.",
//				"If you click the little x to hide the report_ then you are given the following message:",
				"If I show a comment, then hide it, it will still be there when I open the document again."		
		};
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

		@SuppressWarnings("unused")
		String[] trickyOnes = {
				"However_ what if instead_ for determining whether to aggregate feed stories_ it first parsed it for multiple actors and any title_data attributes that are not used with multiple actors are cast out." };

	}

}