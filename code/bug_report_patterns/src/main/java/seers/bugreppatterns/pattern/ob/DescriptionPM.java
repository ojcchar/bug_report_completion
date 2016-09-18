package seers.bugreppatterns.pattern.ob;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class DescriptionPM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) {
		List<Sentence> sentences = paragraph.getSentences();
		Sentence sentence = sentences.get(0);
		String text = TextProcessor.getStringFromLemmas(sentence);
		if (text.matches("^[^a-z]*description[ ]?:.*") || text.matches("[^a-z]*problem description[ ]?:.*")
				|| text.matches("[^a-z]*description of (the )?problem[ ]?:.*")
				|| text.matches("[^a-z]*describe the result you receive[ ]?:.*")) {
			return 1;
		}
		return 0;
	}

}
