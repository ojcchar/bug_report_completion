package seers.bugreppatterns.pattern.eb;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class BehaveDifferentPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		String txt = TextProcessor.getStringFromLemmas(sentence);

		return txt.matches("(?s).+behave different (from|than) .+") ? 1 : 0;
	}

}
