package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ObsBehaviorLiteralMultiStncePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		List<Sentence> sentences2 = paragraph.getSentences();

		if (sentences2.size() > 1) {
			Sentence sentence = sentences2.get(0);

			String text = TextProcessor.getStringFromLemmas(sentence);

			// ----------------

			boolean b = text
					.matches("((actual|observed|current) )?((result|behavior|description|situation))? ?(:|-+)?");
			if (b) {
				return 1;
			}
		}
		return 0;
	}
}
