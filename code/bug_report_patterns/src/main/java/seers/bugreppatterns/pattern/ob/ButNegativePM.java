package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ButNegativePM extends ObservedBehaviorPatternMatcher {

	public final static ObservedBehaviorPatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new VerbToBeNegativePM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM(),
			new NothingHappensPM() };

	final static Set<String> NEGATIVE_TERMS = JavaUtils.getSet("no", "nothing", "not", "never");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> subTokens = sentence.getTokens();
		List<Integer> buts = SentenceUtils.findLemmasInTokens(CONTRAST_TERMS, subTokens);

		for (Integer but : buts) {

			if (but + 1 < subTokens.size()) {
				Token nextToken = subTokens.get(but + 1);
				if (SentenceUtils.lemmasContainToken(NEGATIVE_TERMS, nextToken)) {
					return 1;
				}

				Sentence sentence2 = new Sentence(sentence.getId(), subTokens.subList(but + 1, subTokens.size()));
				if (isNegative(sentence2)) {
					return 1;
				}

			}

		}

		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}

}
