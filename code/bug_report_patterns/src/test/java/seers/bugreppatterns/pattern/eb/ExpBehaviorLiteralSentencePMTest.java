package seers.bugreppatterns.pattern.eb;

import org.junit.Test;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.main.BaseTest;
import seers.textanalyzer.entity.Sentence;

public class ExpBehaviorLiteralSentencePMTest extends BaseTest {

	public ExpBehaviorLiteralSentencePMTest() {
		pm = new ExpBehaviorLiteralSentencePM();
	}

	@Test
	public void testNegative() throws Exception {
		String[] txts = {
				"Expected Results:  \nshortened the box to fit within the viewable screen, or opened it in the other\ndirection (up).",
				"Expected: Hit the space where the version inof is." };
		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];

			Paragraph paragraph = parseParagraph(txt);

			Sentence sentence = paragraph.getSentences().get(0);
			int m = pm.matchSentence(sentence);
			if (m != 1) {
				System.out.println("\n Fail for (" + i + "): \"" + txt + "\"");
				// pm.matchSentence(sentence);
			}
		}

	}

}
