package seers.bugreppatterns.pattern.eb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.bugreppatterns.entity.Paragraph;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class CouldQuestionSentencePMTest {
	CouldQuestionSentencePM pm = new CouldQuestionSentencePM();

	@Test
	public void testMatchParagraph() throws Exception {

		String[] txts = {

				"Could not background jobs be used to do the filling, so that even if my workspace is big and I left it open on a package explorer it starts up faster?",
				"Could you make possible to avoid this automatic scroll, please?",
				"Could this effect be due to conflict or processing being intercepted by another module?" };

		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];

			System.out.print("Testing (positive) " + i);
			List<Sentence> sentences = TextProcessor.processText(txt);

			Paragraph paragraph = new Paragraph("0");
			paragraph.setSentences(sentences);

			int m = pm.matchParagraph(paragraph);
			assertEquals(1, m);

			System.out.println(" PASSED");

		}
	}

}
