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
			boolean matchLabel = false;
			boolean b = text
					.matches("(?s)((actual|observed|current) )((result|behavior|description|situation) )?(:|-+)");
			if (b) {
				matchLabel = true;
			} else {
				b = text.matches("(?s)((actual|observed|current) )?((result|behavior|description|situation) )(:|-+)");
				if (b) {
					matchLabel = true;
				} else {
					b = text.matches(
							"(?s)((actual|observed|current) )((result|behavior|description|situation))( (:|-+))?");
					if (b) {
						matchLabel = true;
					}
				}
			}

			if (matchLabel) {
				Sentence sentence2 = sentences2.get(1);
				if (!sentence2.isEmpty()) {
					return 1;
				}
			}
		}
		return 0;
	}
}
