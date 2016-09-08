package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for OB_COND_POS.
 */
public class ConditionalPositivePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> conditionalTermPositions = findTermsInTokens(CONDITIONAL_TERMS, tokens);

		if (conditionalTermPositions == null || conditionalTermPositions.size() < 1) {
			// If no conditional is found we know it doesn't match
			return 0;
		}

		// TODO: Make sure the sub-sentence is not EB
		// TODO: split into subsentences and only consider the one immediately following the conditional clause
		for (Integer condPos : conditionalTermPositions) {
			if (condPos + 1 < tokens.size()) {
				Sentence subSentence = new Sentence(sentence.getId(), tokens.subList(condPos + 1, tokens.size()));

				if (isNegative(subSentence)) {
					return 0;
				}
			}

		}

		// If there's at least one conditional and none of the sub-sentences are negative
		return 1;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, ButNegativePM.NEGATIVE_PMS);
	}
}
