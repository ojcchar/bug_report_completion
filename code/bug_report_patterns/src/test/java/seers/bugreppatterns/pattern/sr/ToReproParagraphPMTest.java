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

		String[] txts = { "Steps to reproduce:\n		Preview attached report in Web Viewer/HTML/PDF",
				"Unfortunately, I don't have steps to reproduce, since it does not happen every time.",
				"I am afraid this might be rather difficult to reproduce, as it is related to speed of typing: I am using Writer to take notes during classes, and so typing rather fast (at least as fast as I can...) in French language.",
				"1 Use windows CLI to run `docker run -it ubuntu bash`, you get bash prompt." };
		TestUtils.testParagraphs(txts, pm, 0);

	}
}
