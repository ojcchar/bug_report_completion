package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class KeepVerbPM extends ObservedBehaviorPatternMatcher {

	private final static String KEEP = "keep";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		if (containsKeepVerb(tokens)) {
			return 1;
		}

		return 0;
	}

	private boolean containsKeepVerb(List<Token> tokens) {

		for (int i = 0; i < tokens.size() - 1; i++) {
			Token current = tokens.get(i);
			Token next = tokens.get(i + 1);

			if (current.getGeneralPos().equals("VB") && current.getLemma().equals(KEEP)) {
				if (next.getPos().equals("VBG")) {
					return true;
				}
			}
		}
		return false;
	}

}
