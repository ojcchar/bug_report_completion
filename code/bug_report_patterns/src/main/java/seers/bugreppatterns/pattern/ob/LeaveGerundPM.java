package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class LeaveGerundPM extends ObservedBehaviorPatternMatcher {

	private static final Set<String> PUNCTUATION = JavaUtils.getSet(":", ";");

	private static final String LEAVE = "leave";

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		for (Sentence ss : SentenceUtils.breakByParenthesis(sentence)) {

			List<Integer> leaveIndexes = findLeaves(ss.getTokens());

			if (!leaveIndexes.isEmpty() && !isEBModal(ss.getTokens())) {
				List<Sentence> subSentences = SentenceUtils.findSubSentences(ss, leaveIndexes);

				for (Sentence subSentence : subSentences) {
					if (containsGerund(subSentence)) {
						return 1;
					}
				}
			}
		}
		return 0;
	}

	private boolean containsGerund(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();
		for (int i = 1; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getPos().equals("VBG")
					|| (token.getWord().toLowerCase().endsWith("ing") && token.getGeneralPos().equals("NN"))) {
				return true;
			} else if (SentenceUtils.lemmasContainToken(PUNCTUATION, token)) {
				return false;
			}
		}
		return false;
	}

	private List<Integer> findLeaves(List<Token> tokens) {
		List<Integer> indexes = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			if (current.getLemma().equals(LEAVE)
					&& (current.getPos().equals("VBP") || current.getPos().equals("VBZ"))) {
				indexes.add(i);
			}
		}
		return indexes;
	}
}
