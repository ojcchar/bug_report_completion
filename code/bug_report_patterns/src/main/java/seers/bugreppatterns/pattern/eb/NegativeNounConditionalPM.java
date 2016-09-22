package seers.bugreppatterns.pattern.eb;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.ob.NegativeTerms;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.List;

/**
 * Matcher for S_EB_NEG_NOUN_COND.
 */
public class NegativeNounConditionalPM extends ExpectedBehaviorPatternMatcher {
    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        List<Token> tokens = sentence.getTokens();
        if (tokens.get(0).getWord().toLowerCase().equals("no")) {
            Token secondToken = tokens.get(1);
            if (secondToken.getGeneralPos().equals("NN") &&
                    NegativeTerms.NOUNS.contains(secondToken.getLemma()) &&
                    !SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, tokens).isEmpty()) {
                return 1;
            }
        }

        return 0;
    }
}
