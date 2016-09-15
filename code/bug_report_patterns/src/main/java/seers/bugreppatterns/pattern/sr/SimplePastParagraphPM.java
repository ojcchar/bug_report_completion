package seers.bugreppatterns.pattern.sr;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SimpleTenseChecker;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SimplePastParagraphPM extends StepsToReproducePatternMatcher {

	private final static Set<String> POSs = JavaUtils.getSet("VBD", "VBN");
	private final static Set<String> UNDETECTED_VERBS = JavaUtils.getSet("set");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		int num = 0;

		List<Sentence> sentences = paragraph.getSentences();
		for (Sentence sentence : sentences) {

			// no bullets allowed
			List<Token> tokensNoBullet = LabeledListPM.getTokensNoBullet(sentence);
			if (!tokensNoBullet.isEmpty()) {
				continue;
			}

			num += countNumClausesInSimplePast(sentence);

		}

		// more than 1 match?
		if (num > 1) {
			return 1;
		}

		return 0;
	}

	public static int countNumClausesInSimplePast(Sentence sentence) {
		SimpleTenseChecker checker = new SimpleTenseChecker(POSs, UNDETECTED_VERBS);
		return checker.countNumClauses(sentence);
	}

}
