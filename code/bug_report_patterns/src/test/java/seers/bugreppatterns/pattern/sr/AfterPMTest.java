package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class AfterPMTest extends BaseTest {
	public AfterPMTest() {
		pm = new AfterPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = {};

		TestUtils.testSentences(sentences, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = {
				"After successfully uploading a new picture to our canvas page_ the  Save Thumbnail Version  Button will not click to save the adjusted thumbnail.",
				"after loading start page the loading wheel keeps turning as if still loading page",
				"After switching from 2.2.8 to 2.2.8 I found out it writes something like this to error.log during startup:",
				"After restarting the daemon, volume `foobar2` is no longer visible.",
				"After sending the first mail, writer crashes.",
				"After installing the new release (4.2.0.4), I cannot open existing files in LibO on my Windows 7 pro machine.",
				"After creating a form, when i go to the form list page and select it, it throws an exception with the following stack trace:",
				"After retiring a relationship type, there is no way for unretiring it.",
				"After retiring some forms under Manage Form (admin/forms/form.list), those retired forms are still showing up under Patient Dashboard's Form Entry (patientDashboard.form?patientId=123).",
				"After closing it from the open apps dialog, and then reopening it, the comments loaded.",
				"After having loaded the list, {{SessionImpl.list()}} calls {{SessionImpl.afterOperation()}}, which calls {{jdbcContext.afterNontransactionalQuery()}} (as there is no active transaction)." };

		TestUtils.testSentences(sentences, pm, 1);
	}

}
