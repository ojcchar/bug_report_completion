package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;

public class ConditionalSequenceParagrahPM extends StepsToReproducePatternMatcher {

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

		int lastCondSentence = -1;
		int numMatchedSentences = 0;
		for (int i = 0; i < sentences.size(); i++) {
			Sentence sentence = sentences.get(i);
			if (isConditional(sentence)) {
				lastCondSentence = i;
				numMatchedSentences++;
			}
		}

		if (lastCondSentence != -1) {
			int idx = SentenceUtils.findObsBehaviorSentence(sentences.subList(lastCondSentence, sentences.size()));
			if (idx != -1) {
				numMatchedSentences++;
			}
		}

		// System.out.println(numMatchedSentences +" - "+sentences.size());

		return ((float) numMatchedSentences) / sentences.size() >= 0.5F ? 1 : 0;
	}

	private boolean isConditional(Sentence sentence) {
		List<Integer> condTerms = SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, sentence.getTokens());
		return !condTerms.isEmpty();
	}
}
