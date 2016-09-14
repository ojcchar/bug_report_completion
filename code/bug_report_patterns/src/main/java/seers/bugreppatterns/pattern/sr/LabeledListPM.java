package seers.bugreppatterns.pattern.sr;

import java.util.ArrayList;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
import seers.bugreppatterns.pattern.eb.ImperativeSentencePM;
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
		int numbActionNoLabel = 0;
		if (sentences.size() > 1) {
			int labelIdx = getLabelIndex(sentences);

			// there is label
			if (labelIdx != -1) {
				for (int i = labelIdx + 1; i < sentences.size(); i++) {
					Sentence sentence = sentences.get(i);
					List<Token> tokensNoBullet = getTokensNoBullet(sentence);

					if (tokensNoBullet.isEmpty()) {
						continue;
					}

					if (isAnAction(tokensNoBullet) || isANounPhrase(tokensNoBullet)
							|| startsWithNounPhrase(tokensNoBullet) || isPresentTense(tokensNoBullet)) {
						return 1;
					}
				}
			} else {
				for (int i = 0; i < sentences.size(); i++) {
					Sentence sentence = sentences.get(i);

					List<Token> tokensNoBullet = getTokensNoBullet(sentence);

					if (tokensNoBullet.isEmpty()) {
						continue;
					}

					if (isAnAction(tokensNoBullet) || isANounPhrase(tokensNoBullet)
							|| startsWithNounPhrase(tokensNoBullet) || isPresentTense(tokensNoBullet)) {
						numbActionNoLabel++;
						if (numbActionNoLabel > 1) {
							return 1;
						}
					}

				}
			}
		}
		return 0;
	}

	private boolean isPresentTense(List<Token> tokensNoBullet) {
		return ActionsPresentPM.isActionInPresent(new Sentence("-1", tokensNoBullet), true) == 1;
	}

	/**
	 * Removes the bullet tokens from the sentence
	 * 
	 * @param sentence
	 * @return the list of tokens of the sentence with no bullets, or an empty list if it does not contain any bullet
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

				// cases like: 1. or -
			} else if (text.matches("^(\\d+|\\-|\\*).+")) {
				tokensNoBullet = tokens.subList(1, tokens.size());
			}
		}

		return tokensNoBullet;
	}

	public static int getLabelIndex(List<Sentence> sentences) {
		for (int i = 0; i < sentences.size(); i++) {
			String text = TextProcessor.getStringFromLemmas(sentences.get(i));
			boolean b = text.matches("(?s).*(following|repro) step.*")
					|| text.matches("(?s).*?(step)? ?to (reproduce|recreate)( the (problem|issue))?.*")
					|| text.equals("step :") || text.equals("step by step :") || text.equals("str :")
					|| text.endsWith("have try :") || text.contains("here be the step")
					|| text.matches("(?s).*reproduce as follow :") || text.contains("follow scenario :")
					|| text.equals("to reproduce :") || text.equals("to reporduce :") || text.equals("step to repro :")
					|| text.equals("repro :");
			if (b) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isAnAction(List<Token> tokens) throws Exception {

		Token firstToken = tokens.get(0);
		Token secondToken = null;

		if (tokens.size() > 1) {
			secondToken = tokens.get(1);
		}

		if (firstToken.getGeneralPos().equals("VB") || firstToken.getPos().equals("VBP")
				|| firstToken.getGeneralPos().equals("MD")) {
			return true;
		} else {
			if (secondToken != null) {

				if (firstToken.getPos().equals("RB")
						&& (secondToken.getGeneralPos().equals("VB") || secondToken.getPos().equals("VBP"))) {
					return true;
				}
			}
			if (SentenceUtils.lemmasContainToken(ImperativeSentencePM.UNDETECTED_VERBS, firstToken)) {
				return true;
			}
		}
		return false;

	}

	public boolean isANounPhrase(List<Token> tokens) {

		for (int i = 1; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {
				return false;
			}
		}
		return true;
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

	public boolean isParagraphLabeled(List<Sentence> sentences) {
		int labelIdx = getLabelIndex(sentences);

		// there is label
		if (labelIdx != -1) {
			return true;

		}
		return false;
	}
}
