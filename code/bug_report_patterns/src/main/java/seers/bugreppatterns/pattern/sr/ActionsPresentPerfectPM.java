package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for S_SR_ACTIONS_PRESENT_PERFECT.
 */
public class ActionsPresentPerfectPM extends StepsToReproducePatternMatcher {

	static final Set<String> DEFAULT_PRONOUN_LEMMAS = JavaUtils.getSet("i", "we");
	public final static Set<String> POS = JavaUtils.getSet("VBN", "VBD", "VB");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Integer> condTerms = SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, sentence.getTokens());
		if (!condTerms.isEmpty()) {
			return 0;
		}

		boolean question = SentenceUtils.isQuestion(sentence);
		if (question) {
			return 0;
		}

		int numClauses = countNumClauses(sentence);

		if (numClauses > 0) {
			List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
			return ((float) numClauses) / clauses.size() >= 0.5F ? 1 : 0;
		}
		return 0;

	}

	public int countNumClauses(Sentence sentence) {

		int numClauses = 0;

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		// find the first clause "i have run"
		int idxFirstClause = findFirstClauseInTense(clauses);

		// first clause found
		if (idxFirstClause != -1) {
			numClauses++;

			// check the remaining clauses such as "removed...", this is
			// done to match sentences such as "i have created..., run,
			// and removed...."

			// FIXME: check for passive voice clauses
			List<Sentence> remainingClauses = clauses.subList(idxFirstClause + 1, clauses.size());
			for (Sentence clause : remainingClauses) {
				if (checkClauseInTense(clause) || checkClauseInTenseWithPronoun(clause)) {
					numClauses++;
				}
			}
		}
		return numClauses;
	}

	private int findFirstClauseInTense(List<Sentence> clauses) {
		for (int i = 0; i < clauses.size(); i++) {
			Sentence clause = clauses.get(i);

			boolean isValid = checkClauseInTenseWithPronoun(clause);
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

			if (verbIdx - 2 >= 0) {

				Token prevToken = tokens.get(verbIdx - 1);
				Token prevToken2 = tokens.get(verbIdx - 2);

				boolean isNegative = prevToken.getLemma().equals("not");

				// case: I have worked
				// avoid negatives: I have not worked
				if (checkForSubject(prevToken2) && !isNegative && checkForAuxiliary(prevToken)) {
					return true;

					// case: I have then tried
				} else if (prevToken.getGeneralPos().equals("RB")) {
					if (verbIdx - 3 >= 0) {
						Token prevToken3 = tokens.get(verbIdx - 3);
						if (checkForSubject(prevToken3) && !isNegative && checkForAuxiliary(prevToken2)) {
							return true;
						}
					}
				}
			}

		}
		return false;
	}

	private boolean checkForAuxiliary(Token prevToken) {
		return prevToken.getLemma().equals("have") && (prevToken.getPos().equals("VBP")
				|| prevToken.getPos().equals("VBZ") || prevToken.getPos().equals("VBD"));
	}

	private boolean checkClauseInTense(Sentence clause) {

		List<Token> tokens = clause.getTokens();
		if (tokens.size() < 2) {
			return false;
		}

		Token token = tokens.get(0);

		// case: performed
		if (checkForVerb(token)) {
			return true;

		} else {

			// case: simply performed
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

	private boolean checkForSubject(Token token) {
		return (token.getGeneralPos().equals("PRP")
				&& DEFAULT_PRONOUN_LEMMAS.stream().anyMatch(lemma -> token.getLemma().equalsIgnoreCase(lemma)))
				|| token.getGeneralPos().equals("NN");
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
		return POS.stream().anyMatch(pos -> token.getPos().equals(pos))
				&& SimplePresentSubordinatesPM.EXCLUDED_VERBS.stream().noneMatch(verb -> token.getLemma().equals(verb));
	}
}
