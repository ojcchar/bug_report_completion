package seers.bugreppatterns.pattern.eb;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ExpectedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ShouldPM extends ExpectedBehaviorPatternMatcher {

	private static final Set<String> MODAL_TERMS = JavaUtils.getSet("should", "shall");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		// extract clauses
		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);

		int numValidClauses = 0;
		for (Sentence clause : clauses) {

			List<Token> tokens = clause.getTokens();

			// find modals
			List<Integer> idxs = SentenceUtils.findLemmasInTokens(MODAL_TERMS, tokens);
			if (idxs.isEmpty()) {
				continue;
			}

			// no negative auxiliary subclauses before the modal
			Integer firstIdx = idxs.get(0);
			NegativeAuxVerbPM pm = new NegativeAuxVerbPM();
			int matchSentence = pm.matchSentence(new Sentence(clause.getId(), tokens.subList(0, firstIdx)));
			if (matchSentence == 1) {
				continue;
			}

			// no "should be done"
			if (firstIdx + 2 < tokens.size()) {

				Token nextToken1 = tokens.get(firstIdx + 1);
				Token nextToken2 = tokens.get(firstIdx + 2);

				if (nextToken1.getLemma().equals("be") && nextToken2.getLemma().equals("do")) {
					continue;

				}

			}

			numValidClauses++;
		}

		if (numValidClauses > 0) {
			return 1;
		}

		return 0;

	}

}
