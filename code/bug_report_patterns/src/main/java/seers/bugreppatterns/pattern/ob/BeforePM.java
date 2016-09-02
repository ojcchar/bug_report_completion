package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class BeforePM extends ObservedBehaviorPatternMatcher {

	private static String[] BEFORE = new String[] { "before" };

	private static String[] PUNCTUATION = new String[] { ",", ":", "_", "-", "-lrb-", "-rrb-", ".", ";" };

	private static String[] PRESENT_TENSE_VERBS = new String[] { "crashes", "builds", "returns" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		List<Integer> beforeIndexes = findLemmasInTokens(BEFORE, tokens);

		if (!beforeIndexes.isEmpty()) {
			for (Integer beforeIndex : beforeIndexes) {
				List<Token> previousSentence = findPreviousSentence(tokens, beforeIndex);
				List<Token> nextSentence = findNextSentence(tokens, beforeIndex);

				if (isPresentTense(previousSentence) && !isPronounTrySentence(previousSentence)
						&& containsVerb(nextSentence)) {
					return 1;
				}
			}
		}

		return 0;
	}

	private List<Token> findPreviousSentence(List<Token> tokens, Integer beforeIndex) {
		int startSentenceIndex = 0;
		boolean outsidePar = false;
		for (int i = beforeIndex; i >= 0; i--) {
			Token current = tokens.get(i);
			if (Arrays.stream(PUNCTUATION).anyMatch(t -> current.getLemma().equals(t))) {
				if (current.getLemma().equals("-") && tokens.get(i + 1).getLemma().equals(">")) {
					continue;
				} else if (current.getLemma().equals("-rrb-")) {
					outsidePar = true;
					continue;
				} else if (current.getLemma().equals("-lrb-") && outsidePar) {
					outsidePar = false;
					continue;
				}
				startSentenceIndex = i;
				break;
			}
		}
		return tokens.subList(startSentenceIndex, beforeIndex);
	}

	private List<Token> findNextSentence(List<Token> tokens, Integer beforeIndex) {
		int endSentenceIndex = tokens.size();
		boolean outsidePar = false;
		for (int i = beforeIndex; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if (Arrays.stream(PUNCTUATION).anyMatch(t -> current.getLemma().equals(t))) {
				if (current.getLemma().equals("-") && i + 1 < tokens.size()
						&& tokens.get(i + 1).getLemma().equals(">")) {
					continue;
				} else if (current.getLemma().equals("-lrb-")) {
					outsidePar = true;
					continue;
				} else if (current.getLemma().equals("-rrb-") && outsidePar) {
					outsidePar = false;
					continue;
				}
				endSentenceIndex = i;
				break;
			}
		}
		return tokens.subList(beforeIndex + 1, endSentenceIndex);
	}

	private boolean isPresentTense(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if (current.getPos().equals("VBP") || current.getPos().equals("VBZ")
					|| (i - 1 >= 0 && Arrays.stream(PRESENT_TENSE_VERBS).anyMatch(t -> current.getWord().equals(t))
							&& tokens.get(i - 1).getGeneralPos().equals("NN"))) {
				return true;
			}
		}
		return false;
	}

	private boolean isPronounTrySentence(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if (current.getGeneralPos().equals("PRP") && !current.getLemma().equals("it")
					&& !current.getLemma().equals("they")) {
				int j = i + 1;
				while (j < tokens.size() && tokens.get(j).getGeneralPos().equals("VB")) {
					if (tokens.get(j).getLemma().equals("try")) {
						return true;
					}
					j++;
				}
			}
		}
		return false;
	}

	private boolean containsVerb(List<Token> tokens) {
		for (Token current : tokens) {
			if (current.getGeneralPos().equals("VB")
					|| Arrays.stream(PRESENT_TENSE_VERBS).anyMatch(t -> current.getWord().equals(t))) {
				return true;
			}
		}
		return false;
	}

}
