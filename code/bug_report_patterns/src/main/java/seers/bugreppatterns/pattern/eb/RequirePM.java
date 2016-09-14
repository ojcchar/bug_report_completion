package seers.bugreppatterns.pattern.eb;

import java.util.List;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class RequirePM extends ExpectedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		if (tokens.size() > 3) {

			List<Integer> condTerms = SentenceUtils.findLemmasInTokens(CONDITIONAL_TERMS, tokens);

			if (condTerms.isEmpty()) {
				for (Token token : tokens) {
					if ((token.getPos().equals("VBZ") || token.getPos().equals("VBP") || token.getPos().equals("VBN"))
							&& token.getLemma().equals("require")) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

}
