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
			"experience", "see");
	public static final Set<String> DEFAULT_PRONOUN_LEMMAS = JavaUtils.getSet("i", "we");
	public static final Set<String> DEFAULT_PRONOUN_POS = JavaUtils.getSet("PRP");

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

		SimpleTenseChecker checker = new SimpleTenseChecker(ActionsPresentPM.POS, ActionsPresentPM.UNDETECTED_VERBS,
				EXCLUDED_VERBS, DEFAULT_PRONOUN_POS, DEFAULT_PRONOUN_LEMMAS);
		int numClauses = checker.countNumClauses(sentence);

		if (numClauses > 0) {
			List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
			return ((float) numClauses) / clauses.size() >= 0.5F ? 1 : 0;
		}
		return 0;
	}

}
