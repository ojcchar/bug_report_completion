package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * "Need to" --> the to is optional
 * 
 * @author ojcch
 *
 */
public class NeedsToPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		List<Integer> needTokens = getNeedTokens(tokens);

		for (Integer needTok : needTokens) {

			Token needToken = tokens.get(needTok);
			if (needToken.getGeneralPos().equals("VB")) {
				return 1;
			} else {
				if (needTok + 1 < tokens.size()) {

					Token nextToken = tokens.get(needTok + 1);
					if (nextToken.getLemma().equals("to")) {
						return 1;
					}
				}
			}
		}

		return 0;
	}

	private List<Integer> getNeedTokens(List<Token> tokens) {
		List<Integer> needTokens = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals("need")) {
				needTokens.add(i);
			}
		}
		return needTokens;
	}

}
