package seers.bugreppatterns.pattern.eb;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ShouldPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Sentence> sentences = new ArrayList<>();
		sentences.add(sentence);

		List<Token> tokens = TextProcessor.getAllTokens(sentences);

		Optional<Token> first = tokens.stream().filter(t -> "MD".equals(t.getPos()) && "should".equals(t.getLemma()))
				.findFirst();
		return first.isPresent() ? 1 : 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {
		return defaultMatchParagraph(paragraph);
	}

}
