package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class AsExpectedPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		String txt = TextProcessor.getStringFromTerms(sentence);
		if ((txt.toLowerCase().matches(".+[^A-Za-z]?as expected[^A-Za-z]?.*")) && !isEBModal(sentence.getTokens())) {
			return 1;
		}
		return 0;
	}

}
