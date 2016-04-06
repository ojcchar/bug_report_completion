package seers.bugreppatterns.pattern.eb;

import java.util.List;
import java.util.Optional;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ShouldPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public boolean matchSentence(String text) throws Exception {

		List<Sentence> sentences = TextProcessor.processText(text);
		List<Token> tokens = TextProcessor.getAllTokens(sentences);

		Optional<Token> first = tokens.stream().filter(t -> "MD".equals(t.getPos()) && "should".equals(t.getLemma()))
				.findFirst();
		return first.isPresent();
	}

	@Override
	public boolean matchParagraph(String text) throws Exception {
		return false;
	}

	@Override
	public boolean matchDocument(String text) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
