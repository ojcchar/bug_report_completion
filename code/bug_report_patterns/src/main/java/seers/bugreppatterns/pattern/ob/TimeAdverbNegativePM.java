package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Matcher for S_OB_ADV_TIME_NEG.
 */
public class TimeAdverbNegativePM extends ObservedBehaviorPatternMatcher {
    public static final Set<String> TIME_ADVERBS = Arrays.asList("currently", "usually",
            "frequently", "generally", "normally", "occasionally", "often", "regularly",
            "sometimes")
            .stream()
            .collect(Collectors.toSet());

    /**
     * Adverbs that only count as a match if they are in the first position.
     */
    public static final Set<String> FIRST_POS_TIME_ADVERBS = Arrays.asList("then", "now", "again")
            .stream()
            .collect(Collectors.toSet());

    public static final String[] ADVERBIAL_TIME_CLAUSES = {"as soon as", "right now", "once again",
            "for the moment", "up to now"};

    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        List<Token> tokens = sentence.getTokens();

        if (!FIRST_POS_TIME_ADVERBS.contains(tokens.get(0).getLemma()) &&
                !tokens.stream().anyMatch(token -> TIME_ADVERBS.contains(token.getLemma()))) {

            String text = TextProcessor.getStringFromLemmas(sentence);

            if (!Arrays.stream(ADVERBIAL_TIME_CLAUSES).anyMatch(text::contains)) {
                return 0;
            }
        }

        for (PatternMatcher pm : ButNegativePM.NEGATIVE_PMS) {
            if (pm.matchSentence(sentence) == 1) {
                return 1;
            }
        }

        return 0;
    }
}
