package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/*
 * This one applies only for titles
 */
public class ImperativeSentencePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		boolean isImper = SentenceUtils.isImperativeSentence(tokens);
		int match = 0;
		if (isImper) {
			match = 1;
		}

		return match;

	}

}
