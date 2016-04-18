package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ExpectSentencePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		ExpBehaviorLiteralSentencePM pm = new ExpBehaviorLiteralSentencePM();
		int match = pm.matchSentence(sentence);

		List<Token> tokens = sentence.getTokens();
		// analyze the token after the label (e.g., expect. results: bla bla
		// bla --> bla bla bla)
		if (match > 0) {
			int i = findFirstToken(tokens, ":");
			tokens = tokens.subList(i + 1, tokens.size());
		}

		String txt = TextProcessor.getStringFromLemmas(sentence);

		if (!txt.endsWith("right ?") && SentenceUtils.isQuestion(sentence)) {
			return 0;
		}

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

}
