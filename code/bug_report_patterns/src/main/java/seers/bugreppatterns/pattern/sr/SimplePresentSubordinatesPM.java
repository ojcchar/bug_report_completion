package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.bugreppatterns.utils.SimpleTenseChecker;
import seers.textanalyzer.entity.Sentence;

public class SimplePresentSubordinatesPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		SimpleTenseChecker checker = new SimpleTenseChecker(ActionsPresentPM.POS, ActionsPresentPM.UNDETECTED_VERBS,
				ActionsPresentPM.EXCLUDED_VERBS);
		int numClauses = checker.countNumClauses(sentence);

		if (numClauses > 0) {
			List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
			return ((float) numClauses) / clauses.size() >= 0.5F ? 1 : 0;
		}
		return 0;
	}

}
