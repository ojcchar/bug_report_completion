package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class BeforePM extends ObservedBehaviorPatternMatcher {

	private static Set<String> BEFORE = JavaUtils.getSet("before");

	private static Set<String> PUNCTUATION = JavaUtils.getSet(",", ":", "_", "-", ";");

	private static Set<String> PRESENT_TENSE_VERBS = JavaUtils.getSet("crash", "build", "return", "drop", "stay");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		for (Sentence ss : SentenceUtils.breakByParenthesis(sentence)) {
			List<Token> tokens = ss.getTokens();

			List<Integer> beforeIndexes = SentenceUtils.findLemmasInTokens(BEFORE, tokens);

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
		}
		return 0;
	}

	private List<Token> findPreviousSentence(List<Token> tokens, Integer beforeIndex) {
		int startSentenceIndex = 0;
		for (int i = beforeIndex; i >= 0; i--) {
			Token current = tokens.get(i);
			if (SentenceUtils.lemmasContainToken(PUNCTUATION, current)) {
				if (current.getLemma().equals("-") && tokens.get(i + 1).getLemma().equals(">")) {
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
		for (int i = beforeIndex; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if (SentenceUtils.lemmasContainToken(PUNCTUATION, current)) {
				if (current.getLemma().equals("-") && i + 1 < tokens.size()
						&& tokens.get(i + 1).getLemma().equals(">")) {
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
					|| (i - 1 >= 0 && SentenceUtils.lemmasContainToken(PRESENT_TENSE_VERBS, current)
							&& current.getGeneralPos().equals("NN")
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
			if (current.getGeneralPos().equals("VB") || (SentenceUtils.lemmasContainToken(PRESENT_TENSE_VERBS, current)
					&& current.getGeneralPos().equals("NN"))) {
				return true;
			}
		}
		return false;
	}

}
