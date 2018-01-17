package seers.bugreppatterns.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.ImmutablePair;

import seers.appcore.utils.ExceptionUtils;
import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.bugreppatterns.pattern.PatternMatcher;
import seers.bugreppatterns.pattern.ob.ButNegativePM;
import seers.bugreppatterns.pattern.ob.ConditionalNegativePM;
import seers.bugreppatterns.pattern.ob.NegativeAdjOrAdvPM;
import seers.bugreppatterns.pattern.ob.NegativeAuxVerbPM;
import seers.bugreppatterns.pattern.ob.NegativeVerbPM;
import seers.bugreppatterns.pattern.ob.NothingHappensPM;
import seers.bugreppatterns.pattern.ob.NoticePM;
import seers.bugreppatterns.pattern.ob.PassiveVoicePM;
import seers.bugreppatterns.pattern.ob.SimplePresentPM;
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

	public static final Set<String> CLAUSE_SEPARATORS = JavaUtils.getSet(";", ",", "-", "_", "--", ":");
	public static final Set<String> POS_SEPARATORS = JavaUtils.getSet("CC");
	public static final Set<String> TERM_SEPARATORS = JavaUtils.getSet("and", "or", "but");

	/**
	 * Extract clauses or subsentences in the provided sentence, based on
	 * 2-tokens clause separators
	 * 
	 * @param sentence
	 * @param clauseSeparators
	 *            a set of inmutable pair of strings
	 * @return
	 */
	public static List<Sentence> extractClausesBySeparators(Sentence sentence, Set<String> separators) {

		List<Sentence> sentences = new ArrayList<>();

		List<List<Token>> clausesToks = null;
		for (String separator : separators) {
			clausesToks = getElementsBySeparator(sentence.getTokens(), separator);
			if (clausesToks.size() != 1) {
				break;
			}
		}

		// ----------------------

		for (List<Token> clauseTokens : clausesToks) {

			String text = clauseTokens.stream().map(Token::getWord).collect(Collectors.joining(" "));
			List<Sentence> subSentences = TextProcessor.processTextFullPipeline(text, false);

			sentences.addAll(subSentences);
		}

		return sentences;
	}

	public static List<List<Token>> getElementsBySeparator(List<Token> tokens, String separator) {

		List<List<Token>> clauses = new ArrayList<>();
		List<Token> clause = new ArrayList<>();
		for (int i = 0; i < tokens.size();) {
			Token token = tokens.get(i);

			String lemma = getLemmas(tokens, i, separator.length());
			if (lemma.equals(separator)) {
				if (!clause.isEmpty()) {
					clauses.add(clause);
				}

				clause = new ArrayList<>();
				i += separator.length();
			} else {
				clause.add(token);
				i++;
			}
		}
		if (!clause.isEmpty()) {
			clauses.add(clause);
		}

		return clauses;
	}

	private static String getLemmas(List<Token> tokens, int i, int length) {

		int toIndex = i + length;
		List<Token> subList;
		if (toIndex < tokens.size()) {
			subList = tokens.subList(i, toIndex);
		} else {
			subList = tokens.subList(i, tokens.size());
		}

		StringBuffer buf = new StringBuffer();
		for (Token token : subList) {
			buf.append(token.getLemma());
		}
		return buf.toString();
	}

	/**
	 * Extract clauses or subsentences in the provided sentence, based on
	 * 2-tokens clause separators
	 * 
	 * @param sentence
	 * @param clauseSeparators
	 *            a set of inmutable pair of strings
	 * @return
	 */
	public static List<Sentence> extractClausesBySeparatorPairs(Sentence sentence,
			Set<ImmutablePair<String, String>> clauseSeparators) {
		List<Token> tokens = sentence.getTokens();

		List<Sentence> clauses = new ArrayList<>();
		List<Token> clauseTokens = new ArrayList<>();

		for (int i = 0; i < tokens.size();) {

			Token token = tokens.get(i);
			Token nextToken = null;
			if (i + 1 < tokens.size()) {
				nextToken = tokens.get(i + 1);
			}

			if (matchTermsByLemma(clauseSeparators, token, nextToken)) {

				if (!clauseTokens.isEmpty()) {

					String text = clauseTokens.stream().map(Token::getWord).collect(Collectors.joining(" "));
					List<Sentence> subSentences = TextProcessor.processTextFullPipeline(text, false);

					clauses.addAll(subSentences);
					clauseTokens = new ArrayList<>();
				}

				i += 2;

			} else {
				clauseTokens.add(token);
				i++;
			}

		}

		if (!clauseTokens.isEmpty()) {

			String text = clauseTokens.stream().map(Token::getWord).collect(Collectors.joining(" "));
			List<Sentence> subSentences = TextProcessor.processTextFullPipeline(text, false);

			clauses.addAll(subSentences);
		}

		return clauses;
	}

	/**
	 * Matches any of the given term pairs with the token and nextToken's lemma
	 * (case ignored)
	 * 
	 * @param clauseSeparators
	 * @param token
	 * @param nextToken
	 * @return
	 */
	private static boolean matchTermsByLemma(Set<ImmutablePair<String, String>> clauseSeparators, Token token,
			Token nextToken) {

		if (token == null || nextToken == null) {
			return false;
		}
		return clauseSeparators.stream().anyMatch(p -> token.getLemma().equalsIgnoreCase(p.getKey())
				&& nextToken.getLemma().equalsIgnoreCase(p.getValue()));
	}

	/**
	 * Extract clauses or subsentences in the provided sentence, based on
	 * CLAUSE_SEPARATORS and Coordinating conjuctions such as "and", "or",
	 * "but", etc.
	 *
	 * @param tokens
	 * @return
	 */
	public static List<Sentence> extractClauses(Sentence sentence, Set<String> clauseSeparators) {
		List<Token> tokens = sentence.getTokens();

		List<Sentence> clauses = new ArrayList<>();
		List<Token> clauseTokens = new ArrayList<>();

		int currentClause = 0;
		for (int i = 0; i < tokens.size();) {
			Token token = tokens.get(i);
			if (matchTermsByLemma(clauseSeparators, token) || matchTermsByPOS(POS_SEPARATORS, token)
					|| matchTermsByLemma(TERM_SEPARATORS, token)) {

				// ---------------------

				boolean allowSplit = true;
				int numNextTokens = 1;

				// avoid splits when there are '->'
				if (token.getLemma().equals("-")
						&& (i + 1 < tokens.size() && tokens.get(i + 1).getLemma().equals(">"))) {
					allowSplit = false;

					// avoid splits when there are html codes: &#x633;
				} else if (token.getLemma().equals("&")) {
					int numToks = checkHtmlCode(tokens, i);
					if (numToks != -1) {
						allowSplit = false;
						numNextTokens = numToks;
					}
				}

				// -----------------------------

				if (allowSplit) {
					if (!clauseTokens.isEmpty()) {
						clauses.add(new Sentence(sentence.getId() + "." + currentClause++, clauseTokens));
						clauseTokens = new ArrayList<>();
					}
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

	/**
	 * Extract clauses or subsentences in the provided sentence, based on
	 * CLAUSE_SEPARATORS and Coordinating conjuctions such as "and", "or",
	 * "but", etc.
	 *
	 * @param tokens
	 * @return
	 */
	public static List<Sentence> extractClauses(Sentence sentence) {
		return extractClauses(sentence, CLAUSE_SEPARATORS);
	}

	private static int checkHtmlCode(List<Token> tokens, int ampersandIdx) {

		int numTextTokens = checkXTokensForHtmlCode(tokens, ampersandIdx, 3);
		if (numTextTokens == -1) {
			numTextTokens = checkXTokensForHtmlCode(tokens, ampersandIdx, 4);
		}

		return numTextTokens;
	}

	private static int checkXTokensForHtmlCode(List<Token> tokens, int ampersandIdx, int numToks) {
		int toIdx = tokens.size();
		if (ampersandIdx + numToks < tokens.size()) {
			toIdx = ampersandIdx + numToks;
		}
		String text = TextProcessor.getStringFromLemmas(new Sentence("0", tokens.subList(ampersandIdx, toIdx)));
		boolean match = text.matches("\\& # \\d+ ;") || text.matches("\\& #[xX] [0-9a-fA-F]+ ;")
				|| text.matches("\\& #[xX][0-9a-fA-F]+ ;");
		if (match) {
			return numToks;
		}
		return -1;
	}

	/**
	 * Matches any of the given POS with the token's POS (case ignored)
	 *
	 * @param pos
	 * @param token
	 * @return true if there is any match, false otherwise
	 */
	public static boolean matchTermsByPOS(Set<String> pos, Token token) {
		return pos.stream().anyMatch(t -> token.getPos().equalsIgnoreCase(t));
	}

	/**
	 * Matches any of the given terms with the token's lemma (case ignored)
	 *
	 * @param terms
	 * @param token
	 * @return true if there is any match, false otherwise
	 */
	public static boolean matchTermsByLemma(Set<String> terms, Token token) {
		return terms.stream().anyMatch(t -> token.getLemma().equalsIgnoreCase(t));
	}

	/**
	 * Matches any of the given terms with the lemma (case ignored)
	 *
	 * @param terms
	 * @param lemma
	 * @return true if there is any match, false otherwise
	 */
	public static boolean matchTermsByLemma(Set<String> terms, String lemma) {
		return terms.stream().anyMatch(t -> t.equalsIgnoreCase(lemma));
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
		return stringContainToken(lemmas, token.getLemma());
	}

	/**
	 * Checks whether the string matches any of the strings in lemmas
	 *
	 * @param lemmas
	 *            set of lemmas to compare against with
	 * @param str
	 *            the string to match
	 * @return true if the string matches any of the lemmas, false otherwise
	 */
	public static boolean stringContainToken(Set<String> lemmas, String str) {
		if (str == null) {
			return false;
		}
		return lemmas.stream().anyMatch(t -> str.equalsIgnoreCase(t));
	}

	/**
	 * Checks whether the token's word matches any of the strings in words
	 *
	 * @param words
	 *            set of words to compare against with
	 * @param token
	 *            the token to match
	 * @return true if the token matches any of the words, false otherwise
	 */
	public static boolean wordsContainToken(Set<String> words, Token token) {
		return words.stream().anyMatch(t -> token.getWord().equalsIgnoreCase(t));
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
			if (lemmasContainToken(lemmas, token)) {
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

	public final static Set<String> UNDETECTED_VERBS = JavaUtils.getSet("boomark", "build", "cache", "change", "check",
			"clic", "click", "copy", "drag", "enter", "file", "fill", "goto", "import", "input", "insert", "install",
			"load", "long-press", "paste", "post", "press", "release", "rename", "return", "right-click", "run",
			"scale", "scroll", "select", "show", "start", "stop", "surf", "tap", "try", "type", "use", "view", "visit",
			"yield");

	public final static Set<String> AMBIGUOUS_POS_VERBS = JavaUtils.getSet("put", "set", "cut", "quit", "shut", "hit");

	/**
	 * Check if the sentence/clause is imperative or not. It takes into account
	 * labels at the beginning of the sentence, such as "exp. behavior: run the
	 * program"
	 *
	 * @param sentence
	 * @return
	 */
	public static boolean isImperativeSentence(Sentence sentence) {
		return isImperativeSentence(sentence.getTokens(), false);
	}

	/**
	 * Check if the sentence/clause (represented by its list of tokens) is
	 * imperative or not. It takes into account labels at the beginning of the
	 * sentence, such as "exp. behavior: run the program"
	 * 
	 * If enableVerbTaggedAsNouns is true, the method tries to detect imperative
	 * sentences when verbs are incorrectly tagged as nouns
	 *
	 * @param tokens
	 * @param enableVerbTaggedAsNouns
	 * @return
	 */
	public static boolean isImperativeSentence(List<Token> tokens, boolean enableVerbTaggedAsNouns) {

		// --------------------------------

		if (checkForImperativeTokens(tokens, enableVerbTaggedAsNouns)) {
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
				return checkForImperativeTokens(tokens.subList(idx + 1, tokens.size()), enableVerbTaggedAsNouns);
			}
		}

		return false;

	}

	/**
	 * Check if the combination list of tokens matches the usual wording for an
	 * imperative sentence: (adverb +) inf. verb + ...
	 * 
	 * If enableVerbTaggedAsNouns is true, the method tries to detect imperative
	 * tokens when verbs are incorrectly tagged as nouns
	 *
	 * @param tokens
	 * @param enableVerbTaggedAsNouns
	 * @return
	 */
	private static boolean checkForImperativeTokens(List<Token> tokens, boolean enableVerbTaggedAsNouns) {

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

		if ((firstToken.getPos().equals("VBN") || firstToken.getPos().equals("VBD") || firstToken.getPos().equals("VBG")
				|| firstToken.getPos().equals("VBZ"))
				&& !(
//						SentenceUtils.lemmasContainToken(UNDETECTED_VERBS, firstToken)||
								SentenceUtils.lemmasContainToken(AMBIGUOUS_POS_VERBS, firstToken))
				) {
			return false;
		}

		// regular case, the sentence starts with a verb
		if ((firstToken.getPos().equals("VB") || firstToken.getPos().equals("VBP"))) {
			return true;
		} else {

			// case: the sentence starts with an adverb/adjective and then with
			// a verb
			if (secondToken != null) {
				if ((firstToken.getPos().equals("RB") || firstToken.getPos().equals("JJ"))
						&& (secondToken.getPos().equals("VB") || secondToken.getPos().equals("VBP")
								|| (secondToken.getPos().equals("NN")
										&& SentenceUtils.wordsContainToken(UNDETECTED_VERBS, secondToken))
								|| SentenceUtils.lemmasContainToken(AMBIGUOUS_POS_VERBS, secondToken))
						&& tokensNoSpecialChar.size() > 2) {
					return true;
				}
			}

			// case: the first token is an undetected verb
			if (SentenceUtils.lemmasContainToken(UNDETECTED_VERBS, firstToken)
					|| SentenceUtils.lemmasContainToken(AMBIGUOUS_POS_VERBS, firstToken)) {
				return true;
			}

			if (enableVerbTaggedAsNouns) {

				// Case: a verb at the beginning is tagged as NN
				Sentence artificialSentence = appendPronoun(tokensNoSpecialChar);

				if (artificialSentence.getTokens().get(1).getPos().equals("VBP")
						&& !artificialSentence.getTokens().get(2).getGeneralPos().equals("VB")) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Appends the pronoun "I" to the beginning of the tokens and reassigns PoS
	 * tags.
	 *
	 * @param tokens
	 *            Tokens to be modified.
	 * @return A re-tagged list of tokens.
	 */
	private static Sentence appendPronoun(List<Token> tokens) {
		String sentenceText = String.join(" ",
				tokens.stream().map(t -> t.getWord().toLowerCase()).toArray(CharSequence[]::new));
		// Appends an "I" to the beginning of the tokens, attempting to nudge
		// the tagger
		// into recognizing an infinitive verb as such.
		String artificialSentenceText = String.format("I %s", sentenceText);

		return SentenceUtils.parseSentence("0", artificialSentenceText);
	}

	// -------------------------------------------------------------------------

	public static final ObservedBehaviorPatternMatcher[] OB_PMS = { new NegativeAuxVerbPM(), new NegativeVerbPM(),
			new ButNegativePM(), new ConditionalNegativePM(), new NegativeAdjOrAdvPM(), new StillSentencePM(),
			new PassiveVoicePM(), new TimeAdverbPositivePM(), new NoticePM(), new NothingHappensPM(),
			new SimplePresentPM() };

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

	/**
	 * Breaks a given sentence based on parenthesis symbols. Thus each sentence
	 * within parenthesis is extracted from the original sentence and added to
	 * the returned list. For example, a sentence such as:
	 *
	 * "On a side note (which may or may not be related), the total number of
	 * potential matches changes."
	 *
	 * is broken into: - which may or may not be related - On a side note, the
	 * total number of potential matches changes.
	 *
	 * @param sentence
	 * @return list of sentences within the given sentence
	 */
	public static List<Sentence> breakByParenthesis(Sentence sentence) {

		List<Sentence> subSentences = new ArrayList<Sentence>();
		Sentence subSentence = new Sentence(sentence.getId(), new ArrayList<Token>());
		List<Token> tokens = sentence.getTokens();

		for (int j = 0; j < tokens.size(); j++) {
			Token current = tokens.get(j);

			// if (current.getLemma().equals(".")) {
			// subSentences.add(subSentence);
			// subSentence = new Sentence(sentence.getId(), new
			// ArrayList<Token>());
			// } else

			// FIXME: break by squared parentheses as well
			if (current.getLemma().equals("-lrb-")) {

				Sentence s1 = closingParSentence(sentence, j + 1);
				int s1size = s1.getTokens().size();

				int end = j + s1size + 1;
				if (end < tokens.size() && tokens.get(end).getLemma().equals("-rrb-")) {
					subSentences.add(s1);
					j = end;
				} else {
					subSentence.addToken(current);
				}
			} else {
				subSentence.addToken(current);
			}
		}

		if (subSentence != null && !subSentence.getTokens().isEmpty()) {
			subSentences.add(subSentence);
		}
		return subSentences;
	}

	private static Sentence closingParSentence(Sentence sentence, int start) {
		Sentence subSentence = new Sentence(sentence.getId(), new ArrayList<Token>());
		List<Token> tokens = sentence.getTokens();
		for (int j = start; j < tokens.size(); j++) {
			Token current = tokens.get(j);

			if (current.getLemma().equals("-rrb-")) {
				return subSentence;
			} else {
				subSentence.addToken(current);
			}
		}
		return subSentence;
	}

	/**
	 * Checks if any of the terms match any of the lemmas of the provided tokens
	 * prior to the token at position tokenIdx
	 *
	 * @param tokenIdx
	 * @param tokens
	 * @param terms
	 * @return true if there is match, false otherwise
	 */
	public static boolean containsTermsPriorToIndex(Integer tokenIdx, List<Token> tokens, Set<String> terms) {
		List<Integer> indexes = SentenceUtils.findLemmasInTokens(terms, tokens);
		return indexes.stream().anyMatch(condIdx -> condIdx < tokenIdx);
	}

	/**
	 * Check if the sentence/clause is imperative or not. It takes into account
	 * labels at the beginning of the sentence, such as "exp. behavior: run the
	 * program".
	 * 
	 * If enableVerbTaggedAsNouns is true, the method tries to detect imperative
	 * sentences when verbs are incorrectly tagged as nouns
	 *
	 * @param sentence
	 * @param enableVerbTaggedAsNouns
	 * @return
	 */
	public static boolean isImperativeSentence(Sentence sentence, boolean enableVerbTaggedAsNouns) {
		return isImperativeSentence(sentence.getTokens(), enableVerbTaggedAsNouns);
	}

	public static boolean matchAnyPattern(Sentence sentence, Set<? extends PatternMatcher> patterns) {
		return patterns.stream().anyMatch(p -> {
			try {
				return p.matchSentence(sentence) != 0;
			} catch (Exception e) {
				throw ExceptionUtils.getRuntimeException(e);
			}
		});
	}

}
