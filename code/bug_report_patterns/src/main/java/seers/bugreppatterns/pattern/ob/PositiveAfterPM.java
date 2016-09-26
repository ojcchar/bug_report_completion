package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class PositiveAfterPM extends ObservedBehaviorPatternMatcher {

	public final static Set<String> AFTER = JavaUtils.getSet("after");

	private final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new VerbToBeNegativePM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new LeadsToNegativePm(), new ErrorNounPhrasePM(),
			new ErrorTermSubjectPM(), new NoNounPM(), new NounNotPM(), new ProblemIsPM(), new VerbToBeNegativePM(),
			new VerbNoPM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		for (Sentence ss : SentenceUtils.breakByParenthesis(sentence)) {
			List<Token> tokens = ss.getTokens();
			// check for after

			for (Integer afterIndex : findAfters(tokens)) {

				if (afterIndex != -1 && afterIndex > 0) {
					Sentence first = new Sentence(ss.getId(), tokens.subList(0, afterIndex));
					// Sentence second = new Sentence(sentence.getId(),
					// tokens.subList(afterIndex + 1, tokens.size()));
					List<Token> tokensFirst = first.getTokens();
					// List<Token> tokensSecond = second.getTokens();

					// check that the first part is not EB modal
					if (!isEBModal(tokensFirst)) {
						if (!isNegative(first)) {
							return 1;
						}
					}

				}
			}
		}
		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}

	private List<Integer> findAfters(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(AFTER, tokens);
	}

}
