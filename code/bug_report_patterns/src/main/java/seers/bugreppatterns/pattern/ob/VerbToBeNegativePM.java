package seers.bugreppatterns.pattern.ob;

import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class VerbToBeNegativePM extends ObservedBehaviorPatternMatcher {

	final private static Set<String> NEGATIVE_TERMS = JavaUtils.getSet("no", "nothing");

	public final static Set<String> SUBJECT_TERMS = JavaUtils.getSet("there", "these", "this", "here", "it");

	public final static PatternMatcher[] NEGATIVE_PMS = { new ErrorNounPhrasePM() };

	final private static Set<String> POS_LEMMAS = JavaUtils.getSet("MD-can", "MD-would", "MD-will", "MD-could",
			"MD-may");

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
		for (Sentence clause : clauses) {
			List<Token> clauseTokens = clause.getTokens();

			// split sentences by prepositions
			List<Integer> prep = SentenceUtils.findLemmasInTokens(ProblemInPM.PREP_TERMS, clauseTokens);
			List<Sentence> subClauses = SentenceUtils.findSubSentences(clause, prep);

			for (Sentence subClause : subClauses) {

				List<Token> subClauseTokens = subClause.getTokens();

				// find the subjects in the sub-clause
				List<Integer> subjIdx = findSubjects(subClauseTokens);

				for (Integer subj : subjIdx) {

					// is the sub-clause long enough?
					if (subj + 2 >= subClauseTokens.size()) {
						continue;
					}

					Token nextToken = subClauseTokens.get(subj + 1);
					int indxNxtTok2 = 2;

					// if the current token is modal, then check the next token
					if (isModal(nextToken)) {
						nextToken = subClauseTokens.get(subj + 2);
						indxNxtTok2 = 3;
					}

					// enough tokens?
					if (subj + indxNxtTok2 >= subClauseTokens.size()) {
						continue;
					}

					// verb to-be?
					if (nextToken.getLemma().equals("be")) {

						Token nextToken2 = subClauseTokens.get(subj + indxNxtTok2);

						// case: there is no/nothing
						if (SentenceUtils.lemmasContainToken(NEGATIVE_TERMS, nextToken2)) {
							return 1;
						} else {

							// case: there is a bug/error/etc.
							Sentence newClause = new Sentence(sentence.getId(),
									subClauseTokens.subList(subj + indxNxtTok2, subClauseTokens.size()));
							if (isNegative(newClause)) {
								return 1;
							} else
							// case: there are differences...
							if (newClause.getTokens().stream()
									.anyMatch(tok -> tok.getLemma().equalsIgnoreCase("difference"))) {
								return 1;
							}
						}
					}

				}
			}
		}
		return 0;
	}

	private boolean isModal(Token auxToken) {
		String posLemma = auxToken.getGeneralPos() + "-" + auxToken.getLemma().toLowerCase();
		return POS_LEMMAS.stream().anyMatch(p -> posLemma.equals(p));
	}

	private List<Integer> findSubjects(List<Token> tokens) {
		return SentenceUtils.findLemmasInTokens(SUBJECT_TERMS, tokens);
	}

	private boolean isNegative(Sentence sentence) throws Exception {
		return sentenceMatchesAnyPatternIn(sentence, NEGATIVE_PMS);
	}

}
