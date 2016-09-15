package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class AsResultPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String txt = TextProcessor.getStringFromLemmas(sentence);
		if ((txt.toLowerCase().matches("^.*[^a-z]?as (a )?result[^a-z]?.*")
				|| txt.toLowerCase().matches("^.*[^a-z]?the (consequence|implication) be[^a-z].*"))
				&& !isEBModal(sentence.getTokens())) {
			return 1;
		}
		return 0;
	}

}
