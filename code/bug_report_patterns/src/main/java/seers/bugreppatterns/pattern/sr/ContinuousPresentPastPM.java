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
public class ContinuousPresentPastPM extends StepsToReproducePatternMatcher {
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

		SimpleTenseChecker presentChecker = SimpleTenseChecker.createPresentChecker(SimplePresentSubordinatesPM.EXCLUDED_VERBS);
		SimpleTenseChecker pastChecker = SimpleTenseChecker.createPastChecker(SimplePresentSubordinatesPM.EXCLUDED_VERBS);

		// Find at least one continuous or present/past sentence
		for (Sentence sentence : sentences) {
			if (ContinuousPresentSentencePM.countNumClauses(sentence) > 0
					|| presentChecker.countNumClauses(sentence) > 0 || pastChecker.countNumClauses(sentence) > 0) {
				matchingSentences++;
			}
		}

		int sentencesWithVerbs = countSentencesWithVerbs(paragraph);
		float matchingSentenceRatio = (float) matchingSentences / sentencesWithVerbs;

//		 System.out.println(matchingSentences +" - " + sentencesWithVerbs);

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
