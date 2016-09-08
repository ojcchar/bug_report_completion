package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ButNegativePM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new ThereIsNoPM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(),
			new ErrorNounPhrasePM() };

	final static String[] NEGATIVE_TERMS = { "no", "nothing", "not", "never" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens2 = sentence.getTokens();
		List<Integer> buts = findTermsInTokens(CONTRAST_TERMS, tokens2);
		for (Integer but : buts) {

			if (but + 1 < tokens2.size()) {
				Token nextToken = tokens2.get(but + 1);
				if (Arrays.stream(NEGATIVE_TERMS).anyMatch(t -> nextToken.getLemma().equals(t))) {
					return 1;
				}

				Sentence sentence2 = new Sentence(sentence.getId(), tokens2.subList(but + 1, tokens2.size()));
				if(isNegative(sentence2)) {
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
