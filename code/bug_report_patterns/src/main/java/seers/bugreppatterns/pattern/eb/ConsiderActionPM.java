package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConsiderActionPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<Integer> mainTokens = getMainTokens(tokens);
		for (Integer mainTok : mainTokens) {

			if (mainTok + 1 < tokens.size()) {

				Token nextToken = tokens.get(mainTok + 1);
				if (nextToken.getPos().equals("VBG")) {
					return 1;
				}
			}
		}

		return 0;
	}

	private List<Integer> getMainTokens(List<Token> tokens) {
		List<Integer> needTokens = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token1 = tokens.get(i);
			if ((token1.getPos().equals("VB") || token1.getPos().equals("VBP"))
					&& token1.getLemma().equals("consider")) {
				needTokens.add(i);
			}
		}
		return needTokens;
	}
}
