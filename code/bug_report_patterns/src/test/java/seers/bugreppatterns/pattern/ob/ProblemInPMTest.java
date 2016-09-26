package seers.bugreppatterns.pattern.ob;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.utils.TestUtils;

public class ProblemInPMTest extends BaseTest {

	public ProblemInPMTest() {
		pm = new ProblemInPM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] sentences = { "the omission of any characters which have furigana associated with them.",
				"UI elements blinking in Ubuntu", "Right \nnow, it seem to look only for mozilla and netscape.",
				"Unhandled exception caught in event loop.", "problem", };

		TestUtils.testSentences(sentences, pm, 0);

	}

	@Test
	public void testPositive() throws Exception {
		String[] sentences = { "Failure in wikitext ui doc build", "Error on 404 \"missing.jsp\" page",
				"Slow UI refresh with long spreadsheet", "TVT33:TCT229: pl: label truncation in C/C++ Build panel",
				"Possibility for unnavigability", };

		TestUtils.testSentences(sentences, pm, 1);

		@SuppressWarnings("unused")
		String[] trickySentences = {
				"Besides the error message on the webpage [an error occurred while\n"
						+ "processing this directive], there is also a log message sent to\n" + "httpd-error.log:",
				"Visual glitch in site icon display part of location bar", };

	}
}
