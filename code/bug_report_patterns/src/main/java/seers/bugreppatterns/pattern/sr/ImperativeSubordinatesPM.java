package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;

public class ImperativeSubordinatesPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
		if (clauses.isEmpty()) {
			return 0;
		}

		// -----------------------------

		int idxImpClause = 0;
		boolean isImperative = SentenceUtils
				.isImperativeSentence(clauses.get(idxImpClause));
		if (!isImperative) {
			idxImpClause++;
			if (idxImpClause < clauses.size()) {
				isImperative = SentenceUtils
						.isImperativeSentence(clauses.get(idxImpClause));
				if (!isImperative) {
					return 0;
				}
			}
		}

		// -----------------------------

		if (clauses.size() > idxImpClause + 1) {
			int numCl = checkPresentOrActionClauses(
					clauses.subList(idxImpClause + 1, clauses.size()));
			if (numCl != 0) {
				return 1;
			}
		}

		return 0;
	}

	private int checkPresentOrActionClauses(List<Sentence> clauses) {
		int num = 0;
		for (Sentence sentence : clauses) {
			if (ActionsPresentPM.isSimplePresentSentence(sentence) || SentenceUtils.isImperativeSentence(sentence)) {
				num++;
			}
		}
		return num;
	}

}
