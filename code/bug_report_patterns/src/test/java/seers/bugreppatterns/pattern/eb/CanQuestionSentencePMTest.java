package seers.bugreppatterns.pattern.eb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.bugreppatterns.entity.Paragraph;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class CanQuestionSentencePMTest {
	CanQuestionSentencePM pm = new CanQuestionSentencePM();

	@Test
	public void testMatchParagraph() throws Exception {

		String[] txts = {
				"Can we instead describe the facets in a way that uses the corresponding J2EE/Web standards so that users new to WTP aren't left guessing?",
				"Can we improve this before we ship final builds?",
				"Can we get a safe way to display information only to friends of a user within an application tab?" };

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
