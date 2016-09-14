package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ImperativeSubordinatesPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		// -----------------------------

		List<List<Token>> clauses = SentenceUtils.extractClauses(tokens);
		if (clauses.isEmpty()) {
			return 0;
		}

		// -----------------------------

		int idxImpClause = 0;
		boolean isImperative = SentenceUtils.isImperativeSentence(clauses.get(idxImpClause));
		if (!isImperative) {
			idxImpClause++;
			isImperative = SentenceUtils.isImperativeSentence(clauses.get(idxImpClause));
			if (!isImperative) {
				return 0;
			}
		}

		// -----------------------------

		if (clauses.size() > idxImpClause + 1) {
			int numCl = checkPresentOrActionClauses(clauses.subList(idxImpClause + 1, clauses.size()));
			if (numCl != 0) {
				return 1;
			}
		}

		return 0;
	}

	private int checkPresentOrActionClauses(List<List<Token>> clauses) {
		int num = 0;
		for (List<Token> clause : clauses) {
			Sentence sentence = new Sentence("0", clause);
			int num2 = ActionsPresentPM.isActionInPresent(sentence, true);
			boolean isImperative = SentenceUtils.isImperativeSentence(clause);
			if (isImperative) {
				num2++;
			}

			num += (num2 != 0 ? 1 : 0);
		}
		return num;
	}

}
