package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.sr.ActionsPresentPM;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.bugreppatterns.utils.SimpleTenseChecker;
import seers.textanalyzer.entity.Sentence;

public class SimplePresentPM extends ObservedBehaviorPatternMatcher {

	private final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new VerbToBeNegativePM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM(),
			new NoLongerPM(), new PassiveVoicePM() };

	private final static Set<String> EXCLUDED_VERBS = JavaUtils.getSet();

	private final static Set<String> FORBIDDEN_TERMS = CONDITIONAL_TERMS;
	{
		FORBIDDEN_TERMS.addAll(CONTRAST_TERMS);
		FORBIDDEN_TERMS.addAll(JavaUtils.getSet("after", "because"));
	}

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> subSentences = SentenceUtils.breakByParenthesis(sentence);
		for (Sentence subSentence : subSentences) {
			
			SimpleTenseChecker checker = new SimpleTenseChecker(ActionsPresentPM.POS, SentenceUtils.UNDETECTED_VERBS,
					SimplePresentPM.EXCLUDED_VERBS);
			int numClauses = checker.countNumClauses(subSentence);

			if (numClauses > 0 && !isNegative(subSentence)
					&& !SentenceUtils.sentenceContainsAnyLemmaIn(subSentence, FORBIDDEN_TERMS)) {
				return 1;
			}
		}

		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}

}
