package seers.bugreppatterns.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seers.appcore.utils.JavaUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * @author ojcch
 *
 */
public class SimpleTenseChecker {

	public final static Set<String> PRESENT_POS = JavaUtils.getSet("VBP", "VBZ", "VB");
	public final static Set<String> PRESENT_EXCLUDED_VERBS = JavaUtils.getSet("be", "seem", "can");
	public static final Set<String> PRESENT_UNDETECTED_VERBS = JavaUtils.getSet("set", "put", "close", "cache", "scale",
			"change", "type");

	// {
	// PRESENT_UNDETECTED_VERBS.addAll(SentenceUtils.UNDETECTED_VERBS);
	// }

	public final static Set<String> PAST_POS = JavaUtils.getSet("VBD", "VBN");
	public final static Set<String> PAST_UNDETECTED_VERBS = JavaUtils.getSet("set", "put");

	public static final Set<String> DEFAULT_PRONOUN_LEMMAS = JavaUtils.getSet("i", "we");
	public static final Set<String> DEFAULT_PRONOUN_POS_LEMMA = JavaUtils.getSet("NN-user", "PRP-i", "PRP-we"
			,
			"PRP-you"
			);
	public static final Set<String> DEFAULT_PRONOUN_POS = JavaUtils.getSet("PRP");

	private boolean isCheckerPresent;

	/**
	 * POSs of the verbs, to indicate the tense: present (VBP, VBZ) or past
	 * (VBD, VBN)
	 */
	private Set<String> partOfSpeeches;
	/**
	 * Verbs incorrectly detected/labeled
	 */
	private Set<String> undetectedVerbs;
	/**
	 * Set of verbs to avoid matching
	 */
	private Set<String> verbsToAvoid;
	// /**
	// * Check or not for sentence starting with verbs (i.e., missing the
	// subject)
	// */
	// private boolean checkForStartingVerb;

	private Set<String> pronounsGnralPOS;
	private Set<String> pronounsLemmas;
	private Set<String> pronounsPOSLemmas;

	public static SimpleTenseChecker createPresentChecker(Set<String> verbsToAvoid) {
		SimpleTenseChecker checker = new SimpleTenseChecker(PRESENT_POS, PRESENT_UNDETECTED_VERBS, verbsToAvoid, null,
				DEFAULT_PRONOUN_LEMMAS, DEFAULT_PRONOUN_POS_LEMMA);
		checker.isCheckerPresent = true;
		return checker;
	}

	public static SimpleTenseChecker createPresentCheckerOnlyPronouns(Set<String> verbsToAvoid) {
		SimpleTenseChecker checker = new SimpleTenseChecker(PRESENT_POS, PRESENT_UNDETECTED_VERBS, verbsToAvoid,
				DEFAULT_PRONOUN_POS, DEFAULT_PRONOUN_LEMMAS, DEFAULT_PRONOUN_POS_LEMMA);
		checker.isCheckerPresent = true;
		return checker;
	}

	public static SimpleTenseChecker createPresentCheckerPronounsAndNouns(Set<String> verbsToAvoid) {
		SimpleTenseChecker checker = new SimpleTenseChecker(PRESENT_POS, PRESENT_UNDETECTED_VERBS, verbsToAvoid,
				JavaUtils.getSet("PRP", "NN"), DEFAULT_PRONOUN_LEMMAS, DEFAULT_PRONOUN_POS_LEMMA);
		checker.isCheckerPresent = true;
		return checker;
	}

	public static SimpleTenseChecker createPastChecker(Set<String> verbsToAvoid) {
		SimpleTenseChecker checker = new SimpleTenseChecker(PAST_POS, PAST_UNDETECTED_VERBS, verbsToAvoid, null,
				DEFAULT_PRONOUN_LEMMAS, DEFAULT_PRONOUN_POS_LEMMA);
		checker.isCheckerPresent = false;
		return checker;
	}

	public static SimpleTenseChecker createPastCheckerOnlyPronouns(Set<String> verbsToAvoid) {
		SimpleTenseChecker checker = new SimpleTenseChecker(PAST_POS, PAST_UNDETECTED_VERBS, verbsToAvoid,
				DEFAULT_PRONOUN_POS, DEFAULT_PRONOUN_LEMMAS, DEFAULT_PRONOUN_POS_LEMMA);
		checker.isCheckerPresent = false;
		return checker;
	}
	
	public static SimpleTenseChecker createPastCheckerOnlySpecificPronouns(Set<String> verbsToAvoid, Set<String> pronounsPosAllowed) {
		SimpleTenseChecker checker = new SimpleTenseChecker(PAST_POS, PAST_UNDETECTED_VERBS, verbsToAvoid,
				JavaUtils.getSet(""), DEFAULT_PRONOUN_LEMMAS, pronounsPosAllowed);
		checker.isCheckerPresent = false;
		return checker;
	}

	private SimpleTenseChecker(Set<String> partOfSpeeches, Set<String> undetectedVerbs, Set<String> verbsToAvoid,
			Set<String> pronounsGnralPOS, Set<String> pronounsLemmas, Set<String> pronounsPosLemmas) {
		this.partOfSpeeches = partOfSpeeches == null ? new HashSet<>() : partOfSpeeches;
		this.undetectedVerbs = undetectedVerbs == null ? new HashSet<>() : undetectedVerbs;
		this.verbsToAvoid = verbsToAvoid == null ? new HashSet<>() : verbsToAvoid;
		this.pronounsGnralPOS = pronounsGnralPOS == null ? new HashSet<>() : pronounsGnralPOS;
		this.pronounsLemmas = pronounsLemmas == null ? new HashSet<>() : pronounsLemmas;
		this.pronounsPOSLemmas = pronounsPosLemmas == null ? new HashSet<>() : pronounsPosLemmas;
	}

