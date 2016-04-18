package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ObsBehaviorLiteralStncePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String text = TextProcessor.getStringFromLemmas(sentence);

		// ----------------

		boolean b = text.matches("(?s)((actual|observed|current) )((result|behavior|description|situation) )?(:|-+).+");
		if (b) {
			return 1;
		} else {
			b = text.matches("(?s)((actual|observed|current) )?((result|behavior|description|situation) )(:|-+).+");
			if (b) {
				return 1;
			} else {
				b = text.matches("(?s)((actual|observed|current) )((result|behavior|description|situation) )(:|-+)?.+");
				if (b) {
					return 1;
				}
			}
		}

		return 0;
	}

}
