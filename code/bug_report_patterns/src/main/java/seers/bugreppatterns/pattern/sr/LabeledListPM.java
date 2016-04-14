package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class LabeledListPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		List<Sentence> sentences = paragraph.getSentences();

		if (sentences.size() > 1) {
			Sentence sentence = sentences.get(0);
			String text = TextProcessor.getStringFromLemmas(sentence);
			if (text.matches("reproducible.*")) {
				sentence = sentences.get(1);
				text = TextProcessor.getStringFromLemmas(sentence);
			}
			//boolean b = text.matches("((actual|observed|current) )?((result|behavior|description|situation))? ?(:|-+)?");
			boolean b = ((text.matches(".*step.*"))||(text.matches(".*to reproduce.*"))||(text.matches("str :"))||(text.matches(".*try :")));
			if (b) {
				for (Sentence sen : sentences) {
					text = TextProcessor.getStringFromLemmas(sen);
					if (text.matches("^(\\d+|\\-).*")) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

}
