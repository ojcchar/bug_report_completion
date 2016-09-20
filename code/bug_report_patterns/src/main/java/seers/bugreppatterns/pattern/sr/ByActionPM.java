package seers.bugreppatterns.pattern.sr;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.List;

/**
 * Matcher for S_SR_BY_ACTION.
 */
public class ByActionPM extends StepsToReproducePatternMatcher {
    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        List<Token> tokens = sentence.getTokens();
        for (int i = 0; i < tokens.size() - 1; i++) {
            Token token = tokens.get(i);
            if (token.getLemma().equals("by")) {
                for (int j = i; j < tokens.size(); j++) {
                    Token secondToken = tokens.get(j);
                    if (secondToken.getGeneralPos().equals("VB")) {
                        if (secondToken.getPos().equals("VBG")) {
                            return 1;
                        } else {
                            break;
                        }
                    }
                }
            }
        }

        return 0;
    }
}
