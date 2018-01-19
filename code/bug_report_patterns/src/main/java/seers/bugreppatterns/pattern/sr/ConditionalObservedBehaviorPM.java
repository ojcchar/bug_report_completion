package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.eb.WouldBeSentencePM;
import seers.bugreppatterns.pattern.ob.ErrorNounPhrasePM;
import seers.bugreppatterns.pattern.ob.ErrorTermSubjectPM;
import seers.bugreppatterns.pattern.ob.HappensPM;
import seers.bugreppatterns.pattern.ob.NegativeAdjOrAdvPM;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.pattern.ob.NegativeVerbPM;
import seers.bugreppatterns.pattern.ob.NoLongerPM;
import seers.bugreppatterns.pattern.ob.NoNounPM;
import seers.bugreppatterns.pattern.ob.NothingHappensPM;
import seers.bugreppatterns.pattern.ob.PassiveVoicePM;
import seers.bugreppatterns.pattern.ob.ProblemInPM;
import seers.bugreppatterns.pattern.ob.SeemsPM;
import seers.bugreppatterns.pattern.ob.StillSentencePM;
import seers.bugreppatterns.pattern.ob.UnableToPM;
import seers.bugreppatterns.pattern.ob.VerbErrorPM;
import seers.bugreppatterns.pattern.ob.VerbNoPM;
import seers.bugreppatterns.pattern.ob.VerbToBeNegativePM;
import seers.bugreppatterns.pattern.ob.WorksFinePM;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for S_SR_COND_OBS.
 */
public class ConditionalObservedBehaviorPM extends StepsToReproducePatternMatcher {

	final static Set<String> EXCLUDED_VERBS = JavaUtils.getSet("do", "be", "have", "want", "feel", "deal", "look", "decide", "need");

	public final static ObservedBehaviorPatternMatcher[] OB_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new VerbToBeNegativePM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM(),
			new NothingHappensPM(), new PassiveVoicePM(), new StillSentencePM(), new SeemsPM(), new WorksFinePM(),
			new HappensPM(), new NoLongerPM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		
		// avoid sentences like: "it would be nice if... "
		WouldBeSentencePM pm2 = new WouldBeSentencePM();
		if (pm2.matchSentence(sentence)!=0) {
			return 0;
		}
		
//		List<Token> tokens = sentence.getTokens();
//		if (tokens.stream().anyMatch(tok -> tok.getLemma().equals("would"))) {
//			return 0;
//		}

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		int idx = findFirstCondClauseInPresent(clauses);

		// no valid conditional clause
		if (idx == -1) {
			return 0;
		}

		Sentence condClause = clauses.get(idx);
		List<Token> condClausetokens = condClause.getTokens();
		
		int idxCond = findConditionalAndTenseToken(condClausetokens);
		Sentence preClause = new Sentence("0", condClausetokens.subList(0, idxCond));
		List<Token> preClauseTokens = preClause.getTokens();

		// ------------------------------------
		// avoid some cases
		
		//exclude cases like: "please tell me if you need..."
		if (preClauseTokens.stream().anyMatch(tok -> "please".equals(tok.getLemma()))) {
			return 0;
		}
		
		//exclude cases like: "no matter if you..."
		String preClauseTxt = TextProcessor.getStringFromLemmas(preClause);
		if (preClauseTxt.matches(".*no matter.*")) {
			return 0;
		}

		// ------------------------------------

		List<Sentence> remainingClauses = clauses.subList(idx + 1, clauses.size());

		boolean validSentence = checkForRemainingClauses(remainingClauses);
		if (validSentence) {
			return 1;
		} else {

			// check if there is an OB clause (prefix) prior to the
			// conditional
			// token

			for (ObservedBehaviorPatternMatcher pm : OB_PMS) {
				if (pm.matchSentence(preClause) == 1) {
					return 1;
				}
			}

			// check if there is an OB clause in the conditional clause
			Sentence postClause = new Sentence("0", condClausetokens.subList(idxCond + 1, condClausetokens.size()));

			for (ObservedBehaviorPatternMatcher pm : OB_PMS) {
				if (pm.matchSentence(postClause) == 1) {
					return 1;
				}
			}

			// check if there is an OB clause prior to the conditional clause
			if (idx - 1 >= 0) {
				Sentence previousClause = clauses.get(idx - 1);
				for (ObservedBehaviorPatternMatcher pm : OB_PMS) {
					if (pm.matchSentence(previousClause) == 1) {
						return 1;
					}
				}
			}
		}

		// -----------------------------------------------

