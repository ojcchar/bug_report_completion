package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConditionalNegativePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> conds = findTermsInTokens(CONDITIONAL_TERMS, tokens);
		for (Integer cond : conds) {

			if (cond + 1 < tokens.size()) {

				Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(cond + 1, tokens.size()));
				if(isNegative(sentence2)) {
					return 1;
				}
			}

		}

		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, ButNegativePM.NEGATIVE_PMS);
	}
}
