package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ButNegativePM extends ObservedBehaviorPatternMatcher {

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new NoLongerPM(), new VerbErrorPM(), new ThereIsNoPM(), new NegativeAdjOrAdvPM(), new UnableToPM(),
			new VerbNoPM(), new ProblemInPM(), new NoNounPM(), new ErrorTermSubjectPM(), new ErrorNounPhrasePM(),
			new NothingHappensPM() };

	final static String[] NEGATIVE_TERMS = { "no", "nothing", "not", "never" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> subSentences = findSubSentences(sentence, findPunctuation(sentence.getTokens()));

		for (Sentence subSentence : subSentences) {
			List<Token> subTokens = subSentence.getTokens();
			List<Integer> buts = findLemmasInTokens(CONTRAST_TERMS, subTokens);

			for (Integer but : buts) {

				if (but + 1 < subTokens.size()) {
					Token nextToken = subTokens.get(but + 1);
					if (Arrays.stream(NEGATIVE_TERMS).anyMatch(t -> nextToken.getLemma().equals(t))) {
						return 1;
					}

					Sentence sentence2 = new Sentence(sentence.getId(), subTokens.subList(but + 1, subTokens.size()));
					if (isNegative(sentence2)) {
						return 1;
					}

				}

			}
		}

		return 0;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
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
