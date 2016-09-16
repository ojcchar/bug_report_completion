package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;

public class CondSequenceParagrahPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		List<Sentence> sentences = paragraph.getSentences();
		int numMatchedSentences = 0;
		for (Sentence sentence : sentences) {
			if (isConditional(sentence)) {
				numMatchedSentences++;
			}
		}

		int match = numMatchedSentences > 1 ? 1 : 0;

		return match;
	}

	private boolean isConditional(Sentence sentence) {
		List<Integer> condTerms = SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, sentence.getTokens());
		return !condTerms.isEmpty();
	}
}
