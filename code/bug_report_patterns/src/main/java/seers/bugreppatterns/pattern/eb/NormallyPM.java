package seers.bugreppatterns.pattern.eb;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NormallyPM extends ExpectedBehaviorPatternMatcher {

	final public static Set<String> ADVERBS = JavaUtils.getSet("ideally", "perfectly", "normally", "preferably",
			"typically", "generally", "hopefully", "ordinarily", "popularly", "regularly", "usually", "widely",
			"habitually", "frequently", "commonly", "traditionally");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		if (tokens.size() > 1) {
			Token firstToken = tokens.get(0);
			if (firstToken.getGeneralPos().equals("RB") && SentenceUtils.lemmasContainToken(ADVERBS, firstToken)) {
				return 1;
			}
		}
		return 0;
	}

}
