package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorHowPM extends ObservedBehaviorPatternMatcher {

	final private static String[] TOKENS = { "while", "when" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> errTerms = findMainTerms(tokens);
		for (Integer errTerm : errTerms) {
			if (errTerm + 1 < tokens.size()) {
				Token nextToken = tokens.get(errTerm + 1);
				// verb in ing
				if (nextToken.getPos().equals("VBG")) {
					return 1;

					// cases like "occurred loading"
				} else if (nextToken.getPos().equals("VBD")) {

					if (errTerm + 2 < tokens.size()) {
						Token nextToken2 = tokens.get(errTerm + 2);
						if (nextToken2.getPos().equals("VBG")) {
							return 1;
						}
					}
					// cases like "is thrown"
				} else if (nextToken.getPos().equals("VBZ") && nextToken.getLemma().equals("be")) {
					if (errTerm + 2 < tokens.size()) {
						Token nextToken2 = tokens.get(errTerm + 2);
						if (nextToken2.getPos().equals("VBN")) {
							return 1;
						}
					}

					// cases like "error comes up"
				} else if (nextToken.getPos().equals("VBZ") || nextToken.getPos().equals("VBP")) {

					if (errTerm + 2 < tokens.size()) {
						Token nextToken2 = tokens.get(errTerm + 2);
						if (nextToken2.getPos().equals("RP")) {
							return 1;
						}
					}
					// cases like "error thrown"
				} else if (nextToken.getPos().equals("VBN")) {
					return 1;
				} else if (nextToken.getGeneralPos().equals("NN")) {
					// when the verb is wrongly detected as NN
					if (nextToken.getLemma().endsWith("ing")) {
						return 1;
					} else {
						// when there is compound error terms: "error page when"
						if (errTerm + 2 < tokens.size()) {
							Token nextToken2 = tokens.get(errTerm + 2);
							if (Arrays.stream(TOKENS).anyMatch(t -> nextToken2.getLemma().contains(t))) {
								return 1;
							}
						}
					}
					// when...
				} else if (Arrays.stream(TOKENS).anyMatch(t -> nextToken.getLemma().contains(t)) && Arrays
						.stream(new String[] { "NN", "JJ" }).anyMatch(t -> !nextToken.getGeneralPos().equals(t))) {
					return 1;
				}
			}
		}
		return 0;

	}

	private List<Integer> findMainTerms(List<Token> tokens) {

		List<Integer> idxs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (Arrays.stream(VerbErrorPM.ERROR_TERMS).anyMatch(t -> token.getLemma().contains(t))
					&& token.getGeneralPos().equals("NN")) {
				idxs.add(i);
			}
		}
		return idxs;
	}
}
