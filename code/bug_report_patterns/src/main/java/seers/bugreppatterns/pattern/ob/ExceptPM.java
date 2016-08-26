package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ExceptPM extends ObservedBehaviorPatternMatcher {

	private static final String EXCEPT = "except";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		int except = findExcept(tokens);

		if (except == 0) {
			if (!isEBModal(sentence.getTokens())) {
				return 1;
			}

		} else if (except > 1) {
			Sentence before = new Sentence(sentence.getId(), tokens.subList(0, except));
			if (!isEBModal(before.getTokens())) {
				return 1;
			}
		}

		return 0;
	}

	private boolean isEBModal(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token tok = tokens.get(i);
			if (tok.getGeneralPos().equals("MD") && (tok.getLemma().equals("must") || tok.getLemma().equals("need")
					|| tok.getLemma().equals("should"))) {
				return true;
			}
		}
		return false;
	}

	private int findExcept(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if (current.getLemma().equals(EXCEPT)) {
				return i;
			}
		}
		return -1;
	}

}
