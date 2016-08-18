package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeConditionalPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		ArrayList<Integer> indexes = foundIndexToken(tokens);
		if (indexes.size() > 0) {
			for (Integer integer : indexes) {
				if (integer > 0 && (integer < tokens.size() - 1)) {
					Sentence negclause = new Sentence(sentence.getId(), tokens.subList(0, integer));
					for (PatternMatcher pm : ButNegativePM.NEGATIVE_PMS) {
						int match = pm.matchSentence(negclause);
						if (match == 1) {
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}

	private ArrayList<Integer> foundIndexToken(List<Token> tokens) {
		ArrayList<Integer> indexConditionalTerms = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(CONDITIONAL_TERMS).anyMatch(t -> token.getLemma().contains(t))) {
				indexConditionalTerms.add(i);
			}
		}
		return indexConditionalTerms;
	}
}
