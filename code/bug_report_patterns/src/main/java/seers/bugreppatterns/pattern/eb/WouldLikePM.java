package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class WouldLikePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		List<Integer> wouldIdxs = findMainTokens(tokens);

		for (Integer wouldIdx : wouldIdxs) {
			if (wouldIdx - 1 >= 0 && wouldIdx + 2 < tokens.size()) {
				Token prevToken = tokens.get(wouldIdx - 1);
				Token nextToken = tokens.get(wouldIdx + 1);
				Token nextToken2 = tokens.get(wouldIdx + 2);

				if (prevToken.getGeneralPos().equals("PRP") && nextToken.getGeneralPos().equals("VB")
						&& nextToken.getLemma().equals("like") && nextToken2.getLemma().equals("to")) {
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
			if (token.getLemma().equals("would")) {
				mainToks.add(i);
			}
		}
		return mainToks;
	}
}
