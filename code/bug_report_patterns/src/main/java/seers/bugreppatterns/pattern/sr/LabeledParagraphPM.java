package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class LabeledParagraphPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		List<Sentence> sentences2 = paragraph.getSentences();
		if (sentences2.size() > 1) {
			Sentence sentence = sentences2.get(0);
			String text = TextProcessor.getStringFromLemmas(sentence);
			boolean b = text.matches("(step )?(to ((reproduce)|(recreate the problem))) :");
			if (b) {
				Sentence sentence2 = sentences2.get(1);
				text = TextProcessor.getStringFromLemmas(sentence2);
				if (!text.matches("(\\W*)\\d((\\.\\))|\\.|\\)|,|-)(.*)")) {
					return 1;
				}
			}
		}

		return 0;
	}

}
