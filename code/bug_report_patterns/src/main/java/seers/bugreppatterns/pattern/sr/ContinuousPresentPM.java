package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SimpleTenseChecker;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

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

		// System.out.println(sentences.size());

		if (sentences.size() < 2) {
			return 0;
		}

		// The amount of sentences that are either continuous or present
		int matchingSentences = 0;

		SimpleTenseChecker presentChecker = new SimpleTenseChecker(ActionsPresentPM.POS,
				ActionsPresentPM.UNDETECTED_VERBS, SimplePresentSubordinatesPM.EXCLUDED_VERBS,
				SimplePresentSubordinatesPM.DEFAULT_PRONOUN_POS, SimplePresentSubordinatesPM.DEFAULT_PRONOUN_LEMMAS,
				SimplePresentSubordinatesPM.DEFAULT_PRONOUN_POS_LEMMA);

		SimpleTenseChecker pastChecker = new SimpleTenseChecker(SimplePastParagraphPM.POSs,
				SimplePastParagraphPM.UNDETECTED_VERBS, SimplePresentSubordinatesPM.EXCLUDED_VERBS,
				SimplePresentSubordinatesPM.DEFAULT_PRONOUN_POS, SimplePresentSubordinatesPM.DEFAULT_PRONOUN_LEMMAS,
				SimplePresentSubordinatesPM.DEFAULT_PRONOUN_POS_LEMMA);

		// Find at least one continuous or present verb in each sentence
		for (Sentence sentence : sentences) {
			if (ContinuousPresentSentencePM.countNumClauses(sentence) > 0
					|| presentChecker.countNumClauses(sentence) > 0 || pastChecker.countNumClauses(sentence) > 0) {
				matchingSentences++;
			}
		}

		int sentencesWithVerbs = countSentencesWithVerbs(paragraph);
		float matchingSentenceRatio = (float) matchingSentences / sentencesWithVerbs;

		// System.out.println(matchingSentenceRatio);

		// Return a match if most sentences with verbs are either continuous or
		// present tense
		return matchingSentenceRatio >= 0.5 ? 1 : 0;
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
}
