package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ToReproParagraphPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		List<Sentence> sentences = paragraph.getSentences();

		int labelIdx = LabeledListPM.getLabelIndex(sentences);
		if (labelIdx != -1) {
			return 0;
		}

		NegativeAuxVerbPM pm = new NegativeAuxVerbPM();

		// -------------------------------------

		int idxPrefixSentence = -1;
		for (int i = 0; i < sentences.size() && idxPrefixSentence == -1; i++) {
			Sentence sentence = sentences.get(i);

			if (pm.matchSentence(sentence) == 1) {
				continue;
			}

			List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

			if (!clauses.isEmpty()) {
				if (containsToReproPrefix(clauses.get(0))) {
					idxPrefixSentence = i;
				}
			}
		}

		// ----------------------------

		int numImperative = 0;
		if (idxPrefixSentence != -1) {
			numImperative += countNumImperativeSentences(sentences.subList(idxPrefixSentence, sentences.size()));
		}

		if (numImperative > 0) {
			return 1;
		}

		return 0;
	}

	private int countNumImperativeSentences(List<Sentence> sentences) {
		int num = 0;
		for (Sentence sentence : sentences) {
			List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
			for (Sentence clause : clauses) {
				if (SentenceUtils.isImperativeSentence(clause)) {
					num++;
					break;
				}
			}
		}
		return num;
	}

	private boolean containsToReproPrefix(Sentence firstClause) {
		String text = TextProcessor.getStringFromLemmas(firstClause);
		return text.matches(".*to (repro|reproduce|run).*");
	}

}
