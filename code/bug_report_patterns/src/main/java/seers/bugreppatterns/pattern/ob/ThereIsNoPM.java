package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ThereIsNoPM extends ObservedBehaviorPatternMatcher {

	final private static String[] NEGATIVE_TERMS = { "no", "nothing" };

	public final static String[] THERE = { "there" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> theres = findTheres(tokens);

		for (Integer there : theres) {

			if (there > tokens.size() - 3) {
				continue;
			}

			Token nextToken = tokens.get(there + 1);
			Token nextToken2 = tokens.get(there + 2);

			if (nextToken.getLemma().equals("be")
					&& Arrays.stream(NEGATIVE_TERMS).anyMatch(t -> nextToken2.getLemma().equals(t))) {
				return 1;
			}

		}
		return 0;
	}

	private List<Integer> findTheres(List<Token> tokens) {
		return findLemmasInTokens(THERE, tokens);
	}

}
