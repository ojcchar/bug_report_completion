package seers.bugreppatterns.pattern.eb;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;

public class MustSentencePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		boolean anyMatch = sentence.getTokens().stream().anyMatch(t ->
		// t.getPos().equals("MD") &&
		t.getLemma().equalsIgnoreCase("must"));
		return anyMatch ? 1 : 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		return defaultMatchParagraph(paragraph);
	}

}
