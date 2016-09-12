package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class PositiveConditionalPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> conditionalTermPositions = findLemmasInTokens(CONDITIONAL_TERMS, tokens);

		if (conditionalTermPositions != null && conditionalTermPositions.size() > 0) {
			// there is a conditional expression now check that the first part
			// is not EB
			for (Integer ctp : conditionalTermPositions) {
				if (ctp > 0) {
					Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(0, ctp));
					List<Token> tok2 = sentence2.getTokens();
					if (!isEBModal(tok2) && !isNegative(sentence2)) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NegativeAfterPM.NEGATIVE_PMS);
	}

}
