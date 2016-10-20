package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * Matcher for S_SR_PURPOSE_ACTION.
 */
public class PurposeActionPM extends StepsToReproducePatternMatcher {
	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		if (clauses.size() == 2) {
			List<Token> firstTokens = clauses.get(0).getTokens();

			if (firstTokens.size() >= 2 && firstTokens.get(0).getPos().equals("TO") && firstTokens.get(1).getPos().equals("VB")) {

				// Loop the second clause until we find a verb that works
				boolean isImper = SentenceUtils.isImperativeSentence(clauses.get(1));
				if (isImper) {
					return 1;
				}
//				for (Token token : clauses.get(1).getTokens()) {
//					if (token.getGeneralPos().equals("VB")) {
//						if (token.getPos().equals("VB") || token.getPos().equals("VBP")
//								|| token.getPos().equals("VBD")) {
//							return 1;
//						} else {
//							break;
//						}
//					}
//				}
			}
		}

		return 0;
	}
}
