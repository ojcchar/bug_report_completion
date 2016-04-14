package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ProblemInPM extends ObservedBehaviorPatternMatcher {

	final private static String[] OTHER_TERMS = { "in", "on", "with", "from" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> errorTerms = findErrorTerms(tokens);
		for (Integer errorTerm : errorTerms) {

			for (int i = errorTerm + 1; i <= errorTerm + 4 && i < tokens.size(); i++) {

				Token nextToken = tokens.get(i);

				if (Arrays.stream(OTHER_TERMS).anyMatch(t -> nextToken.getLemma().equals(t))) {
					return 1;
				}

			}
		}

		return 0;
	}

	private List<Integer> findErrorTerms(List<Token> tokens) {
		List<Integer> errorTerms = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(VerbErrorPM.ERROR_TERMS).anyMatch(t -> token.getLemma().contains(t))) {
				errorTerms.add(i);
			}
		}
		return errorTerms;
	}

}
