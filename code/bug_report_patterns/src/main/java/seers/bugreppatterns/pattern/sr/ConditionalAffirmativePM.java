package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConditionalAffirmativePM extends StepsToReproducePatternMatcher {

	final static Set<String> EXCLUDED_VERBS = JavaUtils.getSet("do", "be", "have", "want", "feel", "deal");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		if (clauses.size() < 2) {
			return 0;
		}

		int idx = findFirstCondClauseInPresent(clauses);

		if (idx != -1 && idx != clauses.size() - 1) {

			List<Sentence> remainingClauses = clauses.subList(idx + 1, clauses.size());
			int idx2 = SentenceUtils.findObsBehaviorSentence(remainingClauses);
			if (idx2 != -1) {
				return 1;
			}

			Sentence lastClause = remainingClauses.get(remainingClauses.size() - 1);
			List<Token> clauseTokens = lastClause.getTokens();
			if (isSimplePresent(clauseTokens.get(0), -1, clauseTokens, true)) {
				return 1;
			}

		}

		return 0;
	}

	private int findFirstCondClauseInPresent(List<Sentence> clauses) {
		for (int i = 0; i < clauses.size(); i++) {
			List<Token> clauseTokens = clauses.get(i).getTokens();
			boolean isValid = checkConditionalAndPresent(clauseTokens);
			if (isValid) {
				return i;
			}
		}

		return -1;
	}

	private boolean checkConditionalAndPresent(List<Token> clauseTokens) {
		List<Integer> condTerms = SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, clauseTokens);

		for (Integer condTerm : condTerms) {

			if (condTerm + 1 < clauseTokens.size()) {
				Token nextToken = clauseTokens.get(condTerm + 1);
				if (nextToken.getPos().equals("VBG") && !SentenceUtils.lemmasContainToken(EXCLUDED_VERBS, nextToken)) {
					return true;
				} else if (isSimplePresent(nextToken, condTerm, clauseTokens, false)) {
					return true;
				}

			}
		}
		return false;
	}

	private boolean isSimplePresent(Token nextToken, Integer condTerm, List<Token> clauseTokens, boolean dismissIt) {

		// is next token a pronoun or a WDT (e.g., which, that, etc)
		if ((nextToken.getGeneralPos().equals("PRP") || nextToken.getPos().equals("WDT"))
				&& (dismissIt || !nextToken.getLemma().equals("it"))) {
			if (condTerm + 2 < clauseTokens.size()) {
				final Token nextToken2 = clauseTokens.get(condTerm + 2);

				if ((nextToken2.getPos().equals("VBP") || nextToken2.getPos().equals("VBZ")
						|| nextToken2.getPos().equals("VB"))
						&& !SentenceUtils.lemmasContainToken(EXCLUDED_VERBS, nextToken2)) {
					return true;

					// there is an adverb before the verb
				} else if (nextToken2.getPos().equals("RB")) {
					if (condTerm + 3 < clauseTokens.size()) {
						final Token nextToken3 = clauseTokens.get(condTerm + 3);
						if ((nextToken3.getPos().equals("VBP") || nextToken3.getPos().equals("VBZ")
								|| nextToken3.getPos().equals("VB"))
								&& !SentenceUtils.lemmasContainToken(EXCLUDED_VERBS, nextToken3)) {
							return true;
						}
					}
				}
			}
		}

		return false;
	}

}
