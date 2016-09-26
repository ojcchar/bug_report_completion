package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorNounPhrasePM extends ObservedBehaviorPatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {

		List<Sentence> clauses = SentenceUtils.extractClauses(sentence);
		int numValidClauses = 0;
		for (Sentence clause : clauses) {

			// get sub-clauses (by ")
			List<Integer> punctuation = SentenceUtils.findLemmasInTokens(JavaUtils.getSet("\"", "``"),
					clause.getTokens());
			List<Sentence> subClauses = SentenceUtils.findSubSentences(clause, punctuation);

			for (Sentence subClause : subClauses) {

				// check for no prepositions
				List<Integer> prepositions = findPrepositions(subClause.getTokens());
				if (!prepositions.isEmpty()) {
					continue;
				}

				// no WDT tokens
				if (subClause.getTokens().stream().anyMatch(tok -> tok.getPos().equals("WDT"))) {
					continue;
				}

				// check for noun phrases
				if (checkErrorNounPhrase(subClause.getTokens()) != 0) {
					numValidClauses++;
					break;
				}
			}

		}

		if (numValidClauses > 0) {
			return 1;
		}

		return 0;
	}

	private List<Integer> findPrepositions(List<Token> tokens) {
		ArrayList<Integer> lemmaIndexesInTokens = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);

			boolean checkToken = true;

			// check for "a lot of"
			if (token.getLemma().equals("of")) {
				if ((i - 2 >= 0) && tokens.get(i - 2).getLemma().equals("a")
						&& tokens.get(i - 1).getLemma().equals("lot")) {
					checkToken = false;
				}
			}

			if (checkToken && SentenceUtils.lemmasContainToken(ProblemInPM.PREP_TERMS, token)) {
				lemmaIndexesInTokens.add(i);
			}

		}
		return lemmaIndexesInTokens;
	}

	public static int checkErrorNounPhrase(List<Token> tokens) {

		boolean containsNegativeNoun = false;
		for (int i = 0; i < tokens.size(); i++) {

			Token token = tokens.get(i);

			// negative nouns
			if (SentenceUtils.lemmasContainToken(NegativeTerms.NOUNS, token) && (token.getGeneralPos().equals("NN")
					|| token.getGeneralPos().equals("VB") || token.getGeneralPos().equals("CD"))) {
				containsNegativeNoun = true;
				break;
				// exceptions
			} else if (token.getLemma().matches("([A-Za-z0-9.]+)(exception)") && token.getGeneralPos().equals("NN")) {
				containsNegativeNoun = true;
				break;
				// errors
			} else if (token.getLemma().matches("([A-Za-z0-9.]+)(error)") && token.getGeneralPos().equals("NN")) {
				containsNegativeNoun = true;
				break;
			}
			// illegal
			// else
			// if (token.getLemma().matches("(illegal)([A-Za-z0-9.]+)") &&
			// token.getGeneralPos().equals("NN")) {
			// return 1;
			// }
			// negative adjectives
			else if (SentenceUtils.lemmasContainToken(NegativeTerms.ADJECTIVES, token)
					&& (token.getGeneralPos().equals("JJ") || token.getGeneralPos().equals("NN")
							|| token.getGeneralPos().equals("RB") || token.getGeneralPos().equals("VB"))) {
				if (tokens.size() > 1) {
					containsNegativeNoun = true;
					break;
				}

			}

			// stack trace
			else if (token.getLemma().equalsIgnoreCase("stack") && i + 1 < tokens.size()
					&& tokens.get(i + 1).getLemma().equalsIgnoreCase("trace")) {
				containsNegativeNoun = true;
				break;

				// missing labeled as VBG
			} else if (token.getLemma().equals("miss") && token.getPos().equals("VBG")) {
				containsNegativeNoun = true;
				break;
			}
		}

		if (containsNegativeNoun && !(tokens.get(tokens.size() - 1).getPos().equals("VBD")
				|| tokens.get(tokens.size() - 1).getPos().equals("VBN"))) {
			return 1;
		}
		return 0;
	}

	public static int checkErrorNounPhrase2(List<Token> tokens) {

		for (int i = 0; i < tokens.size(); i++) {

			Token token = tokens.get(i);

			// negative nouns
			if (SentenceUtils.lemmasContainToken(NegativeTerms.NOUNS, token) && (token.getGeneralPos().equals("NN")
					|| token.getGeneralPos().equals("VB") || token.getGeneralPos().equals("CD"))) {
				return 1;

				// exceptions
			} else if (token.getLemma().matches("([A-Za-z0-9.]+)(exception)") && token.getGeneralPos().equals("NN")) {
				return 1;

				// errors
			} else if (token.getLemma().matches("([A-Za-z0-9.]+)(error)") && token.getGeneralPos().equals("NN")) {
				return 1;

			}
			// illegal
			// else
			// if (token.getLemma().matches("(illegal)([A-Za-z0-9.]+)") &&
			// token.getGeneralPos().equals("NN")) {
			// return 1;
			// }
			// negative adjectives
			else if (SentenceUtils.lemmasContainToken(NegativeTerms.ADJECTIVES, token)
					&& (token.getGeneralPos().equals("JJ") || token.getGeneralPos().equals("NN")
							|| token.getGeneralPos().equals("RB") || token.getGeneralPos().equals("VB"))) {
				if (tokens.size() > 1) {
					return 1;
				}

			}

			// stack trace
			else if (token.getLemma().equalsIgnoreCase("stack") && i + 1 < tokens.size()
					&& tokens.get(i + 1).getLemma().equalsIgnoreCase("trace")) {
				return 1;

				// missing labeled as VBG
			} else if (token.getLemma().equals("miss") && token.getPos().equals("VBG")) {
				return 1;
			}
		}
		return 0;
	}

}
