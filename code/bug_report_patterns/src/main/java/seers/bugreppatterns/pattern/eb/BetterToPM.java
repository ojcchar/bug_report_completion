package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class BetterToPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		if (containsBetterTo(sentence.getTokens())) {
			return 1;
		}

		return 0;
	}

	private boolean containsBetterTo(List<Token> tokens) {
		for (int i = 0; i < tokens.size() - 1; i++) {
			Token current = tokens.get(i);
			Token next = tokens.get(i + 1);

			if ((current.getPos().equals("JJR") && current.getLemma().equals("better"))
					&& (next.getGeneralPos().equals("TO"))) {
				Token afterBetterTo = i + 2 < tokens.size() ? tokens.get(i + 2) : null;

				if (afterBetterTo != null
						&& !(afterBetterTo.getGeneralPos().equals("VB") && afterBetterTo.getLemma().equals("be"))) {
					return true;
				}
			}
		}
		return false;
	}
}
