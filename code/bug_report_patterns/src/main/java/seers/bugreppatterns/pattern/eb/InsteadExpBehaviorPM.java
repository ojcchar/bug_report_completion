package seers.bugreppatterns.pattern.eb;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class InsteadExpBehaviorPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		String txt = TextProcessor.getStringFromLemmas(sentence);
		if (txt.matches(".+(instead of|rather than|in lieu of).+")) {
			return 1;
		}

		return 0;
	}

}
