package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class BeforePastClausePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		if (tokens.size() > 2) {

			Token token1 = tokens.get(0);
			if (token1.getLemma().equals("before")) {
				Token token2 = tokens.get(1);
				if (token2.getLemma().equals(",") || token2.getLemma().equals("_")) {
					return checkPastClause(tokens.subList(2, tokens.size()));
				} else {
					if (token2.getGeneralPos().equals("DT")) {
						Token token3 = tokens.get(2);
						if (token3.getLemma().equals(",") || token3.getLemma().equals("_")) {
							return checkPastClause(tokens.subList(3, tokens.size()));
						}
					}
				}

			}
		}

		return 0;
	}

	private int checkPastClause(List<Token> tokens) {
		List<Integer> verbs = findMainTokens(tokens);
		for (Integer verb : verbs) {
			if (verb - 1 >= 0) {
				Token token = tokens.get(verb - 1);
				if (token.getGeneralPos().equals("PRP") || token.getGeneralPos().equals("DT")
						|| token.getGeneralPos().equals("NN")) {
					return 1;
				}
			}
		}
		return 0;
	}

	private List<Integer> findMainTokens(List<Token> tokens) {

		List<Integer> mainToks = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getPos().equals("VBD")) {
				mainToks.add(i);
			}
		}
		return mainToks;
	}
}
