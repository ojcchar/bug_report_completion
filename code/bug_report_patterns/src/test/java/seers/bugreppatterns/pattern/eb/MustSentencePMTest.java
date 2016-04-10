package seers.bugreppatterns.pattern.eb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class MustSentencePMTest {
	MustSentencePM pm = new MustSentencePM();

	@Test
	public void testMatchSentence() throws Exception {
		String[] txts = {
				"org.xml.sax.SAXParseException: XML document structures must start and end within the same entity",
				"Some place there must be something to change that will allow me to use my new email address.",
				"Additionally, the\r\n   request handler  must be set to \"cache-server\", which is done\r\n   automatically if a check_disabled handler returns CACHE_DEFER inside\r\n   the context of cache_url_handler.",
				"These MUST be ignored (rfc2616#14.9.6).", "If not, it must not kill process group it belongs.",
				"In some cases it can kill init and this is what must never happen." };

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
