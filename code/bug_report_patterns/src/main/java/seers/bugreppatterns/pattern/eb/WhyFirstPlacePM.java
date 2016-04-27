package seers.bugreppatterns.pattern.eb;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class WhyFirstPlacePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		String txt = TextProcessor.getStringFromLemmas(sentence);
		return txt.matches("(?).*why.+in the first place.*") ? 1 : 0;
	}

}
