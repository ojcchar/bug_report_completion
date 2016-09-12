package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class TriesPM extends ObservedBehaviorPatternMatcher {

	public final static String TRY = "try";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		if (containsTries(tokens)) {
			return 1;
		}
		return 0;
	}

	private boolean containsTries(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getPos().equals("VBZ") && token.getLemma().equals(TRY)) {
				return true;
			}
		}
		return false;
	}

}
