package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class NegativeVerbPM extends ObservedBehaviorPatternMatcher {

	private static final Pattern OTHER_NEGATIVE_VERBS_PATTERN = Pattern
			.compile(".* (slow doen|slow down|faile|stucks up|consume 100|get turn into|"
					+ "be out of|pull out|faul|hangs/get|failes|timing out|go away|be ([a-zA-Z]+ )?go|jitter|failing).*");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> clausesNoParethesis = SentenceUtils.breakByParenthesis(sentence);
		for (Sentence clause : clausesNoParethesis) {

			List<Sentence> subClauses = SentenceUtils.extractClauses(clause);
			for (Sentence subClause : subClauses) {

				// the sub-clauses
				List<Token> subClauseTokens = subClause.getTokens();
				for (int i = 0; i < subClauseTokens.size(); i++) {

					Token token = subClauseTokens.get(i);
					if (SentenceUtils.lemmasContainToken(NegativeTerms.VERBS, token)
							&& (token.getGeneralPos().equals("VB") || token.getGeneralPos().equals("NN")
									|| token.getGeneralPos().equals("JJ"))) {
						// no conditional clause
						// avoid cases: "if you delete"
						// if (!SentenceUtils.containsTermsPriorToIndex(i,
						// subClauseTokens, JavaUtils.getSet("if"))) {
						return 1;
						// }
					}
				}

				String txt = TextProcessor.getStringFromLemmas(subClause);
				Matcher otherVerbsMatcher = OTHER_NEGATIVE_VERBS_PATTERN.matcher(txt);
				if (otherVerbsMatcher.find()) {
					return 1;
				}
			}
		}

		return 0;
	}
}
