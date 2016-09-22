package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class StillSentencePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		String txt = TextProcessor.getStringFromLemmas(sentence);
		if (txt.matches(".*[^a-z]?still .+") || txt.matches(".+ still[^a-z]?.*")) {
			return 1;
		}
		return 0;
	}

}
