package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class NoMatterPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String txt = TextProcessor.getStringFromLemmas(sentence);
		if ((txt.contains("no matter") || txt.contains("regardless")) && !isEBModal(sentence.getTokens())) {
			return 1;
		}
		return 0;
	}

}
