package seers.bugreppatterns.pattern.sr;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * Matcher for P_SR_ACTIONS_INF.
 */
public class ActionsInfinitivePM extends StepsToReproducePatternMatcher {
    /**
     * General PoS of words to be considered bullets if they appear at the beginning of a
     * sentence.
     */
    public static final String[] BULLET_POS = {"CD", "LS", ":", "-LRB-", "-RRB-"};

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
                // If it doesn't contain bullets or consists only of special characters, ignore
                continue;
            } else {
                bulletedSentences++;
            }

            Sentence noBullets = new Sentence("0",
                    sentence.getTokens().subList(firstNonBulletIndex, sentence.getTokens().size()));
            List<Token> tokens = noBullets.getTokens();

            // If the first word is an infinitive verb
            Token firstToken = tokens.get(0);
            if (firstToken.getPos().equals("VB") || firstToken.getWord().toLowerCase().equals("type")) {
                infinitiveSentences++;
            } else {
                Sentence newSentence = appendPronoun(noBullets);

                // We need generalPos here because it will no longer be a base form verb
                if (newSentence != null &&
                        newSentence.getTokens().get(1).getGeneralPos().equals("VB")) {
                    infinitiveSentences++;
                }
            }
        }

        // Match if most sentences are in infinitive
        return ((float) infinitiveSentences) / bulletedSentences > 0.5 ? 1 : 0;
    }

    /**
     * Appends the pronoun "I" to the beginning of the sentence and reassigns PoS tags.
     *
     * @param sentence Sentence to be modified
     * @return A re-tagged sentence
     */
    private Sentence appendPronoun(Sentence sentence) {
        String sentenceText = String.join(" ", sentence.getTokens().stream()
                .map(t -> t.getWord().toLowerCase())
                .toArray(CharSequence[]::new));
        // Appends an "I" to the beginning of the sentence, attempting to nudge the tagger
        // into recognizing an infinitive verb as such.
        String artificialSentenceText = String.format("I %s", sentenceText);

        return SentenceUtils.parseSentence("0", artificialSentenceText);
    }

    private int findFirstNonBulletIndex(Sentence sentence) {
        List<Token> tokens = sentence.getTokens();

        // Skips all the items at the beginning of the sentence that can be considered list markers,
        // finishing at an index where the first actual word of the sentence is
        int sentenceStart;
        for (sentenceStart = 0; sentenceStart < tokens.size(); sentenceStart++) {
            Token token = tokens.get(sentenceStart);

            // Cardinal numbers or list items and some punctuation count as list items
            // Example:
            // 1
            // 1.
            // 1)
            // [1]
            // -
            // 1:
            if (Stream.of(BULLET_POS).noneMatch(pos -> token.getGeneralPos().equals(pos))) {
                // If a noun contains numbers or periods it is also considered a list item. The
                // regexp finds any instances of periods or numbers in the noun
                // Examples:
                // A.   // A. has label NN
                // [5a] // 5a has label NN
                if (!(token.getGeneralPos().equals("NN") &&
                        nonLetters.matcher(token.getWord()).find())) {
                    break;
                }
            }
        }

        return sentenceStart;
    }
}
