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
 * Created by juan on 9/20/16.
 */
public class ContinuousPresentSentencePM extends StepsToReproducePatternMatcher {

	static final Set<String> DEFAULT_PRONOUN_LEMMAS = JavaUtils.getSet("i", "we");

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

	public static int countNumClauses(Sentence sentence) {

		int numClauses = 0;

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		// find the first clause "i am running"
		int idxFirstClause = findFirstClauseInTense(clauses);

		// first clause found
		if (idxFirstClause != -1) {
			numClauses++;

			// check the remaining clauses such as "executing...", this is
			// done to match sentences such as "i am running..., executing,
			// and removing...."
			List<Sentence> remainingClauses = clauses.subList(idxFirstClause + 1, clauses.size());
			for (Sentence clause : remainingClauses) {
				if (checkClauseInTense(clause) || checkClauseInTenseWithPronoun(clause)) {
					numClauses++;
				}
			}
		}
		return numClauses;
	}

	private static int findFirstClauseInTense(List<Sentence> clauses) {
		for (int i = 0; i < clauses.size(); i++) {
			Sentence clause = clauses.get(i);

			boolean isValid = checkClauseInTenseWithPronoun(clause);
			if (isValid) {
				return i;
			}

		}
		return -1;
	}

	private static boolean checkClauseInTenseWithPronoun(Sentence sentence) {

		List<Token> tokens = sentence.getTokens();
		List<Integer> verbs = findVerbsInTense(tokens);

		for (Integer verbIdx : verbs) {

			if (verbIdx - 2 >= 0) {

				Token prevToken = tokens.get(verbIdx - 1);
				Token prevToken2 = tokens.get(verbIdx - 2);

				boolean isNegative = prevToken.getLemma().equals("not");

				// case: I am/was working
				// avoid negatives: i was not working
				if (checkForSubject(prevToken2) && !isNegative && checkForVerbTobe(prevToken)) {
					return true;

					// case: I/we then tr(y|ied)
				} else if (prevToken.getGeneralPos().equals("RB")
				// case: I am/was simply working
				) {
					if (verbIdx - 3 >= 0) {
						Token prevToken3 = tokens.get(verbIdx - 3);
						if (checkForSubject(prevToken3) && !isNegative && checkForVerbTobe(prevToken2)) {
							return true;
						}
					}
				}
			}

		}
		return false;
	}

	private static boolean checkForVerbTobe(Token prevToken) {
		return prevToken.getLemma().equals("be")
				&& (prevToken.getPos().equals("VBP") || prevToken.getPos().equals("VBD"));
	}

	private static boolean checkClauseInTense(Sentence clause) {

		List<Token> tokens = clause.getTokens();
		if (tokens.size() < 2) {
			return false;
		}

		Token token = tokens.get(0);

		// case: performing
		if (checkForVerb(token)) {
			return true;

		} else {

			// case: simply performing
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

	private static boolean checkForSubject(Token token) {
		return token.getGeneralPos().equals("PRP")
				&& DEFAULT_PRONOUN_LEMMAS.stream().anyMatch(lemma -> token.getLemma().equalsIgnoreCase(lemma));
	}

	private static List<Integer> findVerbsInTense(List<Token> tokens) {

		List<Integer> idxs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (checkForVerb(token)) {
				idxs.add(i);
			}
		}
		return idxs;
	}

	private static boolean checkForVerb(Token token) {
		return token.getPos().equals("VBG")
				&& SimplePresentSubordinatesPM.EXCLUDED_VERBS.stream().noneMatch(verb -> token.getLemma().equals(verb));
	}
}
