package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ActionsSeparatorPM extends StepsToReproducePatternMatcher {

	public final static String[] SEPARATORS = { "-" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		List<List<Token>> clauses = extractClauses(tokens);

		int numClauses = 0;
		for (List<Token> clause : clauses) {
			numClauses += checkClause(clause);
		}

		if (numClauses > 1) {
			return 1;
		}

		return 0;
	}

	private int checkClause(List<Token> clause) {
		if (clause.size() > 1) {
			if (ActionsPastPM.isAnAction(clause.get(0), clause.get(1))) {
				return 1;
			}
		}
		return 0;
	}

	private List<List<Token>> extractClauses(List<Token> tokens) {
		List<List<Token>> clauses = new ArrayList<>();

		int ini = 0;

		for (int i = ini; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals("-")) {
				if (ini != i) {
					clauses.add(tokens.subList(ini, i));
				}
				ini = i + 1;
			}
		}

		if (ini != tokens.size() - 1) {
			clauses.add(tokens.subList(ini, tokens.size()));
		}

		return clauses;
	}

}
