package seers.bugreppatterns.pattern.sr;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.bugreppatterns.main.BaseTest;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class TrySentencePMTest extends BaseTest {

	public TrySentencePMTest() {
		pm = new TrySentencePM();
	}

	@Test
	public void testNegatives() throws Exception {

		String text = "If I try to open a .htm file it will report that it\r\ntried to open a .";
		List<Sentence> sentences = TextProcessor.processText(text);
		int match = pm.matchSentence(new Sentence("0", TextProcessor.getAllTokens(sentences)));

		assertEquals(0, match);

	}

}
