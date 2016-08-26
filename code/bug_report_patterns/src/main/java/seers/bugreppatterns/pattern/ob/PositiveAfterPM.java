package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class PositiveAfterPM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new ThereIsNoPM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new FrequencyAdverbPM(), new LeadsToNegativePm(),
			new ErrorNounPhrasePM(), new ErrorTermSubjectPM(), new NoNounPM(), new NounNotPM(), new ProblemIsPM(),
			new ThereIsNoPM(), new VerbNoPM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		// check for after
		int afterIndex = afterIndex(tokens);

		if (afterIndex != -1 && afterIndex > 0) {
			Sentence first = new Sentence(sentence.getId(), tokens.subList(0, afterIndex));
			Sentence second = new Sentence(sentence.getId(), tokens.subList(afterIndex + 1, tokens.size()));
			List<Token> tokensFirst = first.getTokens();
			List<Token> tokensSecond = second.getTokens();

			// check that the first part is not EB modal
			if (!isEBModal(tokensFirst)) {
				if (!isNegative(first)) {
						return 1;
				}
			}

		}
		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		for (PatternMatcher pm : NEGATIVE_PMS) {
			int match = pm.matchSentence(sentence);
			if (match == 1) {
				return true;
			}
		}
		return false;
	}

	private int afterIndex(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			if (tokens.get(i).getLemma().equals("after")) {
				return i;
			}
		}
		return -1;
	}

}
