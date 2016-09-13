package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class BecausePM extends ObservedBehaviorPatternMatcher {

	public final static String[] BECAUSE = { "because" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> becauseIndexes = findBecause(tokens);

		if (!becauseIndexes.isEmpty()) {
			// check for negative pre clause
			for (Integer index : becauseIndexes) {
				Sentence before = new Sentence(sentence.getId(), tokens.subList(0, index));
				if (isNegative(before)) {
					return 1;
				}
			}
		}
		return 0;
	}

	private List<Integer> findBecause(List<Token> tokens) {
		return findLemmasInTokens(BECAUSE, tokens);
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, ButNegativePM.NEGATIVE_PMS);
	}

}
