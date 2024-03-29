package seers.bugreppatterns.pattern.utils;

import static org.junit.Assert.assertFalse;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.main.BaseTest;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;

public class TestUtils {

	public static void testSentences(String[] txts, PatternMatcher pm, int expected) throws Exception {

		System.out.println();
		if (expected == 1) {
			System.out.println("Testing pattern (positive cases): " + pm.getClass().getSimpleName());
		} else {
			System.out.println("Testing pattern (negative cases): " + pm.getClass().getSimpleName());
		}

		boolean fail = false;
		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];

			Sentence sentence = SentenceUtils.parseSentence("0", txt);
			int m = pm.matchSentence(sentence);
			if (m != expected) {
				System.out.println("\n Fail for (" + i + "): \"" + txt + "\"");
				fail = true;
			}
		}

		assertFalse(fail);

		System.out.println("Success: " + txts.length + " more cases passed!");
	}

	public static void testParagraphs(String[] txts, PatternMatcher pm, int expected) throws Exception {

		System.out.println();
		if (expected == 1) {
			System.out.println("Testing pattern (positive cases): " + pm.getClass().getSimpleName());
		} else {
			System.out.println("Testing pattern (negative cases): " + pm.getClass().getSimpleName());
		}

		boolean fail = false;
		for (int i = 0; i < txts.length; i++) {
			String txt = txts[i];
			Paragraph paragraph = BaseTest.parseParagraph(txt);
			int m = pm.matchParagraph(paragraph);
			if (m != expected) {
				System.out.println("\n Fail for (" + i + "): \"" + txt + "\"");
				fail = true;
			}
		}

		assertFalse(fail);

		System.out.println("Success: " + txts.length + " more cases passed!");
	}
}
