package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class GerundSubjectPM extends ObservedBehaviorPatternMatcher {

	final private static String[] VERBS_AS_NOUNS = { "copy", "drag", "insert" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		Token first = tokens.get(0);
		if (first.getPos().equals("VBG") || (first.getLemma().endsWith("ing") && first.getGeneralPos().equals("NN"))) {
			for (int i = 1; i < tokens.size(); i++) {
				Token token = tokens.get(i);
				if (token.getGeneralPos().equals("VB")) {
					return 1;
				} else if (token.getGeneralPos().equals("NN")
						&& Arrays.stream(VERBS_AS_NOUNS).anyMatch(t -> token.getLemma().contains(t))) {
					return 1;
				}
			}
		}
		return 0;
	}

}
