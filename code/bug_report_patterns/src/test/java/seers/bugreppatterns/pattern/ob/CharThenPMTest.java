package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class CharThenPMTest extends BaseTest {

	public CharThenPMTest() {
		pm = new CharThenPM();
	}

	@Test
	public void testPositives() throws Exception {
		String[] txts = { " ==&gt; no method comment for run()", " ==&gt; NPE:",
				"DatabaseInformationImpl -&gt; extractionContext variable never release.",
				"Page Tabs &gt; FBML &gt; Signed Request -&gt;  page  value missing", };
		TestUtils.testSentences(txts, pm, 1);
	}

	@Test
	public void testNegatives() throws Exception {
		String[] txts = { "Go to Windows -&gt; Preference -&gt; Plugin Developement -&gt; Compiler.",
				"Creating a new C++ Hello world project and clicking on Project -&gt; Make Targets -&gt; build shows nothing in the list.",
				"In one .MST I select \"SELECT ALL MSO TYPES\" -&gt; 1 and in the other one I do select \"SELECT NO MSO TYPE\" -&gt;" };
		TestUtils.testSentences(txts, pm, 0);
	}

}
