package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ButPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> subSentences = findSubSentences(sentence, findPunctuation(sentence.getTokens()));

		for (Sentence subSentence : subSentences) {
			List<Token> subTokens = subSentence.getTokens();
			List<Integer> buts = findTermsInTokens(CONTRAST_TERMS, subTokens);

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
		List<Integer> symbols = new ArrayList<>();
		for (int i = 0; i < tokens.size() - 1; i++) {
			Token token = tokens.get(i);
			if (token.getWord().equals(".") || token.getWord().equals(";") || token.getWord().equals("--")) {
				symbols.add(i);
			}
		}
		return symbols;
	}

}
