package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class BeforePastClausePMTest extends BaseTest {

	public BeforePastClausePMTest() {
		pm = new BeforePastClausePM();
	}

	@Test
	public void testPositives() throws Exception {

		String[] txts = {
				"Before that_ I was able to display the login and like buttons on the test page I set up for Facebook integration on our news site.",
				"\"Previously, the comma operator (,) and JOIN both had the same precedence, so the join expression t1, t2 JOIN t3 was interpreted as ((t1, t2) JOIN t3)." };
		TestUtils.testSentences(txts, pm, 1);

	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "This was previously working fine for months.", "previously referenced mod_cache.",
				"I thought they were already removed, we have talked about this in the past (can't remember who it was), and I thought we agreed we would remove the API docs, or at least mark as deprecated, so it wouldn't cause confusion.",
				"This was working before, I just noticed it happening today.",
				"Before it opens, it starts the listening sockets and also starts rotatelogs itself." };
		TestUtils.testSentences(txts, pm, 0);

	}
}
