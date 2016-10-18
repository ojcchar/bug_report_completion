package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ConditionalSequencePM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		// ConditionalObservedBehaviorPM pm = new
		// ConditionalObservedBehaviorPM();
		// return pm.matchSentence(sentence);

		int numValidClauses = 0;
		for (Sentence clause : clauses) {
			List<Token> clauseTokens = clause.getTokens();
			if (ConditionalObservedBehaviorPM.checkConditionalAndTense(clauseTokens)) {
				numValidClauses++;
			}

		}

		if (numValidClauses > 1) {
			return 1;
		}

		return 0;

//		return ((float) numValidClauses) / clauses.size() >= 0.5F ? 1 : 0;
	}

}
