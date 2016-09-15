package seers.bugreppatterns.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.ob.ButNegativePM;
import seers.bugreppatterns.pattern.ob.ConditionalNegativePM;
import seers.bugreppatterns.pattern.ob.NegativeAdjOrAdvPM;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.pattern.ob.NegativeVerbPM;
import seers.bugreppatterns.pattern.ob.NothingHappensPM;
import seers.bugreppatterns.pattern.ob.NoticePM;
import seers.bugreppatterns.pattern.ob.PassiveVoicePM;
import seers.bugreppatterns.pattern.ob.StillSentencePM;
import seers.bugreppatterns.pattern.ob.TimeAdverbPositivePM;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

/**
 * @author ojcch
 *
 */
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

	/**
	 * Extract clauses or subsentences in the provided sentence, based on
	 * CLAUSE_SEPARATORS and Coordinating conjuctions such as "and", "or",
	 * "but", etc.
	 * 
	 * @param tokens
	 * @return
	 */
	public static List<Sentence> extractClauses(Sentence sentence) {

		List<Token> tokens = sentence.getTokens();

		List<Sentence> clauses = new ArrayList<>();
		List<Token> clauseTokens = new ArrayList<>();

		int currentClause = 0;
		for (int i = 0; i < tokens.size();) {
			Token token = tokens.get(i);
			if (matchTermsByLemma(CLAUSE_SEPARATORS, token) || token.getPos().equals("CC")) {

				// ---------------------

				boolean allowSplit = true;
				int numNextTokens = 1;

				// avoid splits when there are '->'
				if (token.getLemma().equals("-")
						&& (i + 1 < tokens.size() && tokens.get(i + 1).getLemma().equals(">"))) {
					allowSplit = false;

					// avoid splits when there are html codes: &#x633;
				} else if (token.getLemma().equals("&") && checkHtmlCode(tokens, i)) {
					allowSplit = false;
					numNextTokens = 4;
				}

				// -----------------------------

				if (allowSplit) {
					clauses.add(new Sentence(sentence.getId() + "." + currentClause++, clauseTokens));
					clauseTokens = new ArrayList<>();
					i++;
				} else {

					// add tokens to the clause, depending on the next # of
					// tokens to add
					for (int j = i; j < i + numNextTokens; j++) {
						clauseTokens.add(tokens.get(j));
					}
					i += numNextTokens;
				}
			} else {
				clauseTokens.add(token);
				i++;
			}
		}

		if (!clauseTokens.isEmpty()) {
			clauses.add(new Sentence(sentence.getId() + "." + currentClause++, clauseTokens));
		}

		return clauses;

	}

	private static boolean checkHtmlCode(List<Token> tokens, int ampersandIdx) {
		int toIdx = tokens.size();
		if (ampersandIdx + 4 < tokens.size()) {
			toIdx = ampersandIdx + 4;
		}
		String text = TextProcessor.getStringFromLemmas(new Sentence("0", tokens.subList(ampersandIdx, toIdx)));
		return text.matches("\\& # \\d+ ;") || text.matches("\\& #[xX] [0-9a-fA-F]+ ;");
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
	 * @param lemmas
	 *            set of lemmas to compare against with
	 * @param token
	 *            the token to match
	 * @return true if the token matches any of the lemmas, false otherwise
	 */
	public static boolean lemmasContainToken(Set<String> lemmas, Token token) {
		return lemmas.stream().anyMatch(t -> token.getLemma().equalsIgnoreCase(t));
	}

	/**
	 * Finds the indexes of the tokens whose lemmas match the given set of
	 * lemmas
	 * 
	 * @param lemmas
	 *            the lemmas to find in the tokens
	 * @param tokens
	 *            the tokens to analyze
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
	 * 
	 * @param tokens
	 * @param lemmas
	 * @return true if any of the tokens's lemmas matches the lemmas in the set,
	 *         false otherwise
	 */
	public static boolean tokensContainAnyLemmaIn(List<Token> tokens, Set<String> lemmas) {
		return !SentenceUtils.findLemmasInTokens(lemmas, tokens).isEmpty();
	}

	/**
	 * Checks whether any of the sentence's tokens matches the lemmas in the set
	 * 
	 * @param sentence
	 * @param lemmas
	 * @return true if any of the sentence's tokens matches the lemmas in the
	 *         set, false otherwise
	 */
	public static boolean sentenceContainsAnyLemmaIn(Sentence sentence, Set<String> lemmas) {
		return SentenceUtils.tokensContainAnyLemmaIn(sentence.getTokens(), lemmas);
	}

	/**
	 * Divides the sentence into subsentences according to the indexes provided
	 * in separatorIndexes. The subSentences do not include the tokens given by
	 * the separatorIndexes.
	 * 
	 * @param sentence
	 * @param separatorIndexes
	 * @return
	 */
	public static List<Sentence> findSubSentences(Sentence sentence, List<Integer> separatorIndexes) {
		List<Sentence> subSentences = new ArrayList<Sentence>();
		if (separatorIndexes.isEmpty()) {
			subSentences.add(sentence);
		} else {
			for (int i = 0; i <= separatorIndexes.size(); i++) {
				int start = i == 0 ? 0 : separatorIndexes.get(i - 1) + 1;
				int end = i == separatorIndexes.size() ? sentence.getTokens().size() : separatorIndexes.get(i);
				if (end > start) {
					Sentence subSentence = new Sentence(sentence.getId(), sentence.getTokens().subList(start, end));
					subSentences.add(subSentence);
				}
			}
		}
		return subSentences;
	}

	// ----------------------------------------

	public final static Set<String> UNDETECTED_VERBS = JavaUtils.getSet("show", "boomark", "rename", "run", "select",
			"post", "stop", "goto", "enter", "drag", "check", "file", "try", "build", "install", "type", "use", "start",
			"paste", "surf", "right-click", "import", "scroll");

	/**
	 * Check if the sentence/clause is imperative or not. It takes into account
	 * labels at the beginning of the sentence, such as "exp. behavior: run the
	 * program"
	 * 
	 * @param tokens
	 * @return
	 */
	public static boolean isImperativeSentence(Sentence sentence) {
		return isImperativeSentence(sentence.getTokens());
	}

	/**
	 * Check if the sentence/clause (represented by its list of tokens) is
	 * imperative or not. It takes into account labels at the beginning of the
	 * sentence, such as "exp. behavior: run the program"
	 * 
	 * @param tokens
	 * @return
	 */
	public static boolean isImperativeSentence(List<Token> tokens) {

		// --------------------------------

		if (checkForImperativeTokens(tokens)) {
			return true;
		}

		// ------------------------

		// check for labels in the first labelLenght terms: find the token ":"
		final int labelLenght = 5;
		int idx = -1;
		for (int i = 0; i < tokens.size() && i <= labelLenght; i++) {

			Token token = tokens.get(i);
			if (token.getLemma().equals(":")) {
				idx = i;
			}
		}

		// if the ":" is found, check for the imperative tokens
		if (idx != -1) {
			if (idx + 2 < tokens.size()) {
				return checkForImperativeTokens(tokens.subList(idx + 1, tokens.size()));
			}
		}

		return false;

	}

	/**
	 * Check if the combination list of tokens matches the usual wording for an
	 * imperative sentence: (adverb +) inf. verb + ...
	 * 
	 * @param tokens
	 * @return
	 */
	private static boolean checkForImperativeTokens(List<Token> tokens) {

		// make sure we discard non-word tokens at the beginning of the sentence
		int idx = -1;
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getLemma().matches("^\\w.*")) {
				idx = i;
				break;
			}

		}

		// the sentence is full of non-word tokens
		if (idx == -1) {
			return false;
		}

		// work only with word tokens
		List<Token> tokensNoSpecialChar = tokens.subList(idx, tokens.size());

		// -----------------------------------

		if (tokensNoSpecialChar.size() < 2) {
			return false;
		}

		Token firstToken = tokensNoSpecialChar.get(0);
		Token secondToken = tokensNoSpecialChar.get(1);

		// regular case, the sentence starts with a verb
		if ((firstToken.getPos().equals("VB") || firstToken.getPos().equals("VBP"))) {
			return true;
		} else {

			// case: the sentence starts with an adverb and then with a verb
			if (secondToken != null) {
				if (firstToken.getPos().equals("RB")
						&& (secondToken.getPos().equals("VB") || secondToken.getPos().equals("VBP"))
						&& tokensNoSpecialChar.size() > 2) {
					return true;
				}
			}

			// case: the first token is an undetected verb
			if (SentenceUtils.lemmasContainToken(UNDETECTED_VERBS, firstToken)) {
				return true;
			}
		}
		return false;
	}

	// -------------------------------------------------------------------------

	public static final ObservedBehaviorPatternMatcher[] OB_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new ButNegativePM(), new ConditionalNegativePM(), new NegativeAdjOrAdvPM(), new StillSentencePM(),
			new PassiveVoicePM(), new TimeAdverbPositivePM(), new NoticePM(), new NothingHappensPM() };

	/**
	 * Finds the first Observed Behavior (OB) sentence in the list of sentences
	 * provided, matched by any of the default OB pattern matchers
	 * 
	 * @param sentences
	 * @param patterns
	 * @return index of the 1st OB sentence found in the list or -1 otherwise
	 * @throws Exception
	 */
	public static int findObsBehaviorSentence(List<Sentence> sentences) throws Exception {
		return findObsBehaviorSentence(sentences, OB_PMS);
	}

	/**
	 * Finds the first Observed Behavior (OB) sentence in the list of sentences
	 * provided, matched by any of the OB pattern matchers
	 * 
	 * @param sentences
	 * @param patterns
	 * @return index of the 1st OB sentence found in the list or -1 otherwise
	 * @throws Exception
	 */
	public static int findObsBehaviorSentence(List<Sentence> sentences, ObservedBehaviorPatternMatcher[] patterns)
			throws Exception {
		for (int i = sentences.size() - 1; i >= 0; i--) {
			Sentence sentence = sentences.get(i);

			for (ObservedBehaviorPatternMatcher pm : patterns) {
				if (pm.matchSentence(sentence) == 1) {
					return i;
				}
			}
		}
		return -1;
	}

}
