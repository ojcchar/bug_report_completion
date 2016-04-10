package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ExpectSentencePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		ExpBehaviorLiteralSentencePM pm = new ExpBehaviorLiteralSentencePM();
		int match = pm.matchSentence(sentence);

		Sentence newSentence = sentence;
		if (match > 0) {
			List<Token> tokens = sentence.getTokens();
			int i = findFirstToken(tokens, ":");
			newSentence.setTokens(tokens.subList(i + 1, tokens.size()));
		}

		List<Token> tokens = newSentence.getTokens();

		boolean anyMatch = tokens.stream()
				.anyMatch(t -> t.getLemma().equalsIgnoreCase("expect") && t.getGeneralPos().equals("VB"));
		if (anyMatch) {
			return 1;
		}

		return 0;
	}

	private int findFirstToken(List<Token> tokens, String lemma) {
		for (int i = 0; i < tokens.size(); i++) {
			Token t = tokens.get(i);
			if (t.getLemma().equalsIgnoreCase(lemma)) {
				return i;
			}

		}
		return -1;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		return defaultMatchParagraph(paragraph);
	}

}
