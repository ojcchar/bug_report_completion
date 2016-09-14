package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
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

		// questions?
		if (SentenceUtils.isQuestion(sentence)) {
			return 0;
		}

		if (SentenceUtils.tokensContainAnyLemmaIn(tokens, JavaUtils.getSet("please"))) {
			return 0;
		}

		// -------------------

		List<Integer> needTokens = getNeedTokens(tokens);

		for (Integer needTok : needTokens) {

			// conditional sentence?
			if (conditionalBefore(needTok, tokens)) {
				return 0;
			}

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

	private boolean conditionalBefore(Integer needTok, List<Token> tokens) {

		for (int i = 0; i < needTok; i++) {
			Token token = tokens.get(i);
			if (SentenceUtils.lemmasContainToken(CONDITIONAL_TERMS_2, token)) {
				return true;
			}
		}
		return false;
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
