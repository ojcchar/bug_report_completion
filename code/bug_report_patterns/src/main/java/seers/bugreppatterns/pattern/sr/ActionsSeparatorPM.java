package seers.bugreppatterns.pattern.sr;

import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.ob.ErrorNounPhrasePM;
import seers.bugreppatterns.pattern.ob.LeadsToNegativePm;
import seers.bugreppatterns.pattern.ob.LeadsToPM;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.pattern.ob.NegativeVerbPM;
import seers.bugreppatterns.pattern.ob.VerbErrorPM;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;

public class ActionsSeparatorPM extends StepsToReproducePatternMatcher {

	public static final ObservedBehaviorPatternMatcher[] OB_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new VerbErrorPM(), new LeadsToPM(), new LeadsToNegativePm(), new ErrorNounPhrasePM() };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> clauses = SentenceUtils.extractClausesBySeparators(sentence, MenuNavigationPM.SEPARATORS);

		int lastImperClause = -1;
		int numImperClause = 0;
		for (int i = 0; i < clauses.size(); i++) {
			Sentence clause = clauses.get(i);
			boolean isImperative = SentenceUtils.isImperativeSentence(clause, false);
			if (isImperative) {
				lastImperClause = i;
				numImperClause++;
			}
		}
		
		if (lastImperClause != -1 && clauses.size() > 1) {

			// check for OB clause at the end of the sentence
			Sentence lastSentence = clauses
					.get(lastImperClause == clauses.size() - 1 ? lastImperClause : lastImperClause + 1);

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
