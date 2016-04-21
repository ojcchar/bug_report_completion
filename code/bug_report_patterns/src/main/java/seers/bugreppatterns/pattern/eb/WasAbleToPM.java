package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class WasAbleToPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> mainToks = findMainTokens(tokens);
		for (Integer ableIdx : mainToks) {
			if (ableIdx - 1 >= 0 && ableIdx + 1 < tokens.size()) {
				Token prevToken = tokens.get(ableIdx - 1);
				Token nextToken = tokens.get(ableIdx + 1);

				if (prevToken.getPos().equals("VBD") && prevToken.getLemma().equals("be")
						&& nextToken.getLemma().equals("to")) {
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
			if (token.getLemma().equals("able") && token.getGeneralPos().equals("JJ")) {
				mainToks.add(i);
			}
		}
		return mainToks;
	}
}
