package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

/**
 * Created by juan on 4/18/16.
 */
public class ContinuousPresentPastPMTest extends BaseTest {

	public ContinuousPresentPastPMTest() {
		pm = new ContinuousPresentPastPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = {
				"I have an image whose size is roughly 800M.\nI start a container on it using\n"
						+ "`docker run -i -t &lt;myimage&gt;`\nThen i do `du -shx /` inside.",
				"I am authenticating using. $appapikey =  XXX ;\n$appsecret =  XXX ;\n$facebook = new Facebook($appapikey_ $appsecret);\n$fb_user\t=$facebook-&gt;get_loggedin_user();",
				"I was starting a big workspace and I was wondering why it was taking time. Trying to find someone to blame ;-) I debugged and randomly stop and here is the capture of the stack at this point.",
				"i was trying to docker build https://github.com/dotcloud/hipache running docker build . \n"
						+ "in the repository root produces the following output: https://gist.github.com/hansent/5815177",
				"Ok, here's where it started to get bizarre.  I was going to submit this report earlier today, but in testing it with netscape on a Solaris box, it wouldn't show itself.  Then I tried with IE for Solaris, and it \"occasionally\" showed itself.  Then... Then I removed the user x bit from index.html, and the problem went away.  Then I turned XBitHack back on, and the x bit on index.html, and everything was -still- behaving correctly.  (?)",

		};

		TestUtils.testParagraphs(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = {
				"I'm working on a pretty large file, authoring for PacktPub, and I'm running in to a pretty consistent issue.",
				"we do have other apps_ and they works fine. only this app has this issue.",
				"Although it shows up under browse spplications.. Searching for it by name yields no result.. "

		};

		TestUtils.testParagraphs(txts, pm, 0);

	}
}