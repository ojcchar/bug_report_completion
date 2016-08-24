package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class AfterPositivePMTest extends BaseTest {

	public AfterPositivePMTest() {
		pm = new AfterPositivePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = {
				"A JAR which gets exported into a not yet existing project directory does not appear in Package Explorer after export.",
				"Now_ the showfeedialog is not shown after the login dialog.", "getting an error after login",
				"User Comments appear after 10-15 minutes delay!",
				"Firefox forgets which sites are allowed to create popups after extended use. complete restart temporarily fixes problem",
				"The add bookmark dialog doesn't close after the OK button is pressed.",
				"add bookmark dialog doesn't close after bookmark added",
				"this generaly occurs after three or ore hours of continuous use with popups occuring form one site on the allow list (www.we-love-anime.com/forum great anime forum...) on an average of 1 every one to two minutes.",
				"Ctrl-Z (undo) reveals visited URLs AFTER clearing history",
				"Marquee Won't go away after I leave site!",
				"Folders in bookmarks menu are inaccessible after moving a bookmark to/in the menu",
				"Drop down remains on the screen after selecting it and then hitting the delete key",
				"The precompiled version does not respond when a client tries to download a big file after about (say) half an hour.",
				"Runaway processes after ssl proxy use", "Apache2 service does not start after httpd.conf is modified.",
				"Something happens after crash",
				"after it dies",
				"I was using DynamicFBML/MockAJAX in my application_ it was giving required result just hit my server and got response_ but after last friday 27th june this is not working_",
				"After tabbing or otherwise navigating through the autocomplete list for the location bar, the last entry selected appears in the location bar but has not loaded (this is correct).",
				"After this, the editor did not redraw any more.",
				"Something happens after selecting it crashes",
				"Something happens after this happens, crash"
			};

		TestUtils.testSentences(negs, pm, 0);
	}

	@Test
	public void testPositive() throws Exception {
		String[] negs = {"after loading start page the loading wheel keeps turning as if still loading page",
				"After switching from 2.2.8 to 2.2.8 I found out it writes something like this to error.log during startup:",
				 };

		TestUtils.testSentences(negs, pm, 1);
	}
}