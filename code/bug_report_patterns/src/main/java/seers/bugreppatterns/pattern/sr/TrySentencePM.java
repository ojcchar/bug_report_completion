package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class TrySentencePM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		// avoid clauses suggesting solutions, using "please"
		if (tokens.stream().anyMatch(t -> "please".equals(t.getLemma()))) {
			return 0;
		}

		List<Integer> tries = findMainTokens(tokens);
		for (Integer tryTerm : tries) {
			Token tryToken = tokens.get(tryTerm);

			if (tryTerm - 1 >= 0) {
				Token prevToken = tokens.get(tryTerm - 1);

				// avoid these ones: if/when I try...
				if (tryTerm - 2 >= 0) {
					Token prevToken2 = tokens.get(tryTerm - 2);

					if (Arrays.stream(CONDITIONAL_TERMS).anyMatch(t -> prevToken2.getLemma().equals(t))) {
						continue;
					}
				}

				// case: I tried/try
				if (isValidPronoun(prevToken)) {
					return 1;
				} else {

					// case: I am/was trying..
					if (tryToken.getPos().equals("VBG")) {
						if ((prevToken.getPos().equals("VBD") || prevToken.getPos().equals("VBP")
								|| prevToken.getPos().equals("VBZ")) && prevToken.getLemma().equals("be")) {
							if (tryTerm - 2 >= 0) {
								Token prevToken2 = tokens.get(tryTerm - 2);
								if (isValidPronoun(prevToken2)) {
									return 1;
								}
							}
						}

						// case: I initially tried/try
					} else if (prevToken.getPos().equals("RB")) {

						if (tryTerm - 2 >= 0) {
							Token prevToken2 = tokens.get(tryTerm - 2);

							if (isValidPronoun(prevToken2)) {
								return 1;

							}
						}

						// case: I have tried
					} else if (prevToken.getPos().equals("VBP") && prevToken.getLemma().equals("have")) {

						if (tryTerm - 2 >= 0) {
							Token prevToken2 = tokens.get(tryTerm - 2);
							if (isValidPronoun(prevToken2)) {
								return 1;
							}
						}

						// case: ... and tried
					} else if (prevToken.getPos().equals("CC")
							&& (tryToken.getPos().equals("VBD") || tryToken.getPos().equals("VBN"))) {
						return 1;
					}
				}
			} else {

				// case: try/tried doing...
				if (tryToken.getPos().equals("VB") || tryToken.getPos().equals("VBD")) {
					if (tryTerm + 1 < tokens.size()) {
						Token nextToken = tokens.get(tryTerm + 1);
						if (nextToken.getPos().equals("VBG")) {
							return 1;
						}
					}
				}

			}
		}

		return 0;
	}

	private boolean isValidPronoun(Token token) {
		return token.getGeneralPos().equals("PRP") && !token.getLemma().equals("it");
	}

	private List<Integer> findMainTokens(List<Token> tokens) {

		List<Integer> elements = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")
					&& (token.getLemma().equals("try") || token.getLemma().equals("attempt"))) {
				elements.add(i);
			}
		}
		return elements;
	}
}
