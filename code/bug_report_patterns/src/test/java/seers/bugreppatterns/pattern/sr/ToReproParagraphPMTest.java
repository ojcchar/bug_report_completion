package seers.bugreppatterns.pattern.sr;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ToReproParagraphPMTest extends BaseTest {

	public ToReproParagraphPMTest() {
		pm = new ToReproParagraphPM();
	}

	@Test
	public void testNegatives() throws Exception {

		String[] txts = { "Steps to reproduce:\n		Preview attached report in Web Viewer/HTML/PDF" };
		TestUtils.testParagraphs(txts, pm, 0);

	}
}
