package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorCondPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new ProblemInPM(), new NoNounPM(), new ErrorNounPhrasePM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		ArrayList<Integer> condIndexes = foundIndexToken(tokens);
		if (!condIndexes.isEmpty()) {
			for (Integer condIndex : condIndexes) {
				if (condIndex > 0 && (condIndex < tokens.size() - 1)) {
					Sentence errorClause = new Sentence(sentence.getId(), tokens.subList(0, condIndex));
					for (PatternMatcher pm : NEGATIVE_PMS) {
						int match = pm.matchSentence(errorClause);
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
			if (Arrays.stream(NegativeConditionalPM.TOKENS).anyMatch(t -> token.getWord().equalsIgnoreCase(t))) {
				indexConditionalTerms.add(i);
			}
		}
		return indexConditionalTerms;
	}
}