	public int countNumClauses(Sentence sentence) {

		int numClauses = 0;

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		// find the first clause "i performed" or "i then performed"
		int idxFirstClause = findFirstClauseInTense(clauses);

		// first clause found
		if (idxFirstClause != -1) {
			numClauses++;

			// check the remaining clauses such as "deleted...", this is
			// done to match sentences such as "i performed..., deleted,
			// and create...."
			List<Sentence> remainingClauses = clauses.subList(idxFirstClause + 1, clauses.size());
			for (Sentence clause : remainingClauses) {
				if (checkClauseInTense(clause) || checkClauseInTenseWithPronoun(clause)) {
					numClauses++;
				}
			}
		} else {
			// if (checkForStartingVerb) {
			//
			// // check for sentence starting with the verb in the sentence
			// for (Sentence clause : clauses) {
			// if (checkClauseInTense(clause)) {
			// numClauses++;
			// }
			// }
			// }
		}
		return numClauses;
	}

	private boolean checkClauseInTense(Sentence clause) {

		List<Token> tokens = clause.getTokens();
		if (tokens.size() < 2) {
			return false;
		}

		Token token = tokens.get(0);

		// case: perform(ed)
		if (checkForVerb(token)) {
			return true;

		} else {

			// case: simply perform(ed)
			if (tokens.size() > 2) {

				Token nextToken = tokens.get(1);

				if (token.getGeneralPos().equals("RB")
						// typo: then -> than
						|| token.getLemma().equals("than")) {
					if (checkForVerb(nextToken)) {
						return true;

					}

				}

			}

		}
		return false;
	}

	private int findFirstClauseInTense(List<Sentence> clauses) {
		for (int i = 0; i < clauses.size(); i++) {
			Sentence sentence = clauses.get(i);

			boolean isValid = checkClauseInTenseWithPronoun(sentence);
			if (isValid) {
				return i;
			}

		}
		return -1;
	}

	private boolean checkClauseInTenseWithPronoun(Sentence sentence) {

		List<Token> tokens = sentence.getTokens();
		List<Integer> verbs = findVerbsInTense(tokens);

		for (Integer verbIdx : verbs) {

			if (verbIdx - 1 >= 0) {

				boolean isNegative = verbIdx + 1 < tokens.size() ? tokens.get(verbIdx + 1).getLemma().equals("not")
						: false;
				boolean isPerfectTense = tokens.get(verbIdx).getLemma().equals("have") && verbIdx + 1 < tokens.size()
						? tokens.get(verbIdx + 1).getGeneralPos().equals("VB") : false;

				Token prevToken = tokens.get(verbIdx - 1);

				// case: I/we/this perform(s|ed)
				// avoid negatives: i did not
				if (checkForSubject(prevToken) && !isNegative && !isPerfectTense) {
					return true;

					// case: I/we then tr(y|ied)
				} else if (prevToken.getGeneralPos().equals("RB")
				// typo: then -> than
				// || prevToken.getLemma().equals("than")
				) {
					if (verbIdx - 2 >= 0) {
						Token prevToken2 = tokens.get(verbIdx - 2);
						// avoid negatives: i then did not
						if (checkForSubject(prevToken2) && !isNegative && !isPerfectTense) {
							return true;
						}
					}

					// case I/we do tr(y|ied)
				} else if (prevToken.getLemma().equals("do") && verbIdx - 2 >= 0) {

					Token prevToken2 = tokens.get(verbIdx - 2);
					if (checkForSubject(prevToken2) && !isNegative && !isPerfectTense) {
						return true;
					}
				}
			}

		}
		return false;
	}

	private boolean checkForSubject(Token token) {
		String generalPos = token.getGeneralPos();
		String lemma = token.getLemma().toLowerCase();
		return pronounsGnralPOS.stream().anyMatch(pos -> generalPos.equals(pos))
				|| pronounsLemmas.stream().anyMatch(lem -> lemma.equals(lem))
				|| pronounsPOSLemmas.stream().anyMatch(posLemma -> posLemma.equals(generalPos + "-" + lemma));
	}

	private List<Integer> findVerbsInTense(List<Token> tokens) {

		List<Integer> idxs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);

			if (checkForVerb(token)) {
				// no auxiliary
				if (token.getLemma().equals("do") && i + 1 < tokens.size()) {
					Token nextToken = tokens.get(i + 1);
					if (!(nextToken.getGeneralPos().equals("VB")
							|| undetectedVerbs.stream().anyMatch(verb -> nextToken.getLemma().equals(verb)))) {
						idxs.add(i);
					}

				} else {
					idxs.add(i);
				}
			} else {

				// case: "I did execute..."
				// if the sentence is in past i should check this token for
				// present tense and the previous token for past tense and the
				// verb "do"
				if (!isCheckerPresent) {
					if (i - 1 >= 0 && (PRESENT_POS.stream().anyMatch(pos -> token.getPos().equals(pos))
							|| undetectedVerbs.stream().anyMatch(verb -> token.getLemma().equals(verb)))) {
						Token previousToken = tokens.get(i - 1);
						if (previousToken.getLemma().equals("do") && checkForVerb(previousToken)) {
							idxs.add(i);
						}
					}
				}
			}
		}
		return idxs;
	}

	private boolean checkForVerb(Token token) {
		return (partOfSpeeches.stream().anyMatch(pos -> token.getPos().equals(pos))
				|| undetectedVerbs.stream().anyMatch(verb -> token.getLemma().equals(verb)))
				&& verbsToAvoid.stream().noneMatch(verb -> token.getLemma().equals(verb));
	}

}
