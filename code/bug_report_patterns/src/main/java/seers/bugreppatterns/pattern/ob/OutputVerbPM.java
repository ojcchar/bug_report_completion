package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for S_OB_OUTPUT_VERB
 */
public class OutputVerbPM extends ObservedBehaviorPatternMatcher {
	public static final Set<String> OUTPUT_VERBS = JavaUtils.getSet("output", "display", "show", "return", "report",
			"pop");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		List<Integer> verbs = getVerbs(tokens);
		for (Integer verbIdx : verbs) {
			Token verbToken = tokens.get(verbIdx);
			if (verbToken.getGeneralPos().equals("VB")) {
				return 1;
			} else {
				if (verbIdx - 1 >= 0 && verbIdx + 1 < tokens.size()) {
					Token prevToken = tokens.get(verbIdx - 1);
					Token nextToken = tokens.get(verbIdx + 1);

					// no surrounding verbs
					// avoid cases like "the output"
					// avoid cases like "output display goes "
					if (!prevToken.getGeneralPos().equals("VB") && !nextToken.getGeneralPos().equals("VB")
							&& !prevToken.getGeneralPos().equals("DT")
							&& !SentenceUtils.lemmasContainToken(OUTPUT_VERBS, prevToken)
							&& !SentenceUtils.lemmasContainToken(OUTPUT_VERBS, nextToken)) {

						// avoid "is not output"
						if (verbIdx - 2 >= 0) {
							Token prevToken2 = tokens.get(verbIdx - 2);
							if (!prevToken2.getLemma().equals("be") && !prevToken.getLemma().equals("not")) {
								return 1;
							}
						} else {
							return 1;
						}
					}
				}
			}
		}

		return 0;
	}

	private List<Integer> getVerbs(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(OUTPUT_VERBS, tokens);
	}
}
