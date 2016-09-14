package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorHowPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> errTerms = findMainTerms(tokens);

		if (containsConditional(tokens) || containsVerbInPresent(tokens)) {
			return 0;
		}

		for (Integer errTerm : errTerms) {
			if (errTerm + 1 < tokens.size()) {
				Token nextToken = tokens.get(errTerm + 1);
				// verb in ing
				if (nextToken.getPos().equals("VBG")) {
					return 1;

				} else if (nextToken.getGeneralPos().equals("NN")) {
					// when the verb is wrongly detected as NN
					if (nextToken.getLemma().endsWith("ing")) {
						return 1;
					}
				}
			}
		}
		return 0;

	}

	private List<Integer> findMainTerms(List<Token> tokens) {

		List<Integer> idxs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			List<Token> subTokens = new ArrayList<Token>();
			subTokens.add(tokens.get(i));
			if (ErrorNounPhrasePM.checkErrorNounPhrase(subTokens) == 1) {
				idxs.add(i);
			}
		}
		return idxs;
	}

	private boolean containsVerbInPresent(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if (current.getPos().equals("VBP") || current.getPos().equals("VBZ")) {
				return true;
			}
		}
		return false;
	}

	private boolean containsConditional(List<Token> tokens) {
		return SentenceUtils.tokensContainAnyLemmaIn(tokens, CONDITIONAL_TERMS);
	}
}
