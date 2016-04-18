package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

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

			boolean b = text.matches("(?s)expect ((result|behavior) )?(:|-+)?.*");
			if (b) {
				List<Token> tokens = sentence.getTokens();
				if (tokens.get(0).getGeneralPos().equals("VB")) {
					return 1;
				}
			}
		}
		return 0;
	}

}
