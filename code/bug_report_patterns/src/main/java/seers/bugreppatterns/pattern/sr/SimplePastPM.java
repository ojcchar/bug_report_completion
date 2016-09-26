package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for S_SR_SIMPLE_PAST.
 */
public class SimplePastPM extends StepsToReproducePatternMatcher {
	/**
	 * Verbs whose present and simple past forms are identical and are assumed
	 * to be past giving them the benefit of the doubt.
	 */
	private static final Set<String> VERBS_ASSUMED_PAST = JavaUtils.getSet("set");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		// Times we have assumed a verb with identical present and past forms to
		// be past.
		int assumptions = 0;

		// Times we can tell with a good probability that a verb is acceptable
		// for this pattern.
		int nonAssumptions = 0;

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);

			// If we find a verb we have to establish whether it is acceptable
			// for the pattern
			if (token.getGeneralPos().equals("VB")) {
				if (VERBS_ASSUMED_PAST.contains(token.getLemma())) {
					// When there's ambiguity, we assume the verb is in past
					assumptions++;
				} else if (token.getPos().equals("VBD") || isTrueInfinitive(sentence, i)
						|| isMistaggedSimplePast(sentence, i) || isMistaggedNoun(sentence, i)
						|| isGerund(sentence, i)) {

					// Acceptable verbs:
					// - Simple past (VBD)
					// - Tagged as participle (VBN) but not after a "to have" or
					// "to be" (likely VBD)
					// - Tagged present (VBP) but are likely to be nouns (are
					// after a CC)
					// - Tagged gerund (VBG) but not present participle (not
					// preceded by "to be")

					nonAssumptions++;
				} else {
					// If the verb has any other form, the sentence doesn't
					// match
					return 0;
				}
			}
		}

		// Match provided we didn't make more assumptions than clear judgments
		return assumptions <= nonAssumptions ? 1 : 0;
	}

	/**
	 * Returns {@code true} if the VB is not likely to be a VBP
	 *
	 * @param sentence
	 *            The sentence.
	 * @param position
	 *            Position of the token.
	 * @return {@code true} if the VB is not likely to be a VBP
	 */
	private boolean isTrueInfinitive(Sentence sentence, int position) {
		Token token = sentence.getTokens().get(position);

		// TODO: what happens if it's in the first position?
		if (token.getPos().equals("VB") && position > 0) {
			String previousTokenPos = sentence.getTokens().get(position - 1).getPos();
			return !previousTokenPos.equals("CC");
		}

		return false;
	}

	private boolean isGerund(Sentence sentence, int position) {
		Token token = sentence.getTokens().get(position);

		if (token.getPos().equals("VBG")) {
			if (position <= 0) {
				return true;
			} else {
				String previousTokenLemma = sentence.getTokens().get(position - 1).getLemma();
				return !previousTokenLemma.equals("be");
			}
		}

		return false;
	}

	/**
	 * Returns {@code true} if the VBP is likely to be a noun.
	 *
	 * @param sentence
	 *            The sentence.
	 * @param position
	 *            Position of the token.
	 * @return {@code true} if the VBP is likely to be a noun.
	 */
	private boolean isMistaggedNoun(Sentence sentence, int position) {
		Token token = sentence.getTokens().get(position);

		// TODO: what happens if it's in the first position?
		if (token.getPos().equals("VBP") && position > 0) {
			String previousTokenPos = sentence.getTokens().get(position - 1).getPos();
			if (previousTokenPos.equals("DT")) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Returns true if the token in the specified position is tagged as VBN but
	 * is not after a "to have" or "to be" verb, since in this case it is most
	 * likely a VBD.
	 *
	 * @param sentence
	 *            Sentence to search in.
	 * @param position
	 *            position of the token suspected to be VBD.
	 * @return {@code true} if the token is likely to be VBD.
	 */
	private boolean isMistaggedSimplePast(Sentence sentence, int position) {
		Token token = sentence.getTokens().get(position);

		// TODO: what happens if it's in the first position?
		if (token.getPos().equals("VBN") && position > 0) {
			String previousTokenLemma = sentence.getTokens().get(position - 1).getLemma();
			if (!(previousTokenLemma.equals("have") && previousTokenLemma.equals("be"))) {
				return true;
			}
		}

		return false;
	}
}
