package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class NothingHappensPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		String txt = TextProcessor.getStringFromLemmas(sentence);
		if ((txt.toLowerCase().matches(".*[^A-Za-z]?nothing happen[^A-Za-z]?.*")) && !isEBModal(sentence.getTokens())) {
			return 1;
		}
		return 0;
	}

}
