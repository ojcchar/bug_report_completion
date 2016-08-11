package seers.bugreppatterns.pattern.ob;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Matcher for S_OB_OUTPUT_VERB
 */
public class OutputVerbPM extends ObservedBehaviorPatternMatcher {
    public static final Set<String> OUTPUT_VERBS = Arrays.asList("output", "display", "show",
            "return", "report")
            .stream()
            .collect(Collectors.toSet());

    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        // TODO: uncomment line to include only verbs. Commented because some are incorrectly tagged
        boolean match = sentence.getTokens().stream().anyMatch(
                token -> // token.getGeneralPos().equals("VB") &&
                        OUTPUT_VERBS.contains(token.getLemma()));

        return match ? 1 : 0;
    }
}
