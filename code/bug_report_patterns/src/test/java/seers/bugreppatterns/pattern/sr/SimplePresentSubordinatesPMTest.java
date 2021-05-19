package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class SimplePresentSubordinatesPMTest extends BaseTest {

	public SimplePresentSubordinatesPMTest() {
		pm = new SimplePresentSubordinatesPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"I type 1 in odometer",
				"- I do a Java Search and sort the results by Parent Path",
				"i create an application and i set it in a tab fan page. ", "On the Tomcat / mod_jk side I use:",
				"The scenario is that the user selects an Exit Type that has a final state \"trigger\" associated with it.",
				"I do npm install and then remove the build tools like python",
				"You close it but it keeps coming back.",
				"I do execute this",
				"I click on first Fillup history entry"
				};	

		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { 
				"/* did we find it?",
				"However_ if you render for aggregation and discard those keys that are within <fb:else> tags within <fb:if-multiple-actors>_ you will find that referer is no longer needed (since we can t track the click back to a single user if story is aggregated) and so we can discard referer from title_data and find that now the stories from multiple actors combine flawlessly.",
				"When we try to make a server side redirection to https://graph.facebook.com/oauth/authorize?",
				"Apache web server uses ISO-8859-1 as default charset for sending all the responses, whereas as I want to use UTF-8 or shift_JIS as charset.",
				"It would be nice if we were able to edit the comments we post to the link.",
				"I do not execute this"
		};

		TestUtils.testSentences(txts, pm, 0);
	}

}
