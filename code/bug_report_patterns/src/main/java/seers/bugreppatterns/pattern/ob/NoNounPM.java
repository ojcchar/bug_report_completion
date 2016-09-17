package seers.bugreppatterns.pattern.ob;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NoNounPM extends ObservedBehaviorPatternMatcher {

	private final static Set<String> NO_TERMS = JavaUtils.getSet("no", "nothing", "none", "neither");

	private final static String[] POST_NO_TERMS = { "NN", "VB", "DT", "MD", "IN", "JJ", "RP", "WH", "``" };

	private final static String[] PRE_NO_POS = { "VB" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		int start = 0;
		while (start < tokens.size()) {
			Token current = tokens.get(start);
			String previousGenPos = start == 0 ? "OK" : tokens.get(start - 1).getGeneralPos();
			// if there is a verb before the NO_TERM, the pattern is S_OB_VERB_NO
			if (SentenceUtils.lemmasContainToken(NO_TERMS, current)
					&& !Arrays.stream(PRE_NO_POS).anyMatch(t -> previousGenPos.equals(t))) {
				return matchSubSentence(new Sentence(sentence.getId(), tokens.subList(start, tokens.size())));
			}
			start++;
		}

		return 0;

	}

	public int matchSubSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		if (tokens.size() > 2) {
			if (SentenceUtils.lemmasContainToken(NO_TERMS, tokens.get(0))) {
				Token nextToken = tokens.get(1);
				// Check the sentence is not S_OB_NO_LONGER
				if (tokens.get(0).getWord().equalsIgnoreCase("no") && nextToken.getWord().equalsIgnoreCase("longer")) {
					return 0;
				} else if (Arrays.stream(POST_NO_TERMS).anyMatch(t -> nextToken.getGeneralPos().equals(t))) {
					return 1;
				}
			}
		}

		return 0;
	}

}
