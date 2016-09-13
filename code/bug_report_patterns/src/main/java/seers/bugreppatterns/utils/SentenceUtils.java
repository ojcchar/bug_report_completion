package seers.bugreppatterns.utils;

import java.util.List;

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

}
