package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ExpBehaviorLiteralMultiSentencePM extends ExpectedBehaviorPatternMatcher {

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
			// ----------------

			if (!sentences.get(1).getTokens().isEmpty()) {

				// ?s: '.' matches any character, including a line terminator
				boolean b = text.matches("(?s)(\\W+ )?expect(ed)? ((result|behavio(u)?r) )?(:|-+)?.*");
				if (b) {
					return 1;
				} else {
					b = text.matches("(?s)(\\W+ )?describe the result you expect(:|-+)?.*");
					if (b) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

}
