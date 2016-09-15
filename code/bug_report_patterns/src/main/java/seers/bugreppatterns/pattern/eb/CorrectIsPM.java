package seers.bugreppatterns.pattern.eb;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.ob.NegativeAfterPM;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class CorrectIsPM extends ObservedBehaviorPatternMatcher {

	final private static Set<String> CORRECT_TERMS = JavaUtils.getSet("correct", "right");

	private static final Set<String> NOT_ALLOWED_POS = JavaUtils.getSet("VB", "VBD", "VBG", "VBP", "VDZ", "MD");

	private static final Set<String> PUNCTUATION = JavaUtils.getSet(",", ";", ":", ".", "_", "-");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		if (!isNegative(sentence)) {
			List<Sentence> subSentences = SentenceUtils.findSubSentences(sentence,
					findPunctuation(sentence.getTokens()));

			for (Sentence subSentence : subSentences) {

				List<Token> tokens = subSentence.getTokens();
				if (containsCorrectTerm(tokens) && isNounPhrase(tokens)) {
					return 1;
				} else if (containsCorrectTerm(tokens) && containsPresentBeOrWouldBe(tokens)) {
					return 1;
				}

			}
		}

		return 0;

	}

	private boolean containsCorrectTerm(List<Token> tokens) {
		return SentenceUtils.tokensContainAnyLemmaIn(tokens, CORRECT_TERMS);
	}

	private boolean containsPresentBeOrWouldBe(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);
			if (current.getLemma().equals("be") && (current.getPos().equals("VBP") || current.getPos().equals("VBZ"))) {
				return true;
			} else if (current.getLemma().equals("would") && current.getPos().equals("MD")) {
				Token next = i + 1 < tokens.size() ? tokens.get(i + 1) : null;

				if (next != null && next.getLemma().equals("be") && next.getGeneralPos().equals("VB")) {
					return true;
				}
			}
		}
		return false;
	}

	private List<Integer> findPunctuation(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(PUNCTUATION, tokens);
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NegativeAfterPM.NEGATIVE_PMS);
	}

	private boolean isNounPhrase(List<Token> tokens) {
		for (Token token : tokens) {
			if (NOT_ALLOWED_POS.stream().anyMatch(t -> token.getGeneralPos().equals(t))) {
				return false;

			}
		}
		return true;
	}

}
