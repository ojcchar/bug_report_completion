package seers.bugreppatterns.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class SentenceUtils {

	public static boolean isQuestion(Sentence sentence) {

		List<Token> tokens = sentence.getTokens();
		if (tokens.isEmpty()) {
			return false;
		}
		if (tokens.get(tokens.size() - 1).getLemma().equals("?")) {
			return true;
		} else if (tokens.size() > 1 && tokens.get(tokens.size() - 1).getLemma().equals("?")) {
			return true;
		}
		return false;
	}

	/**
	 * Parse a sentence into tokens
	 * 
	 * @param sentenceId
	 * @param sentenceTxt
	 * @return null if it fails to parse the sentence
	 */
	public static Sentence parseSentence(String sentenceId, String sentenceTxt) {

		String sentenceEscaped = sentenceTxt;
		// -----------------------------------------

		// FIXME: remove labels from the systems???
		// if ("firefox".equals(system)) {
		//
		// // -----------------------------
		// if (sentenceTxt.matches("(?s)(?i)(actual)( (results|result))?:(.*)"))
		// {
		// sentenceEscaped = sentenceTxt.replaceFirst("(?i)(actual)(
		// (results|result))?:(.*)", "$4");
		// } else if (sentenceTxt.matches("(?s)(?i)(actual)(
		// (result|results))(.*)")) {
		// sentenceEscaped = sentenceTxt.replaceFirst("(?i)(actual)(
		// (results|result))(.*)", "$4");
		// } else
		// // -----------------------------
		//
		// if (sentenceTxt.matches("(?s)(?i)(expected)(
		// (results|result))?:(.*)")) {
		// sentenceEscaped = sentenceTxt.replaceFirst("(?i)(expected)(
		// (results|result))?:(.*)", "$4");
		// } else if (sentenceTxt.matches("(?s)(?i)(expected)(
		// (results|result))?(.*)")) {
		// sentenceEscaped = sentenceTxt.replaceFirst("(?i)(expected)(
		// (results|result))?(.*)", "$4");
		// } else
		//
		// // -----------------------------
		// if (sentenceTxt.matches("(?s)(?i)(str|((steps)( to
		// reproduce)?)):(.*)")) {
		// sentenceEscaped = sentenceTxt.replaceFirst("(?i)(str|((steps)( to
		// reproduce)?)):(.*)", "$5");
		// } else if (sentenceTxt.matches("(?s)(?i)(str|((steps)( to
		// reproduce)?))(.*)")) {
		// sentenceEscaped = sentenceTxt.replaceFirst("(?i)(str|((steps)( to
		// reproduce)?))(.*)", "$5");
		// }
		// }

		// LOGGER.debug("STNC: " + sentenceTxt + " -> " +
		// sentenceEscaped);

		if (sentenceEscaped.trim().isEmpty()) {
			return null;
		}

		// -----------------------------------------

		List<Sentence> sentences = TextProcessor.processText(sentenceEscaped, true);

		if (!sentences.isEmpty()) {

			List<Token> allTokens = TextProcessor.getAllTokens(sentences);

			// if (allTokens.isEmpty()) {
			// LOGGER.debug("No tokens for bug: " + bugId + ", par.: " +
			// par.getId() + ", sent: "
			// + descSentence.getId());
			// continue;
			// }

			Sentence sentence = new Sentence(sentenceId, allTokens, sentenceEscaped);

			return sentence;
		}

		return null;
	}

	public static final String[] CLAUSE_SEPARATORS = { ";", ",", "-", "_", "--" };

	public static List<List<Token>> extractClauses(List<Token> tokens) {

		List<List<Token>> clauses = new ArrayList<>();
		List<Token> clause = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (matchTermsByLemma(CLAUSE_SEPARATORS, token) || token.getPos().equals("CC")) {

				// ---------------------

				// avoid splits when there are '->'
				boolean allowSeparator = true;
				if (token.getLemma().equals("-")) {
					if (i + 1 < tokens.size() && tokens.get(i + 1).getLemma().equals(">")) {
						allowSeparator = false;
					}
				}

				// -----------------------------

				if (allowSeparator) {
					clauses.add(clause);
					clause = new ArrayList<>();
				} else {
					clause.add(token);
				}
			} else {
				clause.add(token);
			}
		}
		if (!clause.isEmpty()) {
			clauses.add(clause);
		}

		return clauses;

	}

	/**
	 * Matches any of the given terms with the token's lemma (case ignored)
	 * 
	 * @param terms
	 * @param token
	 * @return true if there is any match, false otherwise
	 */
	public static boolean matchTermsByLemma(String[] terms, Token token) {
		return Arrays.stream(terms).anyMatch(t -> token.getLemma().equalsIgnoreCase(t));
	}

	/**
	 * Checks whether the token's lemma matches any of the strings in lemmas
	 * 
	 * @param lemmas set of lemmas to compare against with
	 * @param token the token to match
	 * @return true if the token matches any of the lemmas, false otherwise
	 */
	public static boolean lemmasContainToken(Set<String> lemmas, Token token) {
		return lemmas.stream().anyMatch(t -> token.getLemma().equalsIgnoreCase(t));
	}

	/**
	 * Finds the indexes of the tokens whose lemmas match the given set of lemmas
	 * @param lemmas the lemmas to find in the tokens
	 * @param tokens the tokens to analyze
	 * @return the indexes of the tokens matching with the lemmas
	 */
	public static List<Integer> findLemmasInTokens(Set<String> lemmas, List<Token> tokens) {
		ArrayList<Integer> lemmaIndexesInTokens = new ArrayList<Integer>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (SentenceUtils.lemmasContainToken(lemmas, token)) {
				lemmaIndexesInTokens.add(i);
			}
		}
		return lemmaIndexesInTokens;
	}

	/**
	 * Checks whether any of the tokens' lemmas matches the lemmas in the set
	 * @param tokens
	 * @param lemmas
	 * @return true if any of the tokens's lemmas matches the lemmas in the set, false otherwise
	 */
	public static boolean tokensContainAnyLemmaIn(List<Token> tokens, Set<String> lemmas) {
		return !SentenceUtils.findLemmasInTokens(lemmas, tokens).isEmpty();
	}

	/**
	 * Checks whether any of the sentence's tokens matches the lemmas in the set
	 * @param sentence
	 * @param lemmas
	 * @return true if any of the sentence's tokens matches the lemmas in the set, false otherwise
	 */
	public static boolean sentenceContainsAnyLemmaIn(Sentence sentence, Set<String> lemmas) {
		return SentenceUtils.tokensContainAnyLemmaIn(sentence.getTokens(), lemmas);
	}

}
