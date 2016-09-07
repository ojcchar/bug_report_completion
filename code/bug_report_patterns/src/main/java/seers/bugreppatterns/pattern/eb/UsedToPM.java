package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class UsedToPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		if (containsUsedTo(tokens)) {
			return 1;
		}
		return 0;
	}


	private boolean containsUsedTo(List<Token> tokens) {
		for (int i = 0; i < tokens.size() - 1; i++) {
			Token current = tokens.get(i);
			Token next = tokens.get(i + 1);
			Token previous = i - 1 >= 0 ? tokens.get(i - 1) : null;

			// Find every "used to"
			if ((current.getGeneralPos().equals("VB") && current.getWord().equalsIgnoreCase("used"))
					&& (next.getGeneralPos().equals("TO"))) {
				
				// do not consider "used to" preceded by verb "to be"
				if (previous == null || (!previous.getGeneralPos().equals("VB") && !previous.getLemma().equals("be"))) {
					return true;
				}
			}
		}
		return false;
	}

}
