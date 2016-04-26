package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NecessaryPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> mainTokens = findMainTokens(tokens);
		for (Integer tok : mainTokens) {
			if (tok - 1 >= 0) {
				Token token = tokens.get(tok - 1);
				if (token.getLemma().equals("be") && !token.getPos().equals("VBD") && !token.getPos().equals("VBG")
						&& !token.getPos().equals("VBN")) {
					if (tok - 2 >= 0) {

						Token token2 = tokens.get(tok - 2);
						if (token2.getGeneralPos().equals("PRP") || token2.getGeneralPos().equals("DT")
								|| token2.getGeneralPos().equals("NN") || token2.getPos().equals("VBG")) {
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}

	private List<Integer> findMainTokens(List<Token> tokens) {

		List<Integer> mainToks = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals("necessary")) {
				mainToks.add(i);
			}
		}
		return mainToks;
	}
}
