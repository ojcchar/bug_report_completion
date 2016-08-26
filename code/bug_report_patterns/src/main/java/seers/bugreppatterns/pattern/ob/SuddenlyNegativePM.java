package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SuddenlyNegativePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		
		int sudden = findSuddenly(tokens);

		if (sudden > -1 && isNegative(sentence)) {
			return 1;
		}

		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		for (PatternMatcher pm : NegativeAfterPM.NEGATIVE_PMS) {
			int match = pm.matchSentence(sentence);
			if (match == 1) {
				return true;
			}
		}
		return false;
	}

	private int findSuddenly(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if (current.getLemma().equals("suddenly") || current.getLemma().equals("sudden")) {
				return i;
			}
		}
		return -1;
	}

}
