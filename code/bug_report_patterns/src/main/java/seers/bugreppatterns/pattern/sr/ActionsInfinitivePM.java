package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for P_SR_ACTIONS_INF.
 */
public class ActionsInfinitivePM extends StepsToReproducePatternMatcher {
	/**
	 * General PoS of words to be considered bullets if they appear at the
	 * beginning of a sentence.
	 */
	public static final Set<String> BULLET_POS = JavaUtils.getSet("CD", "LS", ":", "-LRB-", "-RRB-");

	public static final String[] MISTAGGED_VERBS = { "type" };

	private Pattern nonLetters = Pattern.compile("[\\.\\d]");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		int bulletedSentences = 0;
		int infinitiveSentences = 0;
		for (Sentence sentence : paragraph.getSentences()) {
			// Determines if the sentence contains bullets.
			int firstNonBulletIndex = findFirstNonBulletIndex(sentence);
			if (firstNonBulletIndex == 0 || firstNonBulletIndex >= sentence.getTokens().size()) {
				// If it doesn't contain bullets or consists only of special
				// characters, ignore
				continue;
			} else {
				bulletedSentences++;
			}

			Sentence noBullets = new Sentence("0",
					sentence.getTokens().subList(firstNonBulletIndex, sentence.getTokens().size()));

			// If the first word is an infinitive verb
			if (hasVerbInPos(noBullets, 0)) {
				infinitiveSentences++;
			} else {
				Sentence modifiedSentence = noBullets;

				if (!noBullets.getTokens().get(0).getGeneralPos().equals("NN")) {
					Sentence trimmedSentence = attemptTrim(noBullets);
					if (trimmedSentence != null) {
						if (hasVerbInPos(trimmedSentence, 0)) {
							infinitiveSentences++;
							continue;
						} else {
							modifiedSentence = trimmedSentence;
						}
					}
				}

				Sentence artificialSentence = appendPronoun(modifiedSentence);

				if (artificialSentence != null && hasVerbInPos(artificialSentence, 1)) {
					infinitiveSentences++;
				}
			}
		}

		// Match if most bulleted sentences are in infinitive
		return ((float) infinitiveSentences) / bulletedSentences > 0.5F ? 1 : 0;
	}

	/**
	 * Tries to remove a clause from the beginning of a sentence. Returns null
	 * if there are no clauses to remove.
	 *
	 * @param sentence
	 *            A sentence.
	 * @return A trimmed sentence or null.
	 */

	private Sentence attemptTrim(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();

		// Remove initial conjunction
		if (tokens.get(0).getPos().equals("CC")) {
			return new Sentence("", tokens.subList(1, tokens.size()));
		}

		// Try to trim everything before the first comma found
		int tokenAmount = tokens.size();
		for (int i = 0; i < tokenAmount; i++) {
			Token token = tokens.get(i);
			if (token.getLemma().equals(",") && i + 1 < tokenAmount) {
				return new Sentence("", tokens.subList(i + 1, tokenAmount));
			}
		}

		// Search for initial prepositions
		if (tokens.get(0).getGeneralPos().equals("IN")) {
			// Trim until the first noun found, inclusive
			for (int i = 0; i < tokenAmount; i++) {
				Token token = tokens.get(i);
				if (token.getGeneralPos().equals("NN") && i + 1 < tokenAmount) {
					return new Sentence("", tokens.subList(i + 1, tokenAmount));
				}
			}
		}

		return null;
	}

	/**
	 * Appends the pronoun "I" to the beginning of the sentence and reassigns
	 * PoS tags.
	 *
	 * @param sentence
	 *            Sentence to be modified
	 * @return A re-tagged sentence
	 */
	private Sentence appendPronoun(Sentence sentence) {
		String sentenceText = String.join(" ",
				sentence.getTokens().stream().map(t -> t.getWord().toLowerCase()).toArray(CharSequence[]::new));
		// Appends an "I" to the beginning of the sentence, attempting to nudge
		// the tagger
		// into recognizing an infinitive verb as such.
		String artificialSentenceText = String.format("I %s", sentenceText);

		return SentenceUtils.parseSentence("0", artificialSentenceText);
	}

	private int findFirstNonBulletIndex(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();

		// Skips all the items at the beginning of the sentence that can be
		// considered list markers,
		// finishing at an index where the first actual word of the sentence is
		int sentenceStart;
		for (sentenceStart = 0; sentenceStart < tokens.size(); sentenceStart++) {
			Token token = tokens.get(sentenceStart);

			// Cardinal numbers or list items and some punctuation count as list
			// items
			// Example:
			// 1
			// 1.
			// 1)
			// [1]
			// -
			// 1:
			if (!BULLET_POS.contains(token.getGeneralPos())) {
				// If a noun contains numbers or periods it is also considered a
				// list item. The
				// regexp finds any instances of periods or numbers in the noun
				// Examples:
				// A. // A. has label NN
				// [5a] // 5a has label NN
				if (!(token.getGeneralPos().equals("NN") && nonLetters.matcher(token.getWord()).find())) {
					break;
				}
			}
		}

		return sentenceStart;
	}

	private boolean hasVerbInPos(Sentence sentence, int position) {
		if(position >= 0 && position < sentence.getTokens().size()) {
			Token token = sentence.getTokens().get(position);

			return token.getPos().equals("VB") || token.getPos().equals("VBP")
				|| Stream.of(MISTAGGED_VERBS).anyMatch(v -> token.getWord().toLowerCase().equals(v));
		}
		return false;
	}
}
