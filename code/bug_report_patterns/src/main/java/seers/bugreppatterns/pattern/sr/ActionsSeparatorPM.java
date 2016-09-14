package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ActionsSeparatorPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		List<List<Token>> clauses = MenuNavigationPM.extractClauses(tokens, MenuNavigationPM.SEPARATORS);

		int numClauses = 0;
		for (List<Token> clause : clauses) {
			boolean isImperative = SentenceUtils.isImperativeSentence(clause);
			if (isImperative) {
				numClauses++;
			}
		}

		// TODO: check for OB clause at the end of the sentence
		if (numClauses > 0 && clauses.size() > 1) {
			return 1;
		}

		return 0;
	}

}
