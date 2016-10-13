package seers.bugreppatterns.pattern.eb;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ExpBehaviorLiteralSentencePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		String text = TextProcessor.getStringFromLemmas(sentence);

		// ----------------

		String regexPrefix = "(?s)(\\W+ )?expect(ed)?( (result|behavio(u)?r))?( (:|-+))";
		boolean b = text.matches(regexPrefix + ".+");
		if (b) {
			// check for only "expect behavior", with no text after the label
			b = text.matches(regexPrefix);
			if (b) {
				return 0;
			} else {
				return 1;
			}
		} else {

			b = text.matches("expectation :.+");
			if (b) {
				return 1;
			}
			
			b = text.matches("(?s)(\\W+ )expect(ed)?( (result|behavio(u)?r)) .+");
			if (b) {
				return 1;
			}
		}

		return 0;
	}

}
