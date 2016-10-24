package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.bugreppatterns.utils.SimpleTenseChecker;
import seers.textanalyzer.entity.Sentence;

public class SimplePresentSubordinatesPM extends StepsToReproducePatternMatcher {

	public final static Set<String> EXCLUDED_VERBS = JavaUtils.getSet("be", "seem", "have", "volunteer", "want",
			"experience", "see", "can");

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

		SimpleTenseChecker checker = SimpleTenseChecker.createPresentChecker(EXCLUDED_VERBS);
		int numClauses = checker.countNumClauses(sentence);

		if (numClauses > 0) {
			List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
			
			System.out.println(numClauses + " - "+ clauses.size());
			
			return ((float) numClauses) / clauses.size() >= 0.5F ? 1 : 0;
		}
		return 0;
	}

}
