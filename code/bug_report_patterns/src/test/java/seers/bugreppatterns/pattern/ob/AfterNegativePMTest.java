package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class AfterNegativePMTest extends BaseTest {

	public AfterNegativePMTest() {
		pm = new AfterNegativePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] negs = { "After this, the editor did not redraw any more.",
				"A JAR which gets exported into a not yet existing project directory does not appear in Package Explorer after export.",
				"I was using DynamicFBML/MockAJAX in my application_ it was giving required result just hit my server and got response_ but after last friday 27th june this is not working_",
				"Now_ the showfeedialog is not shown after the login dialog.", "getting an error after login",
				"User Comments appear after 10-15 minutes delay!",
				"After tabbing or otherwise navigating through the autocomplete list for the location bar, the last entry selected appears in the location bar but is not loaded (this is correct).",
				"The add bookmark dialog doesn't close after the OK button is pressed.",
				"Firefox forgets which sites are allowed to create popups after extended use. complete restart temporarily fixes problem" };

		TestUtils.testSentences(negs, pm, 0);
	}

}
