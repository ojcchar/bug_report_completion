package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ThereIsNoPM extends ObservedBehaviorPatternMatcher {

	final private static Set<String> NEGATIVE_TERMS = JavaUtils.getSet("no", "nothing" );

	public final static Set<String> THERE = JavaUtils.getSet( "there" );

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
					&& SentenceUtils.lemmasContainToken(NEGATIVE_TERMS, nextToken2)) {
				return 1;
			}

		}
		return 0;
	}

	private List<Integer> findTheres(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(THERE, tokens);
	}

}
