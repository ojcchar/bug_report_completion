package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

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

		List<Sentence> sentences = paragraph.getSentences();

		if (sentences.size() < 2) {
			return 0;
		}

		int bulletedSentences = 0;
		int imperativeSentences = 0;
		for (Sentence sentence : sentences) {
			// Determines if the sentence contains bullets.
			int firstNonBulletIndex = findFirstNonBulletIndex(sentence);
			if (firstNonBulletIndex == 0 || firstNonBulletIndex >= sentence.getTokens().size()) {
				// If it doesn't contain bullets or consists only of special
				// characters, ignore
				continue;
			} else {
				bulletedSentences++;
			}

			Sentence noBulletsSentece = new Sentence("0",
					sentence.getTokens().subList(firstNonBulletIndex, sentence.getTokens().size()));

			List<Sentence> clauses = SentenceUtils.extractClauses(noBulletsSentece);

			// check just first 2 clauses
			for (int i = 0; i < clauses.size() && i < 2; i++) {
				Sentence clause = clauses.get(i);

				boolean isImp = SentenceUtils.isImperativeSentence(clause, true);
				if (isImp) {
					imperativeSentences++;
					break;
				}
			}

		}

		// System.out.println(imperativeSentences +" - "+bulletedSentences);

		// Match if most bulleted sentences are in infinitive
		return ((float) imperativeSentences) / bulletedSentences >= 0.5F ? 1 : 0;
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

}
