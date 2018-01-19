package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class WhenAfterSentencePM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		boolean cond = checkConditional(sentence);
		if (cond) {
			boolean after = checkAfter(sentence);
			if (after) {
				return 1;
			}
		}
		return 0;
	}

	private boolean checkAfter(Sentence sentence) {

		List<Token> tokens = sentence.getTokens();
		List<Integer> idxs = findMainTokens(tokens, "after");
		return checkForClause(tokens, idxs);
	}

	private boolean checkForClause(List<Token> tokens, List<Integer> idxs) {
		for (Integer idx : idxs) {
			if (idx + 1 < tokens.size()) {
				Token nextTok = tokens.get(idx + 1);
				if (nextTok.getPos().equals("VBG")) {
					return true;
				} else {
					if (idx + 2 < tokens.size()) {
						Token nextTok2 = tokens.get(idx + 2);
						if (nextTok.getGeneralPos().equals("PRP") && nextTok2.getGeneralPos().equals("VB")) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	private List<Integer> findMainTokens(List<Token> tokens, String lemma) {
		return SentenceUtils.findLemmasInTokens(JavaUtils.getSet(lemma), tokens);
	}

	private boolean checkConditional(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();
		List<Integer> idxs = findMainTokens(tokens, "when");
		return checkForClause(tokens, idxs);
	}

}
