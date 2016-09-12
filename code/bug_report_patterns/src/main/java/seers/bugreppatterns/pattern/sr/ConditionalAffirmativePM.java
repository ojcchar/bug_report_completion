package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.ob.ButNegativePM;
import seers.bugreppatterns.pattern.ob.ConditionalNegativePM;
import seers.bugreppatterns.pattern.ob.NegativeAdjOrAdvPM;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.pattern.ob.NegativeVerbPM;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConditionalAffirmativePM extends StepsToReproducePatternMatcher {

	public final static String[] COND_TERMS = { "when", "if" };
	final static String[] EXCLUDED_VERBS = { "do", "be", "have", "want", "feel", "deal" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<List<Token>> clauses = extractClauses(tokens);

		if (clauses.size() < 2) {
			return 0;
		}

		int idx = findFirstCondClauseInPresent(clauses);

		if (idx != -1 && idx != clauses.size() - 1) {
			List<List<Token>> remainingClauses = clauses.subList(idx + 1, clauses.size());
			int idx2 = findOBClause(remainingClauses);
			if (idx2 != -1) {
				return 1;
			}

		}

		return 0;
	}

	private static final ObservedBehaviorPatternMatcher[] OB_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new ButNegativePM(), new ConditionalNegativePM(), new NegativeAdjOrAdvPM() };

	private int findOBClause(List<List<Token>> clauses) throws Exception {
		for (int i = clauses.size() - 1; i >= 0; i--) {
			List<Token> clause = clauses.get(i);

			for (ObservedBehaviorPatternMatcher pm : OB_PMS) {
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
		List<Integer> condTerms = this.findLemmasInTokens(COND_TERMS, clauseTokens);

		for (Integer condTerm : condTerms) {

			if (condTerm + 1 < clauseTokens.size()) {
				Token nextToken = clauseTokens.get(condTerm + 1);
				if (nextToken.getPos().equals("VBG")
						&& Arrays.stream(EXCLUDED_VERBS).noneMatch(av -> nextToken.getLemma().equals(av))) {
					return true;
				} else if (nextToken.getGeneralPos().equals("PRP") && !nextToken.getLemma().equals("it")) {
					if (condTerm + 2 < clauseTokens.size()) {
						final Token nextToken2 = clauseTokens.get(condTerm + 2);

						if ((nextToken2.getPos().equals("VBP") || nextToken2.getPos().equals("VBZ")
								|| nextToken2.getPos().equals("VB"))
								&& Arrays.stream(EXCLUDED_VERBS).noneMatch(av -> nextToken2.getLemma().equals(av))) {
							return true;

							// there is an adverb before the verb
						} else if (nextToken2.getPos().equals("RB")) {
							if (condTerm + 3 < clauseTokens.size()) {
								final Token nextToken3 = clauseTokens.get(condTerm + 3);
								if ((nextToken3.getPos().equals("VBP") || nextToken3.getPos().equals("VBZ")
										|| nextToken3.getPos().equals("VB"))
										&& Arrays.stream(EXCLUDED_VERBS)
												.noneMatch(av -> nextToken3.getLemma().equals(av))) {
									return true;
								}
							}
						}
					}
				}

			}
		}
		return false;
	}

	private static final String[] CLAUSE_SEPARATORS = { ";", ",", "-", "_" };

	private List<List<Token>> extractClauses(List<Token> tokens) {

		List<List<Token>> clauses = new ArrayList<>();
		List<Token> clause = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (this.matchTermsByLemma(CLAUSE_SEPARATORS, token) || token.getPos().equals("CC")) {

				// is there any verb in the clause?
				if (clause.stream().anyMatch(t -> t.getGeneralPos().equals("VB"))) {
					clauses.add(clause);
					clause = new ArrayList<>();
				} else {
					clause.add(token);
				}
			} else {
				clause.add(token);
			}
		}
		if (!clause.isEmpty()) {
			clauses.add(clause);
		}

		return clauses;

	}

}
