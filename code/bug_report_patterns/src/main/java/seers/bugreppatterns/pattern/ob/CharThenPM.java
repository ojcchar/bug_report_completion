package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class CharThenPM extends ObservedBehaviorPatternMatcher {

	public final static Set<String> ARROW_TAIL = JavaUtils.getSet("=", "==", "-", "--");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		if (findArrow(sentence.getTokens()) && !isEBModal(sentence.getTokens())) {
			return 1;
		}

		return 0;
	}

	private boolean findArrow(List<Token> tokens) {
		for (int i = 0; i < tokens.size(); i++) {
			Token current = tokens.get(i);

			if (current.getGeneralPos().equals("JJ")
					&& (current.getLemma().equals(">") || current.getLemma().equals("&gt;")) && i - 1 >= 0
					&& i + 1 < tokens.size()) {
				Token previous = tokens.get(i - 1);
				if (SentenceUtils.lemmasContainToken(ARROW_TAIL, previous)) {
					if (i - 2 >= 0) {
						Token beforePrevious = tokens.get(i - 2);
						Token next = tokens.get(i + 1);
						if (isNavMenu(beforePrevious, next)) {
							return false;
						}
					}
					return true;
				}
			}

		}

		return false;
	}

	private boolean isNavMenu(Token beforeArrow, Token afterArrow) {

		if ((beforeArrow.getPos().equals("NNP") || beforeArrow.getWord().matches("[A-Z][A-Za-z]+")
				|| beforeArrow.getLemma().matches("[^A-Za-z]+"))
				&& (afterArrow.getPos().equals("NNP") || afterArrow.getWord().matches("[A-Z][A-Za-z]+")
						|| afterArrow.getLemma().matches("[^A-Za-z]+")))
			return true;
		return false;
	}
}
