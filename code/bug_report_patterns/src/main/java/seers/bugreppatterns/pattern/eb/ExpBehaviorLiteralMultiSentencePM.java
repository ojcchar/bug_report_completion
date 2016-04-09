package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
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

			List<Sentence> stnces = new ArrayList<>();
			stnces.add(sentence);

			String text = TextProcessor.getStringFromSentences(stnces).trim();

			// ----------------

			boolean b = text.matches("expect ((result|behavior) )?(:|-+) .+");
			if (b) {
				List<Token> tokens = TextProcessor.getAllTokens(stnces);
				if (tokens.get(0).getGeneralPos().equals("VB")) {
					return 1;
				}
			} else {
				b = text.matches("expect , ((result|behavior) )?.+(:|-+) .+");
				if (b) {
					List<Token> tokens = TextProcessor.getAllTokens(stnces);
					if (tokens.get(0).getGeneralPos().equals("VB")) {
						return 1;
					}
				} else {
					b = text.matches("expect ((result|behavior) )?(:|-+)\r\n");
					if (b) {
						List<Token> tokens = TextProcessor.getAllTokens(stnces);
						if (tokens.get(0).getGeneralPos().equals("VB")) {
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}

}
