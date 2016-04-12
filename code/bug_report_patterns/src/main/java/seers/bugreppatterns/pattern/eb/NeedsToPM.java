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

			try {
				Token needToken = tokens.get(needTok);
				if (needToken.getGeneralPos().equals("VB")) {
					return 1;
				}
			} catch (IndexOutOfBoundsException e) {
			}
		}

		return 0;
	}

	private List<Integer> getNeedTokens(List<Token> tokens) {
		List<Integer> needTokens = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equalsIgnoreCase("need")) {
				needTokens.add(i);
			}
		}
		return needTokens;
	}

}
