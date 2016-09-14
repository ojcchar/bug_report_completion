package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ExceptPM extends ObservedBehaviorPatternMatcher {

	private static final Set<String> EXCEPT = JavaUtils.getSet("except" );

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		for (Integer except : findExcepts(tokens)) {

			if (except == 0) {
				if (!isEBModal(sentence.getTokens())) {
					return 1;
				}

			} else if (except > 1) {
				Sentence before = new Sentence(sentence.getId(), tokens.subList(0, except));
				if (!isEBModal(before.getTokens())) {
					return 1;
				}
			}
		}
		return 0;
	}

	private List<Integer> findExcepts(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(EXCEPT, tokens);
	}

}
