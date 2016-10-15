package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.ob.ErrorNounPhrasePM;
import seers.bugreppatterns.pattern.ob.ErrorTermSubjectPM;
import seers.bugreppatterns.pattern.ob.NegativeAdjOrAdvPM;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.pattern.ob.NegativeVerbPM;
import seers.bugreppatterns.pattern.ob.NoLongerPM;
import seers.bugreppatterns.pattern.ob.NoNounPM;
import seers.bugreppatterns.pattern.ob.NothingHappensPM;
import seers.bugreppatterns.pattern.ob.PassiveVoicePM;
import seers.bugreppatterns.pattern.ob.ProblemInPM;
import seers.bugreppatterns.pattern.ob.StillSentencePM;
import seers.bugreppatterns.pattern.ob.UnableToPM;
import seers.bugreppatterns.pattern.ob.VerbErrorPM;
import seers.bugreppatterns.pattern.ob.VerbNoPM;
import seers.bugreppatterns.pattern.ob.VerbToBeNegativePM;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for S_SR_COND_OBS.
 */
public class ConditionalObservedBehaviorPM extends StepsToReproducePatternMatcher {

	final static Set<String> EXCLUDED_VERBS = JavaUtils.getSet("do", "be", "have", "want", "feel", "deal");

	public final static ObservedBehaviorPatternMatcher[] OB_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new VerbToBeNegativePM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM(),
			new NothingHappensPM(), new PassiveVoicePM(), new StillSentencePM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		if (clauses.size() < 2) {
			return 0;
		}

		int idx = findFirstCondClauseInPresent(clauses);

		// no valid conditional clause
		if (idx == -1 || idx == clauses.size() - 1) {
			return 0;
		}

		// search for OB clauses
		List<Sentence> remainingClauses = clauses.subList(idx + 1, clauses.size());

		// check for present tense clauses
		int idxLastPresentClause = -1;
		for (int i = 0; i < remainingClauses.size(); i++) {
			Sentence clause = remainingClauses.get(i);
			List<Token> clauseTokens = clause.getTokens();
			if (!clauseTokens.isEmpty()) {

				if (isSimplePresent(clauseTokens.get(0), -1, clauseTokens, true)) {
					idxLastPresentClause = i;
				}
			}
		}

		if (idxLastPresentClause != -1) {
			return 1;
		}

		// search for OB clauses
		int idx2 = SentenceUtils.findObsBehaviorSentence(remainingClauses, OB_PMS);
		if (idx2 != -1) {
			return 1;
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

				// case: "when running"
				if (nextToken.getPos().equals("VBG") && !SentenceUtils.lemmasContainToken(EXCLUDED_VERBS, nextToken)) {
					return true;
				} else if (isSimplePresent(nextToken, condTerm, clauseTokens, false)) {
					return true;
				} else if (isPresentPerfect(condTerm, clauseTokens)) {
					return true;
				}

			}
		}
		return false;
	}

	private boolean isPresentPerfect(Integer condTerm, List<Token> clauseTokens) {

		if (condTerm + 3 >= clauseTokens.size()) {
			return false;
		}

		Token nextToken = clauseTokens.get(condTerm + 1);
		Token nextToken2 = clauseTokens.get(condTerm + 2);
		Token nextToken3 = clauseTokens.get(condTerm + 3);

		// case "I have started..."
		if (nextToken.getGeneralPos().equals("PRP") && nextToken2.getLemma().equals("have")
				&& (nextToken3.getPos().equals("VBD") || nextToken3.getPos().equals("VBN"))) {
			return true;
		}

		return false;
	}

	private boolean isSimplePresent(Token nextToken, Integer condTerm, List<Token> clauseTokens, boolean dismissIt) {

		// is next token a pronoun or a WDT (e.g., which, that, etc)
		if ((nextToken.getGeneralPos().equals("PRP") || nextToken.getPos().equals("WDT"))
				&& (dismissIt || !nextToken.getLemma().equals("it"))) {

			if (condTerm + 2 < clauseTokens.size()) {
				final Token nextToken2 = clauseTokens.get(condTerm + 2);

				// case: "when I run..."
				if ((nextToken2.getPos().equals("VBP") || nextToken2.getPos().equals("VBZ")
						|| nextToken2.getPos().equals("VB"))
						&& !SentenceUtils.lemmasContainToken(EXCLUDED_VERBS, nextToken2)) {
					return true;

				} else
				// there is an adverb before the verb
				// case: "when I simply run..."
				if (nextToken2.getPos().equals("RB")) {
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
