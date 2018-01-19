package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ButPM extends ObservedBehaviorPatternMatcher {

	private static final Set<String> PUNCTUATION = JavaUtils.getSet(";", "--");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> subSentences = SentenceUtils.findSubSentences(sentence, findPunctuation(sentence.getTokens()));

		for (Sentence subSentence : subSentences) {
			List<Token> subTokens = subSentence.getTokens();
			List<Integer> buts = SentenceUtils.findLemmasInTokens(CONTRAST_TERMS, subTokens);

			PatternMatcher pmw = new WorksButPM();
			int match1 = pmw.matchSentence(subSentence);
			if (match1 == 0) {
				for (Integer but : buts) {
					Sentence sentence2 = new Sentence(subSentence.getId(),
							subTokens.subList(but + 1, subTokens.size()));
					if (!isNegative(sentence2)) {
						return 1;
					}
				}
			}
		}

		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, ButNegativePM.NEGATIVE_PMS);
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		List<Integer> symbols = SentenceUtils.findLemmasInTokens(PUNCTUATION, tokens);
		if (symbols.size() - 1 >= 0 && symbols.get(symbols.size() - 1) == tokens.size() - 1) {
			return symbols.subList(0, symbols.size() - 1);
		}
		return symbols;
	}

}
