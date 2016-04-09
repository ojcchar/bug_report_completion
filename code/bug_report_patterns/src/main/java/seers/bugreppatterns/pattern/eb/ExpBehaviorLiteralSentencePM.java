package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ExpBehaviorLiteralSentencePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> sentences = new ArrayList<>();
		sentences.add(sentence);

		String text = TextProcessor.getStringFromSentences(sentences).trim();

		// ----------------

		boolean b = text.matches("expect ((result|behavior) )?(:|-+) .+");
		if (b) {
			List<Token> tokens = TextProcessor.getAllTokens(sentences);
			if (tokens.get(0).getGeneralPos().equals("VB")) {
				return 1;
			}
		}

		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		return defaultMatchParagraph(paragraph);
	}

}
