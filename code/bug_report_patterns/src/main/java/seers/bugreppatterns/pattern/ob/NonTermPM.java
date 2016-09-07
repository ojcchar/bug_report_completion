package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NonTermPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		if (containsNonTerm(tokens) && !isEBModal(tokens) && !containsWould(tokens)) {
			return 1;
		}
		return 0;
	}

	private boolean containsWould(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if (current.getGeneralPos().equals("MD") && current.getLemma().equals("would")) {
				return true;
			} else if (current.getGeneralPos().equals("NN") && current.getLemma().equals("d") && i - 1 >= 0) {
				Token previous = tokens.get(i - 1);
				if (previous.getGeneralPos().equals("NN") || previous.getGeneralPos().equals("PRP")) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean containsNonTerm(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if ((current.getGeneralPos().equals("NN") || current.getGeneralPos().equals("JJ")) && current.getLemma().matches("non([-])?[a-z]+")
					&& !current.getLemma().equals("none")) {
				return true;
			} else if (current.getGeneralPos().equals("JJ") && current.getLemma().equals("non")) {
				return true;
			}
		}
		return false;
	}

}
