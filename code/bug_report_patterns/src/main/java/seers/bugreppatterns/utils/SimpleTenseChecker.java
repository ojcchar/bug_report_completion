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

	public SimpleTenseChecker(Set<String> partOfSpeeches, Set<String> undetectedVerbs) {
		this(partOfSpeeches, undetectedVerbs, null);

	}

	public SimpleTenseChecker(Set<String> partOfSpeeches, Set<String> undetectedVerbs, Set<String> verbsToAvoid) {
		this.partOfSpeeches = partOfSpeeches == null ? new HashSet<>() : partOfSpeeches;
		this.undetectedVerbs = undetectedVerbs == null ? new HashSet<>() : undetectedVerbs;
		this.verbsToAvoid = verbsToAvoid == null ? new HashSet<>() : verbsToAvoid;
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

		if (clause.getTokens().size() < 2) {
			return false;
		}

		Token token = clause.getTokens().get(0);

		// case: perform(ed)
		if (checkForVerb(token)) {
			return true;
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
				} else if (prevToken.getGeneralPos().equals("RB")) {
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
		return prevToken.getGeneralPos().equals("PRP") || prevToken.getGeneralPos().equals("NN")
				|| prevToken.getLemma().equals("i");
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
