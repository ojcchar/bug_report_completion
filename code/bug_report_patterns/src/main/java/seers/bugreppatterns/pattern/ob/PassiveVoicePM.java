package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class PassiveVoicePM extends ObservedBehaviorPatternMatcher {

	private final static String[] AUXILIARS = { "be", "get" };

	public final static PatternMatcher[] NEGATIVE_PMS = { new NegativeAuxVerbPM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		if (isPassive(tokens) && !isEBModal(tokens) && !isNegative(sentence)) {
			return 1;
		}

		return 0;
	}

	private boolean isPassive(List<Token> tokens) {

		for (int i = 0; i < tokens.size() - 1; i++) {
			Token current = tokens.get(i);

			if (current.getGeneralPos().equals("VB")
					&& Arrays.stream(AUXILIARS).anyMatch(t -> current.getLemma().equals(t))) {
				Token next = tokens.get(i + 1);
				if (next.getPos().equals("VBN")) {
					return true;
				} else if (next.getGeneralPos().equals("NN") && i + 2 < tokens.size()) {
					next = tokens.get(i + 2);
					if (next.getPos().equals("VBN")) {
						return true;
					}
				}

			}

		}
		return false;
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}
}
