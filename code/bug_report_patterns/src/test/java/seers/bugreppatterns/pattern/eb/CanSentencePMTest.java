package seers.bugreppatterns.pattern.eb;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import seers.bugreppatterns.entity.Paragraph;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class CanSentencePMTest {
	CanSentencePM pm = new CanSentencePM();

	@Test
	public void testMatchParagraph() throws Exception {

		String[] txts = {
				"1 Browser can prevent from opening more tabs than it can show in the tab bar \r\n(worst solution).",
				"2 Tab bar can be expanded to more than one line.",
				"3 All homeless tabs plus one can be hided and last one replasing with pair of \r\nnavigation buttons (\"&lt;\" and \"&gt;\"). ",
				"4 Above solutions can be implementing together and behaviour of tab bar can be \r\nselected by user.",
				"Expected Results:  \r\nA blank page can show up, but the tab bar should be visible!  Novice users would\r\nnot realize that you would have to hit ctrl-w to close the tab." };

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
