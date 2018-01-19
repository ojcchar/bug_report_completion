package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SimpleTenseChecker;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SimplePastParagraphPM extends StepsToReproducePatternMatcher {

	private static final Set<String> EXCLUDED_VERBS = JavaUtils.getSet("notice");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		List<Sentence> sentences = paragraph.getSentences();

		if (sentences.size() < 2) {
			return 0;
		}

		int validNumSentences = 0;
		int numPastSentences = 0;

		for (Sentence sentence : sentences) {

			// no bullets allowed
			List<Token> tokensNoBullet = LabeledListPM.getTokensNoBullet(sentence);
			if (!tokensNoBullet.isEmpty()) {
				continue;
			}

			validNumSentences++;

			int num = countNumClausesInSimplePast(sentence);
			if (num > 0) {
				numPastSentences++;
			}

		}

//		 System.out.println(numPastSentences +" - "+validNumSentences);

		// more than 1 match?
		return ((float) numPastSentences) / validNumSentences >= 0.5F ? 1 : 0;
	}

	public static int countNumClausesInSimplePast(Sentence sentence) {
		SimpleTenseChecker pastChecker = SimpleTenseChecker
				.createPastCheckerOnlyPronouns(EXCLUDED_VERBS);
		return pastChecker.countNumClauses(sentence);
	}

}
