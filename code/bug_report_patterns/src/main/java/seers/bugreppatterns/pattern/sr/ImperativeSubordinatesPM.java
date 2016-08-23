package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.eb.ImperativeSentencePM;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ImperativeSubordinatesPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();
		List<List<Token>> clauses = extractClauses(tokens);

		if (clauses.isEmpty()) {
			return 0;
		}

		List<Token> clause1 = clauses.get(0);

		int match = checkFirstClause(clause1);
		if (match == 0) {
			return 0;
		}

		if (clauses.size() > 1) {
			int numCl = checkPresentorActionClauses(clauses.subList(1, clauses.size()));
			if (numCl == 0) {
				return 0;
			}
		}

		return 1;
	}

	private int checkPresentorActionClauses(List<List<Token>> clauses) {
		int num = 0;
		for (List<Token> clause : clauses) {
			Sentence sentence = new Sentence("o", clause);
			int num2 = ActionsPresentPM.isActionInPresent(sentence, true);
			Token secondToken = null;
			if (clause.size() > 1) {
				secondToken = clause.get(1);
			}
			num2 += ImperativeSentencePM.checkNormalCase(clause.get(0), secondToken);

			num += (num2 != 0 ? 1 : 0);
		}
		return num;
	}

	private int checkFirstClause(List<Token> clause) {
		Token firstToken = clause.get(0);
		Token secondToken = null;
		if (clause.size() > 1) {
			secondToken = clause.get(1);
		}

		int match = ImperativeSentencePM.checkNormalCase(firstToken, secondToken);
		if (match == 1) {
			return match;
		}

		match = ImperativeSentencePM.checkNormalCaseWithLabel(clause, ":");
		if (match == 1) {
			return match;
		}

		return ImperativeSentencePM.checkNormalCaseWithLabel(clause, ",");
	}

	private List<List<Token>> extractClauses(List<Token> tokens) {

		List<List<Token>> clauses = new ArrayList<>();
		List<Token> clause = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals("-") || token.getLemma().equals(";") || token.getLemma().equals(",")) {

				// check if the clause is short, to avoid splitting cases such
				// "In Reader, click on a post to view full post"
				if (clause.size() > 5) {
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
