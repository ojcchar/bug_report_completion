package seers.bugreppatterns.pattern.eb;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;

/*
 * This one applies only for titles
 */
public class ImperativeSentencePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		boolean isImper = SentenceUtils.isImperativeSentence(sentence);
		int match = 0;
		if (isImper) {
			match = 1;
		}

		return match;

	}

}
