package seers.bugreppatterns.pattern.eb;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;

import java.util.List;
import java.util.Set;

/**
 * Matcher for S_EB_BUT_CORRECT.
 */
public class ButCorrectPM extends ExpectedBehaviorPatternMatcher {
    public static final Set<String> CONTRAST_TERMS = JavaUtils.getSet("but", "however");
    public static final Set<String> POSITIVE_TERMS = JavaUtils.getSet("correct", "right");
    private final NegativeAuxVerbPM negAuxMatcher;

    public ButCorrectPM() {
        negAuxMatcher = new NegativeAuxVerbPM();
    }

    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        List<Integer> contrastTermIndexes =
                SentenceUtils.findLemmasInTokens(CONTRAST_TERMS, sentence.getTokens());
        if (!contrastTermIndexes.isEmpty()) {
            List<Sentence> subSentences =
                    SentenceUtils.findSubSentences(sentence, contrastTermIndexes);
            // Take sentences by pairs
            for (int i = 0; i < subSentences.size() - 1; i++) {
                Sentence firstSentence = subSentences.get(i);
                Sentence secondSentence = subSentences.get(i + 1);

                if (negAuxMatcher.matchSentence(firstSentence) == 0 &&
                        !SentenceUtils.findLemmasInTokens(POSITIVE_TERMS,
                                secondSentence.getTokens()).isEmpty()) {
                    return 1;
                }
            }
        }

        return 0;
    }
}
