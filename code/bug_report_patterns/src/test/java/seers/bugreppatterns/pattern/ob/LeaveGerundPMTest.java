package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class LeaveGerundPMTest extends BaseTest {

	public LeaveGerundPMTest() {
		pm = new LeaveGerundPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = {
				"A JAR which gets exported into a not yet existing project directory does not appear in Package Explorer after export.",
				"Ideally I could turn off decorators for this project but leave them on for the second which depends on the first and that I do have commit privileges on.",
				"it doesn't leave the process running", "it leaves the process; running this also works",
				"Instead it just leaves the user on their facebook mobile page. ",
				"When I share a URL in my Facebook wall page_ I input a URL like http://support.microsoft.com/ph/14019/ko_ Facebook cannot retrieve the page info but just leaves a URL there.",
				"Marquee Won't go away after I leave site!",

		};

		TestUtils.testSentences(negs, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = {
				"Attempting to abort with \"\"apachectl stop\"\" leaves all of the httpd processes still running." };

		TestUtils.testSentences(negs, pm, 1);
	}

}
