package seers.bugreppatterns.pattern.eb;

import java.util.List;
import java.util.Optional;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ShouldPM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		Optional<Token> first = tokens.stream().filter(t -> "MD".equals(t.getPos()) && "should".equals(t.getLemma()))
				.findFirst();
		return first.isPresent() ? 1 : 0;
	}

}
