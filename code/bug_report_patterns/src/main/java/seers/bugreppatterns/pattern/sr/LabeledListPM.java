package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.appcore.utils.JavaUtils;
import seers.bugreppatterns.utils.SentenceUtils;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class LabeledListPM extends StepsToReproducePatternMatcher {

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		return 0;
	}

	@Override
	public int matchParagraph(Paragraph paragraph) throws Exception {

		List<Sentence> sentences = paragraph.getSentences();

		int numSentences = 0;
		int bulletedSentences = 0;

		int labelIdx = getLabelIndex(sentences);

		// no label
		if (labelIdx == -1) {
			return 0;
		}

		for (int i = labelIdx + 1; i < sentences.size(); i++) {
			Sentence sentence = sentences.get(i);
			List<Token> tokensNoBullet = getTokensNoBullet(sentence);

			if (tokensNoBullet.isEmpty()) {
				continue;
			}

			bulletedSentences++;
			if (checkImperativeClauses(tokensNoBullet) || isANounPhrase(tokensNoBullet)
					|| startsWithNounPhrase(tokensNoBullet) || isPresentTense(tokensNoBullet)
					|| isPastTenseAction(tokensNoBullet)) {
				numSentences++;
			}
		}

		// System.out.println(numSentences +"-"+bulletedSentences);

		int match = ((float) numSentences) / bulletedSentences >= 0.5F ? 1 : 0;
		return match;
	}

	private boolean checkImperativeClauses(List<Token> tokensNoBullet) {
		List<Sentence> clauses = SentenceUtils.extractClauses(new Sentence("0", tokensNoBullet));
		for (Sentence clause : clauses) {
			if (SentenceUtils.isImperativeSentence(clause)) {
				return true;
			}
		}
		return false;
	}

	private boolean isPastTenseAction(List<Token> tokensNoBullet) {
		if (tokensNoBullet.size() > 1) {
			Token firstToken = tokensNoBullet.get(0);
			return firstToken.getPos().equals("VBD") || firstToken.getPos().equals("VBN");
		}
		return false;
	}

	private boolean isPresentTense(List<Token> tokensNoBullet) {
		return ActionsPresentPM.isSimplePresentSentence(new Sentence("-1", tokensNoBullet));
	}

	/**
	 * Removes the bullet tokens from the sentence
	 * 
	 * @param sentence
	 * @return the list of tokens of the sentence with no bullets, or an empty
	 *         list if it does not contain any bullet
	 */
	public static List<Token> getTokensNoBullet(Sentence sentence) {

		String text = TextProcessor.getStringFromLemmas(sentence);
		List<Token> tokens = sentence.getTokens();
		List<Token> tokensNoBullet = new ArrayList<>();

		// no parentheses
		if (!text.matches("^(-lcb-|-rcb-|-lrb-|-rrb-|-lsb-|-rsb-) \\D+ .+")) {

			// cases like: 1 -
			if (text.matches("^(\\d+ \\-+).+")) {
				tokensNoBullet = tokens.subList(2, tokens.size());

			} else
			// cases like: [1], (1), {1}
			// ---------------
			if (text.matches("^(-lsb- \\d+(\\w+)? -rsb-).+")) {

				tokensNoBullet = tokens.subList(3, tokens.size());

			} else if (text.matches("^(-lcb- \\d+(\\w+)? -rcb-).+")) {

				tokensNoBullet = tokens.subList(3, tokens.size());

			} else if (text.matches("^(-lrb- \\d+(\\w+)? -lsb-).+")) {

				tokensNoBullet = tokens.subList(3, tokens.size());

			} else
			// ---------------
			// cases like: 1. or -
			if (text.matches("^(\\d+|\\-|\\*).+")) {
				tokensNoBullet = tokens.subList(1, tokens.size());

			} else
			// cases like: step1 :
			if (text.matches("^[a-zA-Z]+\\d+ :.*")) {
				tokensNoBullet = tokens.subList(2, tokens.size());
			}
		}

		return tokensNoBullet;
	}

	private static final Set<String> UNDETECTED_LABELS = JavaUtils.getSet("step to reproduce", "step to repro",
			"reproduce step", "step by step :", "str :", "s2r :", "bulleted list bug :",
			"- step to replicate on the app", "reproduce as follow :", "what I have try :", "a similar bug :",
			"here be the step :", "reproduction step :");

	private static final String REGEX_ENDING_CHAR = "(:|\\.|\\-|\\(|#)";

	public static int getLabelIndex(List<Sentence> sentences) {
		for (int i = 0; i < sentences.size(); i++) {
			String text = TextProcessor.getStringFromLemmas(sentences.get(i));
			boolean b = text.matches(
					"(?s).*step( how)? to (reproduce|recreate|create|replicate)( the (problem|issue|behavior|bug))? "
							+ REGEX_ENDING_CHAR + ".*")
					|| text.matches("(?s)" + REGEX_ENDING_CHAR
							+ "+ ?step( how)? to (reproduce|recreate|create|replicate)( the (problem|issue|behavior|bug))?( ?"
							+ REGEX_ENDING_CHAR + "+)?")
					|| text.matches("(step|repro|repro step|step to repro) " + REGEX_ENDING_CHAR)
					|| text.matches("(how )?to (reproduce|reporduce|recreate|replicate) " + REGEX_ENDING_CHAR + "+")
					|| text.matches(
							"(?s)step( how)? to (reproduce|recreate|create|replicate)( the (problem|issue|behavior|bug))? with.*")
					|| text.matches("(?s).+follow(ing)? (scenario|step) :");

			if (b) {
				return i;
			} else {

				b = UNDETECTED_LABELS.stream().anyMatch(label -> text.equalsIgnoreCase(label));
				if (b) {
					return i;
				}
			}
		}
		return -1;
	}

	public boolean isANounPhrase(List<Token> tokens) {

		for (int i = 1; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {
				return false;
			}
		}

		return tokens.stream().anyMatch(tok -> tok.getGeneralPos().equals("NN") && !tok.getLemma().equals("..."));
	}

	public boolean startsWithNounPhrase(List<Token> tokens) throws Exception {

		Token firstToken = tokens.get(0);

		if (tokens.size() == 1) {
			if (firstToken.getGeneralPos().equals("NN")) {
				return true;
			}
		} else {

			Token secondToken = tokens.get(1);

			if (firstToken.getGeneralPos().equals("JJ") && secondToken.getGeneralPos().equals("VB")) {
				return true;
			}
			if ((firstToken.getGeneralPos().equals("JJ") && secondToken.getGeneralPos().equals("NN"))
					|| (firstToken.getGeneralPos().equals("NN") && secondToken.getGeneralPos().equals("JJ"))) {
				return true;
			}
		}
		return false;
	}

	public static boolean isParagraphLabeled(List<Sentence> sentences) {
		int labelIdx = getLabelIndex(sentences);

		// there is label
		if (labelIdx != -1) {
			return true;

		}
		return false;
	}
}
