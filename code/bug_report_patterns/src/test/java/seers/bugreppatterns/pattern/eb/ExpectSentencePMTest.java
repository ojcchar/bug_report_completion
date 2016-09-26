package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ExpectSentencePMTest extends BaseTest {

	public ExpectSentencePMTest() {
		pm = new ExpectSentencePM();
	}

	@Test
	public void testPositive() throws Exception {

		String[] txts = {
				"Result: The Underline button changes - as expected - to pressed state while the caret is within the underlined part." };
		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "## Expected Results: Almost instant, around 1 second:", "Describe the results you expected:",
				"the network stack is updated but not `/etc/hosts` --- is this expected behavior?",
				"Check4change plugin is not working as expected",
				"@NotFound( action = NotFoundAction.IGNORE ) doesn't behave as expected.",
				"In RC2, all entries worked as expected, so it seems like a regression.", "### Expected behavior" };
		TestUtils.testSentences(txts, pm, 0);

	}

}
