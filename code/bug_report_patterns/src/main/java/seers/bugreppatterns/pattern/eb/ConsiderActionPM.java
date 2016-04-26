package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConsiderActionPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		if (tokens.size() > 1) {
			Token token1 = tokens.get(0);
			if ((token1.getPos().equals("VB") || token1.getPos().equals("VBP"))
					&& token1.getLemma().equals("consider")) {

				Token token2 = tokens.get(1);
				if (token2.getPos().equals("VBG")) {
					return 1;
				}

			}
		}
		return 0;
	}

}
