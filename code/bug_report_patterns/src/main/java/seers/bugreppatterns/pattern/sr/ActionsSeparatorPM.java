package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.ob.LeadsToNegativePm;
import seers.bugreppatterns.pattern.ob.LeadsToPM;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.pattern.ob.NegativeVerbPM;
import seers.bugreppatterns.pattern.ob.VerbErrorPM;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ActionsSeparatorPM extends StepsToReproducePatternMatcher {

	public static final ObservedBehaviorPatternMatcher[] OB_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new VerbErrorPM(), new LeadsToPM(), new LeadsToNegativePm() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Token> tokens = sentence.getTokens();

		List<List<Token>> clauses = MenuNavigationPM.extractClauses(tokens, MenuNavigationPM.SEPARATORS);

		int lastImperClause = -1;
		int numImperClause = 0;
		for (int i = 0; i < clauses.size(); i++) {
			List<Token> clause = clauses.get(i);
			boolean isImperative = SentenceUtils.isImperativeSentence(clause, false);
			if (isImperative) {
				lastImperClause = i;
				numImperClause++;
			}
		}

		if (lastImperClause != -1 && clauses.size() > 1) {

			// check for OB clause at the end of the sentence
			List<Token> lastClauseTokens = clauses
					.get(lastImperClause == clauses.size() - 1 ? lastImperClause : lastImperClause + 1);

			Sentence lastSentence = new Sentence("0", lastClauseTokens);

			// System.out.println(lastSentence);

			for (ObservedBehaviorPatternMatcher pm : OB_PMS) {
				if (pm.matchSentence(lastSentence) == 1) {
					return 1;
				}
			}

			String text = TextProcessor.getStringFromLemmas(lastSentence);
			if (text.matches(".*there be.*") || text.matches(".* close$")) {
				return 1;
			}

			if (numImperClause == clauses.size()) {
				return 1;
			}
		}

		return 0;
	}

}
