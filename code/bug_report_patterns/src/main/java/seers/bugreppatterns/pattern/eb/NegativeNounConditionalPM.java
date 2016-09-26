package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.ob.NegativeTerms;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for S_EB_NEG_NOUN_COND.
 */
public class NegativeNounConditionalPM extends ExpectedBehaviorPatternMatcher {
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		if (tokens.get(0).getWord().toLowerCase().equals("no") && tokens.size() >= 2) {
			Token secondToken = tokens.get(1);
			if (secondToken.getGeneralPos().equals("NN") && NegativeTerms.NOUNS.contains(secondToken.getLemma())
					&& !SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, tokens).isEmpty()) {
				return 1;
			}
		}

		return 0;
	}
}
