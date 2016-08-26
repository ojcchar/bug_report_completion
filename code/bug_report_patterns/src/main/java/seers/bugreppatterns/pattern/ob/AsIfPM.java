package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class AsIfPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		String txt = TextProcessor.getStringFromLemmas(sentence);
		if ((txt.toLowerCase().matches(".*[^A-Za-z]?as if[^A-Za-z]?.+")) && !isEBModal(sentence.getTokens())) {
			return 1;
		}
		return 0;
	}

}
