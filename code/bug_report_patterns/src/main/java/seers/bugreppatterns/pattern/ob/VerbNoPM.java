package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class VerbNoPM extends ObservedBehaviorPatternMatcher {

	final private static Set<String> NEGATIVE_TERMS = JavaUtils.getSet( "no", "nothing"
			// ,"not", "never"
	);

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		List<Integer> verbs = findVerbs(tokens);

		for (Integer verb : verbs) {

			for (int i = verb + 1; i <= verb + 4 && i < tokens.size(); i++) {
				Token nextToken = tokens.get(i);
				if (SentenceUtils.lemmasContainToken(NEGATIVE_TERMS,nextToken)) {
					return 1;
				}
			}
		}

		return 0;

	}

	private List<Integer> findVerbs(List<Token> tokens) {

		List<Integer> verbs = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")
					|| SentenceUtils.lemmasContainToken(SentenceUtils.UNDETECTED_VERBS, token)) {
				verbs.add(i);
			}
		}
		return verbs;
	}
}
