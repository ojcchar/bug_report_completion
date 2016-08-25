package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class UnfortunatelyPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		String txt = TextProcessor.getStringFromLemmas(sentence);
		if (txt.startsWith("unfortunately") || txt.startsWith("oddly enough") || txt.startsWith("for some reason")
				|| txt.startsWith("sadly")) {
			return 1;
		}
		return 0;
	}

}
