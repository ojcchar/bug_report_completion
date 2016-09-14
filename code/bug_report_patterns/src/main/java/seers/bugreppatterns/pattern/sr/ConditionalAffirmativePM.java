package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.ob.ButNegativePM;
import seers.bugreppatterns.pattern.ob.ConditionalNegativePM;
import seers.bugreppatterns.pattern.ob.NegativeAdjOrAdvPM;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.pattern.ob.NegativeVerbPM;
import seers.bugreppatterns.pattern.ob.StillSentencePM;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConditionalAffirmativePM extends StepsToReproducePatternMatcher {

	final static Set<String> COND_TERMS = JavaUtils.getSet("when", "if");

	final static Set<String> EXCLUDED_VERBS = JavaUtils.getSet("do", "be", "have", "want", "feel", "deal");

	public static final ObservedBehaviorPatternMatcher[] OB_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new ButNegativePM(), new ConditionalNegativePM(), new NegativeAdjOrAdvPM(), new StillSentencePM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<List<Token>> clauses = SentenceUtils.extractClauses(tokens);

		if (clauses.size() < 2) {
			return 0;
		}

		int idx = findFirstCondClauseInPresent(clauses);

		if (idx != -1 && idx != clauses.size() - 1) {

			List<List<Token>> remainingClauses = clauses.subList(idx + 1, clauses.size());
			int idx2 = findOBClause(remainingClauses, OB_PMS);
			if (idx2 != -1) {
				return 1;
			}

			List<Token> lastClause = remainingClauses.get(remainingClauses.size() - 1);
			if (isSimplePresent(lastClause.get(0), -1, lastClause, true)) {
				return 1;
			}

		}

		return 0;
	}

	private static int findOBClause(List<List<Token>> clauses, ObservedBehaviorPatternMatcher[] patterns)
			throws Exception {
		for (int i = clauses.size() - 1; i >= 0; i--) {
			List<Token> clause = clauses.get(i);

			for (ObservedBehaviorPatternMatcher pm : patterns) {
				if (pm.matchSentence(new Sentence("0", clause)) == 1) {
					return i;
				}
			}
		}
		return -1;
	}

	private int findFirstCondClauseInPresent(List<List<Token>> clauses) {
		for (int i = 0; i < clauses.size(); i++) {
			List<Token> clauseTokens = clauses.get(i);
			boolean isValid = checkConditionalAndPresent(clauseTokens);
			if (isValid) {
				return i;
			}
		}

		return -1;
	}

	private boolean checkConditionalAndPresent(List<Token> clauseTokens) {
		List<Integer> condTerms = SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS_2, clauseTokens);

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

		if (nextToken.getGeneralPos().equals("PRP") && (dismissIt || !nextToken.getLemma().equals("it"))) {
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
