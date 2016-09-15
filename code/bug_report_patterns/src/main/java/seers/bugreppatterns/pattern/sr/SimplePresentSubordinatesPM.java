package seers.bugreppatterns.pattern.sr;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SimpleTenseChecker;
import seers.textanalyzer.entity.Sentence;

public class SimplePresentSubordinatesPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		SimpleTenseChecker checker = new SimpleTenseChecker(ActionsPresentPM.POS, null,
				ActionsPresentPM.EXCLUDED_VERBS);
		int numClauses = checker.countNumClauses(sentence);
		if (numClauses > 1) {
			return 1;
		}
		return 0;
	}

}
