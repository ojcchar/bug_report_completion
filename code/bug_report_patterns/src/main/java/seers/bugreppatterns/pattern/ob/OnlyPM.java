package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.eb.MakeSensePM;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class OnlyPM extends ObservedBehaviorPatternMatcher {

	private static final String ONLY = "only";
	private static final PatternMatcher[] EB_PMS = { new MakeSensePM() };

	private static String[] ALLOWED_PREPS = new String[] { "that" };

	private static String[] PRESENT_TENSE_VERBS = new String[] { "crashes", "builds", "returns", "copies" };

	private static String[] PUNCTUATION = new String[] { ",", ".", ";", ":", "--" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Integer> punctuation = findLemmasInTokens(PUNCTUATION, sentence.getTokens());
		List<Sentence> subSentences = findSubSentences(sentence, punctuation);

		for (Sentence subSentence : subSentences) {
			List<Token> tokens = subSentence.getTokens();
			if (containsOnly(tokens) && !isEBModal(tokens) && !isEB(subSentence)) {
				return 1;
			}
		}
		return 0;
	}

	private boolean containsOnly(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			// find every "only"
			if ((current.getGeneralPos().equals("RB") || current.getGeneralPos().equals("JJ"))
					&& current.getLemma().equals(ONLY)) {

				// The right "only"
				Token previous = i - 1 >= 0 ? tokens.get(i - 1) : null;
				Token next = i + 1 < tokens.size() ? tokens.get(i + 1) : null;

				if (previous != null && previous.getGeneralPos().equals("IN")
						&& !Arrays.stream(ALLOWED_PREPS).anyMatch(t -> previous.getLemma().equals(t))) {
					continue;
				}
				// the one that comes before or after a verb
				if ((previous != null && previous.getGeneralPos().equals("VB"))
						|| (next != null && next.getGeneralPos().equals("VB"))
						// some verbs that are not correctly PoS tagged
						|| (previous != null && Arrays.stream(PRESENT_TENSE_VERBS)
								.anyMatch(t -> previous.getWord().equalsIgnoreCase(t)))
						|| (next != null && Arrays.stream(PRESENT_TENSE_VERBS)
								.anyMatch(t -> next.getWord().equalsIgnoreCase(t)))) {
					return true;
				}

				// the one that precedes a verb in infinitive
				if (next != null && next.getGeneralPos().equals("TO")) {
					Token afterTo = i + 2 < tokens.size() ? tokens.get(i + 2) : null;

					if (afterTo != null && afterTo.getGeneralPos().equals("VB")) {
						return true;
					}
				}

			}
		}
		return false;
	}

	private boolean isEB(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, EB_PMS);
	}
}
