package seers.bugreppatterns.pattern.eb;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NormallyPM extends ExpectedBehaviorPatternMatcher {

	final public static String[] ADVERBS = { "ideally", "perfectly", "normally", "preferably", "typically", "generally",
			"hopefully", "ordinarily", "popularly", "regularly", "usually", "widely", "habitually", "frequently",
			"commonly", "traditionally" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		if (tokens.size() > 1) {
			Token firstToken = tokens.get(0);
			if (firstToken.getGeneralPos().equals("RB")
					&& Arrays.stream(ADVERBS).anyMatch(t -> firstToken.getLemma().equals(t))) {
				return 1;
			}
		}
		return 0;
	}

}
