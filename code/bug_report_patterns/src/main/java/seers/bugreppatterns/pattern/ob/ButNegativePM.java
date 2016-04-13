package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;

public class ButNegativePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		NegativeAuxVerbPM pm = new NegativeAuxVerbPM();
		return pm.matchSentence(sentence);
	}

}
