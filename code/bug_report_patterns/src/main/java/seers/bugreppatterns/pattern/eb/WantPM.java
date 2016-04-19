package seers.bugreppatterns.pattern.eb;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Created by juan on 4/19/16.
 */
public class WantPM extends ExpectedBehaviorPatternMatcher {
    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        return sentence.getTokens().stream()
                .filter(t -> t.getGeneralPos().equals("VB") && t.getLemma().equals("want"))
                .findFirst().isPresent() ? 1 : 0;
    }
}
