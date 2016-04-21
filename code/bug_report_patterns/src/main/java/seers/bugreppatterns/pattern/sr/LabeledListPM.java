package seers.bugreppatterns.pattern.sr;

import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.entity.Paragraph;
import seers.bugreppatterns.pattern.StepsToReproducePatternMatcher;
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
		boolean isLabeled = false;
		if (sentences.size() > 1) {
			isLabeled = isParagraphLabeled(sentences);

			if (isLabeled) {
				for (int i = 0; i < sentences.size(); i++) {
					List<Token> tokens = sentences.get(i).getTokens();
					String text = TextProcessor.getStringFromLemmas(sentences.get(i));
					if (tokens.size() >= 3) {
						if (text.matches("^(\\d+|\\-).+")) {
							if (isAnAction(tokens.get(1), tokens.get(2)) || isANounPhrase(tokens)
									|| (startsWithNounPhrase(tokens.get(1), tokens.get(2)))) {
								return 1;
							}
						} else if ((isAnAction(tokens.get(0), tokens.get(1)) || isANounPhrase(tokens)
								|| (startsWithNounPhrase(tokens.get(1), tokens.get(2))))) {
							return 1;
						}
					}
				}
			} else {
				for (int i = 0; i < sentences.size(); i++) {
					String text = TextProcessor.getStringFromLemmas(sentences.get(i));
					List<Token> tokens = sentences.get(i).getTokens();
					if (tokens.size() >= 3) {
						if (text.matches("^(\\d+|\\-).+")) {
							if (isAnAction(tokens.get(1), tokens.get(2)) || isANounPhrase(tokens)) {
								numbActionNoLabel++;
							}
						}
					}
				}
			}

			if (numbActionNoLabel > 1) {
				return 1;
			}
		}
		return 0;
	}

	public boolean isParagraphLabeled(List<Sentence> sentences) {
		for (int i = 0; (i < sentences.size() - 1); i++) {
			String text = TextProcessor.getStringFromLemmas(sentences.get(i));
			boolean b = (text.matches("(?s).*(following|repro) step.*")) || (text.matches("(?s).*?(step)? ?to repro.*"))
					|| (text.equals("step :")) || (text.equals("str :")) || (text.endsWith("have try :"))
					|| (text.contains("here be the step") || (text.matches("(?s).*reproduce as follow :")));
			if (b) {
				return true;
			}
		}
		return false;
	}

	final private static String[] UNDETECTED_VERBS = { "show", "boomark", "rename", "run", "select", "post", "stop",
			"goto", "enter", "drag", "check", "file", "try", "build", "install", "type", "use" };

	public static boolean isAnAction(Token firstToken, Token secondToken) {

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
			if (Arrays.stream(UNDETECTED_VERBS).anyMatch(p -> firstToken.getLemma().equals(p))) {
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

	public boolean startsWithNounPhrase(Token first, Token second) {
		if (first.getGeneralPos().equals("JJ") && second.getGeneralPos().equals("VB")) {
			return true;
		}
		if ((first.getGeneralPos().equals("JJ") && second.getGeneralPos().equals("NN"))
				|| (first.getGeneralPos().equals("NN") && second.getGeneralPos().equals("JJ"))) {
			return true;
		}
		return false;
	}
}
