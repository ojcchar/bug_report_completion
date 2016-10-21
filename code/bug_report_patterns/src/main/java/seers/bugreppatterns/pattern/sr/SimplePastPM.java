package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.bugreppatterns.utils.SimpleTenseChecker;
import seers.textanalyzer.entity.Sentence;

/**
 * Matcher for S_SR_SIMPLE_PAST.
 */
public class SimplePastPM extends StepsToReproducePatternMatcher {

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

		SimpleTenseChecker checker = new SimpleTenseChecker(SimplePastParagraphPM.POSs,
				SimplePastParagraphPM.UNDETECTED_VERBS, SimplePresentSubordinatesPM.EXCLUDED_VERBS,
				SimplePresentSubordinatesPM.DEFAULT_PRONOUN_POS, SimplePresentSubordinatesPM.DEFAULT_PRONOUN_LEMMAS,
				SimplePresentSubordinatesPM.DEFAULT_PRONOUN_POS_LEMMA);
		int numClauses = checker.countNumClauses(sentence);

		if (numClauses > 0) {
			List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
			// System.out.println(numClauses +" - "+clauses.size() );
			return ((float) numClauses) / clauses.size() >= 0.5F ? 1 : 0;
		}
		return 0;

	}

}
