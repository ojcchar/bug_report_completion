package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.ob.NegativeTerms;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class MustSentencePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		List<Integer> idxs = SentenceUtils.findLemmasInTokens(JavaUtils.getSet("must"), tokens);

		int numValidClauses = 0;
		for (Integer idx : idxs) {
			if (idx + 2 < tokens.size()) {

				Token nextToken1 = tokens.get(idx + 1);
				Token nextToken2 = tokens.get(idx + 2);

				if (nextToken1.getLemma().equals("be")
						&& (SentenceUtils.lemmasContainToken(NegativeTerms.ADJECTIVES, nextToken2)
								|| SentenceUtils.lemmasContainToken(NegativeTerms.NOUNS, nextToken2))) {
					continue;
				}
			}
			numValidClauses++;
		}

		if (numValidClauses > 0) {
			return 1;
		}

		return 0;
	}

}
