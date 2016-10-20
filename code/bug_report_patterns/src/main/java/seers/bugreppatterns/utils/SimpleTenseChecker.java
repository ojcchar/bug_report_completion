package seers.bugreppatterns.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * @author ojcch
 *
 */
public class SimpleTenseChecker {

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

	public static final Set<String> DEFAULT_PRONOUN_POS = JavaUtils.getSet("PRP", "NN");
	public static final Set<String> DEFAULT_PRONOUN_LEMMAS = JavaUtils.getSet("i");

	public SimpleTenseChecker(Set<String> partOfSpeeches, Set<String> undetectedVerbs, Set<String> verbsToAvoid) {
		this(partOfSpeeches, undetectedVerbs, verbsToAvoid, DEFAULT_PRONOUN_POS, DEFAULT_PRONOUN_LEMMAS);
	}

	public SimpleTenseChecker(Set<String> partOfSpeeches, Set<String> undetectedVerbs) {
		this(partOfSpeeches, undetectedVerbs, null, DEFAULT_PRONOUN_POS, DEFAULT_PRONOUN_LEMMAS);

	}

	public SimpleTenseChecker(Set<String> partOfSpeeches, Set<String> undetectedVerbs, Set<String> verbsToAvoid,
			Set<String> pronounsGnralPOS, Set<String> pronounsLemmas) {
		this.partOfSpeeches = partOfSpeeches == null ? new HashSet<>() : partOfSpeeches;
		this.undetectedVerbs = undetectedVerbs == null ? new HashSet<>() : undetectedVerbs;
		this.verbsToAvoid = verbsToAvoid == null ? new HashSet<>() : verbsToAvoid;
		this.pronounsGnralPOS = pronounsGnralPOS == null ? new HashSet<>() : pronounsGnralPOS;
		this.pronounsLemmas = pronounsLemmas == null ? new HashSet<>() : pronounsLemmas;
		// this.checkForStartingVerb = checkForStartingVerb;
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
				}
			}

		}
		return false;
	}

	private boolean checkForSubject(Token prevToken) {
		return pronounsGnralPOS.stream().anyMatch(pos -> prevToken.getGeneralPos().equals(pos))
				|| pronounsLemmas.stream().anyMatch(lemma -> prevToken.getLemma().equalsIgnoreCase(lemma));
	}

	private List<Integer> findVerbsInTense(List<Token> tokens) {

		List<Integer> idxs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (checkForVerb(token)) {
				idxs.add(i);
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
