package seers.bugreppatterns.pattern.sr;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

import java.util.List;

/**
 * Created by juan on 4/18/16.
 */
public class ContinuousPresentPM extends StepsToReproducePatternMatcher {
    @Override
    public int matchSentence(Sentence sentence) throws Exception {
        return 0;
    }

    @Override
    public int matchParagraph(Paragraph paragraph) throws Exception {
        // TODO: match OB sentence at the end and make more specific
        List<Sentence> sentences = paragraph.getSentences();
        // The amount of sentences that are either continuous or present
        int matchingSentences = 0;

        // Find at least one continuous or present verb in each sentence
        for (Sentence sentence : sentences) {
            if (containsContinuousOrPresent(sentence)) {
                matchingSentences++;
            }
        }

        int sentencesWithVerbs = countSentencesWithVerbs(paragraph);
        float matchingSentenceRatio = (float) matchingSentences / sentencesWithVerbs;

        // Return a match if most sentences with verbs are either continuous or present tense
        return matchingSentenceRatio > 0.5 ? 1 : 0;
    }

    private int countSentencesWithVerbs(Paragraph paragraph) {
        int sentencesWithVerbs = 0;
        for (Sentence sentence : paragraph.getSentences()) {
            for (Token token : sentence.getTokens()) {
                if (token.getGeneralPos().equals("VB")) {
                    sentencesWithVerbs++;
                    break;
                }
            }
        }

        return sentencesWithVerbs;
    }

    private boolean containsContinuousOrPresent(Sentence sentence) {
        int baseVerbs = 0;
        int otherVerbs = 0;
        int continuousOrPresentVerbs = 0;

        for (Token token : sentence.getTokens()) {
            if (token.getGeneralPos().equals("VB")) {
                String pos = token.getPos();
                if (pos.equals("VB")) {
                    baseVerbs++;
                } else if (pos.equals("VBG") ||
                        pos.equals("VBN") ||
                        pos.equals("VBP") ||
                        pos.equals("VBZ")) {
                    continuousOrPresentVerbs++;
                } else {
                    otherVerbs++;
                }
            }
        }

        if (continuousOrPresentVerbs > 0) {
            return true;
        } else {
            return otherVerbs == 0 && baseVerbs > 0;
        }
    }
}
