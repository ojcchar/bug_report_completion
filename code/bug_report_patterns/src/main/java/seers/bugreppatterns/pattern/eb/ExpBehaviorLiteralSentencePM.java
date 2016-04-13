package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ExpBehaviorLiteralSentencePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		String text = TextProcessor.getStringFromLemmas(sentence);

		// ----------------

		boolean b = text.matches("expect ((result|behavior) )?(:|-+).+");
		if (b) {
			List<Token> tokens = sentence.getTokens();
			if (tokens.get(0).getGeneralPos().equals("VB")) {
				return 1;
			}
		} else {

			b = text.matches("expectation :.+");
			if (b) {
				return 1;
			}
		}

		return 0;
	}

}
