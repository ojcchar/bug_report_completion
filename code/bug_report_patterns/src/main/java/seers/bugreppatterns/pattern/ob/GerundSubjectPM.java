package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class GerundSubjectPM extends ObservedBehaviorPatternMatcher {

	final private static Set<String> VERBS_AS_NOUNS = JavaUtils.getSet("copy", "drag", "insert" );

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
						&& SentenceUtils.lemmasContainToken(VERBS_AS_NOUNS, token)) {
					return 1;
				}
			}
		}
		return 0;
	}

}
