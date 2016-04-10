package seers.bugreppatterns.pattern.eb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ExpectSentencePMTest {

	ExpectSentencePM pm = new ExpectSentencePM();

	@Test
	public void testMatchSentence() throws Exception {
		String[] txts = { "But, I expected return the file \"FooBar.Baz.html\".",
				"   The expected action would be for the proxy to get the object, and \r\nreport '304 Not-Modifed' to the client, so that the next request is served out \r\nof cache, and no further traffic occurs between proxy and upstream server.",
				"Expected result:\r\nI expected it to read \"www2.example.org:443\"\r\nOr I expected a critical error during start time of Apache because the\r\nconfiguration file is arguably inconsistent (see below)",
				"I expected it to be \"on\"",
				"I expected that to override the higher level directive and allow this subdirectory to be accessed without a client certificate.",
				"I would expect the field to be set to \"test@test.com\" in this case.",
				"mozconfig] one would expect the *compiled* firefox to be compiled with debug symbols and installed with debug symbols (and this is what I believe happened with Firefox 2.0).",
				"That's what I would expect from the Installed JREs preference page.",
				"If I were to do a myElement.setStyle( top _ myElement.getAbsoluteTop() +  px )_ you d expect it to not move.",
				"99.1% of the users expecting the focus on the control itself and not the handle button.",
				"The users expecting to press key UP/DOWN or Alt+DOWN to select an item. " };

		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];

			System.out.print("Testing (positive) " + i);
			List<Sentence> sentences = TextProcessor.processText(txt);
			int m = pm.matchSentence(sentences.get(0));
			assertEquals(1, m);

			System.out.println(" PASSED");

		}
	}

}
