package seers.bugreppatterns.pattern.sr;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.ob.ConditionalNegativePM;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Matcher for S_SR_COND_CODE.
 */
public class ConditionalCodePM extends StepsToReproducePatternMatcher {
    public static final Set<String> CODE_LEMMAS = JavaUtils.getSet("query");

    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        List<Sentence> subordinates = SentenceUtils.breakByParenthesis(sentence);

        for (Sentence subordinate : subordinates) {
            List<Token> tokens = subordinate.getTokens();
            List<Integer> conditionalPositions =
                    SentenceUtils.findLemmasInTokens(ConditionalNegativePM.CONDITIONAL_TERMS,
                            tokens);

            if (!conditionalPositions.isEmpty()) {
                List<Sentence> clauses =
                        SentenceUtils.findSubSentences(subordinate, conditionalPositions);

                // If the first clause doesn't contain a conditional, ignore it
                int startingIndex = conditionalPositions.get(0) == 0 ? 0 : 1;
                for (int i = startingIndex; i < clauses.size(); i++) {
                    if (clauses.get(i).getTokens().stream()
                            .anyMatch(t -> {
                                // Attempt to split the lemma for stuff like files with extensions
                                String[] components = t.getLemma().split("\\.");

                                return Stream.of(components)
                                        .anyMatch(CodeRefPM.NOUNS_TERM::contains);
                            })) {
                        return 1;
                    }
                }
            }
        }

        return 0;
    }
}
