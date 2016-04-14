package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConditionalNegativePM extends ObservedBehaviorPatternMatcher {

	final static String[] CONDITIONAL_TERMS = { "when", "if", "while", "whenever" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> conds = findConditionalTerms(tokens);
		for (Integer cond : conds) {

			if (cond + 1 < tokens.size()) {

				Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(cond + 1, tokens.size()));
				for (PatternMatcher pm : ButNegativePM.NEGATIVE_PMS) {
					int match = pm.matchSentence(sentence2);
					if (match == 1) {
						return 1;
					}
				}
			}

		}

		return 0;
	}

	private List<Integer> findConditionalTerms(List<Token> tokens) {

		List<Integer> conds = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(CONDITIONAL_TERMS).anyMatch(t -> t.equals(token.getLemma()))) {
				conds.add(i);
			}
		}
		return conds;
	}

}
