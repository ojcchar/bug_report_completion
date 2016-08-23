package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeVerbPM extends ObservedBehaviorPatternMatcher {

    private static final Pattern OTHER_NEGATIVE_VERBS_PATTERN =
            Pattern.compile("\\W(?:slow doen|slow down|faile|stucks up|consume 100|get turn into|" +
                    "be out of|pull out|faul|hangs/get|failes|timing out|go away)\\W");

    @Override
    public int matchSentence(Sentence sentence) throws Exception {

        List<Token> tokens = sentence.getTokens();
        for (Token token : tokens) {
            if (Arrays.stream(NegativeTerms.VERBS).anyMatch(t -> token.getLemma().equals(t))
                    && (token.getGeneralPos().equals("VB") || token.getGeneralPos().equals("NN")
                    || token.getGeneralPos().equals("JJ"))) {
                return 1;
            }
        }
        String txt = TextProcessor.getStringFromLemmas(sentence);

        Matcher otherVerbsMatcher = OTHER_NEGATIVE_VERBS_PATTERN.matcher(txt);
        if (otherVerbsMatcher.find()) {
            return 1;
        }

        return 0;
    }

}
