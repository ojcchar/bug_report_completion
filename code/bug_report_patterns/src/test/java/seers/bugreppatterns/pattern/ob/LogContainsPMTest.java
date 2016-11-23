package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class LogContainsPMTest extends BaseTest {
	public LogContainsPMTest() {
		pm = new LogContainsPM();
	}

	@Test
	public void testPositive() throws Exception {
		String[] paragraphs = { "Our site heavily relies on SSI, in particular <!--#include virtual=..-->\n"
				+ "(they're often nested aswell), and mod_rewrite. Output is compressed with\n"
				+ "mod_deflate. Often the crashdumps looks like this:" };

		TestUtils.testParagraphs(paragraphs, pm, 1);
	}
}
