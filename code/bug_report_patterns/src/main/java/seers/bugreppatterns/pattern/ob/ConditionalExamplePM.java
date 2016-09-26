package seers.bugreppatterns.pattern.ob;

import java.util.Collections;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.bugreppatterns.utils.SimpleTenseChecker;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for P_OB_COND_EXAMPLE.
 */
public class ConditionalExamplePM extends ObservedBehaviorPatternMatcher {
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		List<Sentence> sentences = paragraph.getSentences();
		Sentence firstSentence = sentences.get(0);
		List<Token> tokens = firstSentence.getTokens();
		List<Integer> conditionalIndexes = SentenceUtils.findLemmasInTokens(ConditionalNegativePM.CONDITIONAL_TERMS,
				tokens);

		if (conditionalIndexes.get(0) == 0 && tokens.size() >= 4) {
			int tokensSize = tokens.size();
			List<Token> firstSentenceEnding = tokens.subList(tokensSize - 3, tokensSize - 1);

			if (firstSentenceEnding.get(0).getLemma().equals("like")
					&& firstSentenceEnding.get(1).getLemma().equals("this")) {
				SimpleTenseChecker simplePresentChecker = new SimpleTenseChecker(JavaUtils.getSet("VBP", "VBZ"),
						Collections.emptySet());

				if (sentences.subList(1, sentences.size()).stream()
						.anyMatch(s -> simplePresentChecker.countNumClauses(s) > 0)) {
					return 1;
				}
			}
		}

		return 0;
	}
}
