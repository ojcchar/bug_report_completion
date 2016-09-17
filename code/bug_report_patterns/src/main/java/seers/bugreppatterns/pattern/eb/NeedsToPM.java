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

		List<Token> tokens2 = sentence.getTokens();

		// no questions allowed
		if (SentenceUtils.isQuestion(sentence)) {
			return 0;
		}

		// no sentences with please
		if (SentenceUtils.tokensContainAnyLemmaIn(tokens2, JavaUtils.getSet("please"))) {
			return 0;
		}

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		// check clause by clause
		int numValidClauses = 0;
		for (Sentence clause : clauses) {
			List<Token> tokens = clause.getTokens();
			// -------------------

			List<Integer> needTokens = getNeedTokens(tokens);

			for (Integer needTok : needTokens) {

				// no conditional clause
				if (SentenceUtils.containsTermsPriorToIndex(needTok, tokens, CONDITIONAL_TERMS)) {
					continue;
				}

				// accept the clause if it contains need!
				Token needToken = tokens.get(needTok);
				if (needToken.getGeneralPos().equals("VB")) {
					numValidClauses++;
				}
			}
		}

		if (numValidClauses > 0) {
			return 1;
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
