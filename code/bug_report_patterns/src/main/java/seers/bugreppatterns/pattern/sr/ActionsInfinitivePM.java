package seers.bugreppatterns.pattern.sr;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matcher for P_SR_ACTIONS_INF.
 */
public class ActionsInfinitivePM extends StepsToReproducePatternMatcher {
    private Pattern lettersOnly = Pattern.compile("[a-zA-Z]+");

    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        return 0;
    }

    @Override
    public int matchParagraph(Paragraph paragraph) throws Exception {
        int infinitiveSentences = 0;
        for (Sentence sentence : paragraph.getSentences()) {
            Sentence noBullets = removeBullets(sentence);
            List<Token> tokens = noBullets.getTokens();

            if (tokens.isEmpty()) {
                continue;
            }

            // FIXME: When an infinitive verb is treated as NN, add "I" at the beginning
            // and recompute PoS tags to see if it gets it right.
            if (tokens.get(0).getGeneralPos().equals("VB")) {
                infinitiveSentences++;
            }
        }

        // Match if most sentences are in infinitive
        return ((float) infinitiveSentences) / paragraph.getSentences().size() > 0.5 ? 1 : 0;
    }

    private Sentence removeBullets(Sentence sentence) {
        List<Token> tokens = sentence.getTokens();

        // Determines where the first token that is not a list marker is
        int sentenceStart;
        for (sentenceStart = 0; sentenceStart < tokens.size(); sentenceStart++) {
            Token token = tokens.get(sentenceStart);

            // Cardinal numbers or items tagged as list markers are immediately considered markers
            if (token.getGeneralPos().equals("CD") ||
                    token.getGeneralPos().equals("LS")) {
                continue;
            }

            // If the token is composed of letters only, we consider it the first real word of the
            // sentence
            Matcher matcher = lettersOnly.matcher(token.getLemma());
            if (matcher.find()) {
                break;
            }
        }

        return new Sentence(sentence.getId(), tokens.subList(sentenceStart, tokens.size()));
    }
}