		return 0;
	}

	private boolean checkForRemainingClauses(List<Sentence> remainingClauses) throws Exception {

		// check for present tense clauses
		int idxLastPresentClause = -1;
		for (int i = 0; i < remainingClauses.size(); i++) {
			Sentence clause = remainingClauses.get(i);
			List<Token> clauseTokens = clause.getTokens();
			if (!clauseTokens.isEmpty()) {

				if (isSimpleTenseWithPronoun(clauseTokens.get(0), -1, clauseTokens, true)) {
					idxLastPresentClause = i;
				} else if (isSimpleTense(-1, clauseTokens.get(0), clauseTokens)) {
					idxLastPresentClause = i;
				}
			}
		}

		if (idxLastPresentClause != -1) {
			return true;
		}

		// search for OB clauses
		int idx2 = SentenceUtils.findObsBehaviorSentence(remainingClauses, OB_PMS);
		if (idx2 != -1) {
			return true;
		}

		// cases: OB clauses like "but not if the..."
		// only the first two tokens are checked (i.e., 'not' and 'if') because
		// the 'but' token is removed when the sentence is split in clauses
		return remainingClauses.stream()
				.anyMatch(clause -> clause.getTokens().size() > 2 && clause.getTokens().get(0).getLemma().equals("not")
						&& clause.getTokens().get(1).getLemma().equals("if"));

	}

	public static int findFirstCondClauseInPresent(List<Sentence> clauses) {
		for (int i = 0; i < clauses.size(); i++) {
			List<Token> clauseTokens = clauses.get(i).getTokens();
			boolean isValid = checkConditionalAndTense(clauseTokens);
			if (isValid) {
				return i;
			}
		}

		return -1;
	}

	private static int findConditionalAndTenseToken(List<Token> clauseTokens) {
		List<Integer> condTerms = SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, clauseTokens);

		for (Integer condTerm : condTerms) {

			if (condTerm + 1 < clauseTokens.size()) {
				Token nextToken = clauseTokens.get(condTerm + 1);

				// case: "when running"
				if (nextToken.getPos().equals("VBG") && !SentenceUtils.lemmasContainToken(EXCLUDED_VERBS, nextToken)) {
					return condTerm;

				} else if (isSimpleTenseWithPronoun(nextToken, condTerm, clauseTokens, false)) {
					return condTerm;
				} else if (isPresentPerfect(condTerm, clauseTokens)) {
					return condTerm;
				} else {

					if (condTerm + 3 < clauseTokens.size()) {

						Token nextToken2 = clauseTokens.get(condTerm + 2);
						Token nextToken3 = clauseTokens.get(condTerm + 3);

						Set<String> pronouns = JavaUtils.getSet("i", "we", "you");

						// case: When we are running
						if (nextToken3.getPos().equals("VBG")
								&& !SentenceUtils.lemmasContainToken(EXCLUDED_VERBS, nextToken)
								&& pronouns.stream().anyMatch(pron -> nextToken.getLemma().equalsIgnoreCase(pron))
								&& nextToken2.getLemma().equals("be")) {
							return condTerm;
						}
					}

					if (condTerm + 4 < clauseTokens.size()) {

						Token nextToken2 = clauseTokens.get(condTerm + 2);
						Token nextToken3 = clauseTokens.get(condTerm + 3);
						Token nextToken4 = clauseTokens.get(condTerm + 4);

						Set<String> subjects = JavaUtils.getSet("user", "patient");
						Set<String> posToBe = JavaUtils.getSet("VBZ", "VBP");
						Set<String> posVerb = JavaUtils.getSet("VBD", "VBN");

						// case: when a user is prompted
						if (subjects.stream().anyMatch(subj -> nextToken2.getLemma().equals(subj))
								&& posToBe.stream().anyMatch(p -> nextToken3.getPos().equals(p))
								&& nextToken3.getLemma().equals("be")
								&& posVerb.stream().anyMatch(p -> nextToken4.getPos().equals(p))) {
							return condTerm;
						}

					}
				}

			}
		}
		return -1;
	}

	public static boolean checkConditionalAndTense(List<Token> clauseTokens) {
		return findConditionalAndTenseToken(clauseTokens) != -1;
	}

	private static boolean isPresentPerfect(Integer condTerm, List<Token> clauseTokens) {

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

	private static boolean isSimpleTenseWithPronoun(Token nextToken, Integer condTerm, List<Token> clauseTokens,
			boolean acceptItPronoun) {

		// is next token a pronoun or a WDT (e.g., which, that, etc), but not
		// the "it" pronoun (if acceptItPronoun=false)?
		if ((nextToken.getGeneralPos().equals("PRP") || nextToken.getPos().equals("WDT")
				|| nextToken.getLemma().equals("i") || nextToken.getLemma().equals("anyone"))
				&& (acceptItPronoun || !nextToken.getLemma().equals("it"))) {

			if (condTerm + 2 < clauseTokens.size()) {

				final Token nextToken2 = clauseTokens.get(condTerm + 2);
				if (isSimpleTense(condTerm, nextToken2, clauseTokens)) {
					return true;
				}

			}
		}

		return false;
	}

	// this method checks for simple present or past tense
	private static boolean isSimpleTense(Integer condTerm, Token nextToken2, List<Token> clauseTokens) {
		// case: "when I run..."
		if ((nextToken2.getPos().equals("VBP") || nextToken2.getPos().equals("VBZ") || nextToken2.getPos().equals("VB")
				|| nextToken2.getPos().equals("VBD") || nextToken2.getPos().equals("VBN"))
				&& !SentenceUtils.lemmasContainToken(EXCLUDED_VERBS, nextToken2)) {
			return true;

		} else
		// there is an adverb before the verb
		// case: "when I simply run..."
		if (nextToken2.getPos().equals("RB")) {
			if (condTerm + 3 < clauseTokens.size()) {
				final Token nextToken3 = clauseTokens.get(condTerm + 3);
				if ((nextToken3.getPos().equals("VBP") || nextToken3.getPos().equals("VBZ")
						|| nextToken3.getPos().equals("VB") || nextToken2.getPos().equals("VBD")
						|| nextToken2.getPos().equals("VBN"))
						&& !SentenceUtils.lemmasContainToken(EXCLUDED_VERBS, nextToken3)) {
					return true;
				}
			}
		} else
		// case: When I do a session.save( parent )...
		if ((nextToken2.getPos().equals("VBP") || nextToken2.getPos().equals("VBZ") || nextToken2.getPos().equals("VB"))
				&& nextToken2.getLemma().equals("do") 
				//
				&& (condTerm + 1 < clauseTokens.size()) && !clauseTokens.get(condTerm + 1).getGeneralPos().equals("VB") 
				// avoid: when I do not ... 
				&& (condTerm + 3 < clauseTokens.size()) && !clauseTokens.get(condTerm + 3).getLemma().equals("not")
				) {
			return true;
		}
		return false;
	}
}
