package seers.bugreppatterns.pattern.sr;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.List;

/**
 * Matcher for S_SR_ACTIONS_PRESENT_PERFECT.
 */
public class ActionsPresentPerfectPM extends StepsToReproducePatternMatcher {
    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        List<Token> tokens = sentence.getTokens();
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getGeneralPos().equals("VB")) {
                if (!isPresentPerfectOrPassiveVoice(tokens, i)) {
                    return 0;
                } else {
                    i++;
                }
            }
        }

        return 1;
    }

    private boolean isPresentPerfectOrPassiveVoice(List<Token> tokens, int position) {
        Token token = tokens.get(position);

        if ((token.getLemma().equals("be") || token.getLemma().equals("have")) &&
                (token.getPos().equals("VBP") || token.getPos().equals("VBZ")) &&
                tokens.size() > position + 1) {
            Token nextToken = tokens.get(position + 1);
            return nextToken.getPos().equals("VBN");
        }

        return false;
    }
}
