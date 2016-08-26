package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class InsteadOfOBPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String txt = TextProcessor.getStringFromLemmas(sentence);
		if (txt.matches(".*[^A-Za-z]?(instead|rather than|in lieu of)[^A-Za-z]?.*")) {
			return 1;
		}

		return 0;
	}

}
