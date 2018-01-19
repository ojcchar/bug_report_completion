package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeVerbPM extends ObservedBehaviorPatternMatcher {

	private static final Set<String> OTHER_NEGATIVE_TERMS = JavaUtils.getSet("slow doen", "slow down", "faile",
			"stucks up", "consume 100", "get turn into", "be out of", "pull out", "faul", "hangs/get", "failes",
			"timing out", "go away", "jitter", "failing", "be simply go", "be go");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> clausesNoParentheses = SentenceUtils.breakByParenthesis(sentence);
		for (Sentence clause : clausesNoParentheses) {

			List<Sentence> subClauses = SentenceUtils.extractClauses(clause);
			for (Sentence subClause : subClauses) {

				// the sub-clauses
				List<Token> subClauseTokens = subClause.getTokens();
				for (int i = 0; i < subClauseTokens.size(); i++) {

					Token token = subClauseTokens.get(i);
					if ((token.getGeneralPos().equals("VB") || token.getGeneralPos().equals("NN")
							|| token.getGeneralPos().equals("JJ"))
							&& SentenceUtils.lemmasContainToken(NegativeTerms.VERBS, token)) {

						if (i - 1 >= 0) {
							// avoid nouns, e.g.: "a crash"
							// avoid enumerations, e.g.: "6. crash" or "6 crash"
							Token prevToken = subClauseTokens.get(i - 1);
							if (!(prevToken.getLemma().equals("a") || prevToken.getLemma().equals("the"))) {

								// avoid enumerations, e.g.: "6. crash"
								if (i - 2 >= 0) {
									Token prevToken2 = subClauseTokens.get(i - 2);
									if (!(prevToken2.getGeneralPos().equals("CD") && prevToken.getLemma().equals(".")
											&& i - 2 == 0)) {
										return 1;
									}
								} else {
									// avoid enumerations, e.g.: "6 crash" or ". crash"
									if (!((prevToken.getLemma().equals(".") || prevToken.getGeneralPos().equals("CD"))
											&& i - 1 == 0)) {
										return 1;
									}
								}

								// if (!prevToken.getLemma().equals(".") && i -
								// 2 >= 0) {
								// return 1;
								// } else {
								//
								// // avoid enumerations, e.g.: "6. crash"
								// if (i - 2 >= 0) {
								// Token prevToken2 = subClauseTokens.get(i -
								// 2);
								// if (!prevToken2.getGeneralPos().equals("CD"))
								// {
								// return 1;
								// }
								// } else {
								// return 1;
								// }
								// }
							}
						} else {

							if (!token.getGeneralPos().equals("NN")) {

								// no conditional clause
								// avoid cases: "if you delete"
								// if
								// (!SentenceUtils.containsTermsPriorToIndex(i,
								// subClauseTokens, JavaUtils.getSet("if"))) {
								return 1;
								// }
							}
						}
					}
				}

				String txt = TextProcessor.getStringFromLemmas(subClause);

				if (OTHER_NEGATIVE_TERMS.stream().anyMatch(negTerm -> txt.contains(" " + negTerm))) {
					return 1;
				}

				// Matcher otherVerbsMatcher =
				// OTHER_NEGATIVE_VERBS_PATTERN.matcher(txt);
				// if (otherVerbsMatcher.find()) {
				// return 1;
				// }
			}
		}

		return 0;
	}
}
