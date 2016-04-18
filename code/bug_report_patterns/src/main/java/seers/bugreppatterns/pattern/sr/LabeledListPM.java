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
		String text = "";
		int stepNumber = 0;
		int count = 0;
		int actionsNumber = 0;
		if (sentences.size() > 1) {
			// verify if there is a numbered or bullet sequences
			for (Sentence sentence : sentences) {
				List<Token> tokens = sentence.getTokens();
				text = TextProcessor.getStringFromLemmas(sentence);
				if (text.matches("^(\\d+|\\-).+")) {
					stepNumber++;
				}
			}
			if (stepNumber != sentences.size()) {
				// it is possible to have numbered item as 1. identified as a
				// sentence
				// it is possible that there is a labeled paragraph with
				// eventually noun actions
				Sentence sentence = sentences.get(0);
				text = TextProcessor.getStringFromLemmas(sentence);
				if (text.matches("reproducible.*")) {
					sentence = sentences.get(1);
					text = TextProcessor.getStringFromLemmas(sentence);
				}
				// case like Repro steps:, reported in the following steps,
				// steps to reproduce, what i have tried:
				boolean b = (text.matches(".*(following|repro) step.*")) || (text.matches(".*?(step)? ?to repro.*"))
						|| (text.equals("step :")) || (text.equals("str :")) || (text.endsWith("have try :"))
						|| (text.contains("here be the step") || (text.matches(".*reproduce as follow :")));
				if (b == false) {
					sentence = sentences.get(1);
					b = (text.matches(".*(following|repro) step.*")) || (text.matches(".*?(step)? ?to repro.*"))
							|| (text.equals("step :")) || (text.equals("str :")) || (text.endsWith("have try :"))
							|| (text.contains("here be the step") || (text.matches(".*reproduce as follow :")));
				}
				if (b) {
					return 1;
				} else {
					for (int i = 0; i < sentences.size(); i++) {
						Sentence s = sentences.get(i);
						List<Token> tokens = s.getTokens();
						text = TextProcessor.getStringFromLemmas(s);
						if (text.matches("^(\\d+|\\-) \\.")) {
							continue;
						} else {
							boolean isAction = false;

							boolean isNoun = isANounPhrase(s);

							if (tokens.size() > 1) {
								if (tokens.get(1).getWord().equals(")")
										&& (tokens.get(2).getGeneralPos().equals("VB"))) {
									isAction = true;
								} else {
									if (text.matches("^(\\d+).*") && (tokens.size() > 2)) {
										isAction = isAnAction(tokens.get(1), tokens.get(2));
									} else {
										isAction = isAnAction(tokens.get(0), tokens.get(1));
									}
								}
								if (isNoun) {
									count++;
								} else if (hasANounTerm(tokens)) {
									count++;
								}
								if (isAction) {
									count++;
								} else {
									for (int y = 0; y < tokens.size(); y++) {
										Token tok = tokens.get(y);
										if (tok.getLemma().matches("\\p{Punct}")) {
											if (y + 1 < tokens.size()) {
												Token firstToken2 = tokens.get(y + 1);
												isAction = isAnAction(firstToken2, null);
												if (isAction) {
													count++;
												}
											}
										}

									}
								}
							}
						}
					}
					if (count > 1) {
						return 1;
					}
				}
			}
			// verify if is P_SR_NUMB_ACTIONS_MULTILINE
			else {
				for (Sentence sen : sentences) {
					List<Token> tokens = sen.getTokens();
					if (tokens.size() > 2) {
						Token toAnalyse = tokens.get(1);
						if (toAnalyse.getWord().equals(":")) {
							toAnalyse = tokens.get(2);
						}
						if (toAnalyse.getGeneralPos().equals("VB") || toAnalyse.getGeneralPos().equals("VBP")
								|| (Arrays.stream(UNDETECTED_VERBS).anyMatch(p -> tokens.get(1).getLemma().equals(p)))
								|| (Arrays.stream(UNDETECTED_VERBS)
										.anyMatch(p -> tokens.get(2).getLemma().equals(p)))) {
							actionsNumber++;
						}
					}
				}
				if (stepNumber > 1 && actionsNumber >= 1) {
					return 1;
				}
			}
			/*
			 * if (verbSentenceNumber>0 || stepNumber>0){ //the paragraph will
			 * start with a sentence likes steps to reproduce Sentence sentence
			 * = sentences.get(0);
			 * text=TextProcessor.getStringFromLemmas(sentence); if
			 * (text.matches("reproducible.*")) { sentence = sentences.get(1);
			 * text = TextProcessor.getStringFromLemmas(sentence); } //case like
			 * Repro steps:, reported in the following steps, steps to
			 * reproduce, what i have tried: boolean b = (text.matches(
			 * ".*(following|repro) step.*")) || (text.matches(
			 * ".*?(step)? ?to repro.*")) || (text.equals("step :")) ||
			 * (text.equals("str :")) || (text.endsWith("have try :")) ||
			 * (text.contains("here be the step")); if(b==false){
			 * sentence=sentences.get(1); b=(text.matches(
			 * ".*(following|repro) step.*")) || (text.matches(
			 * ".*?(step)? ?to repro.*")) || (text.equals("step :")) ||
			 * (text.equals("str :")) || (text.endsWith("have try :"))||
			 * (text.contains("here be the step")); } if(b){ return 1; }
			 * 
			 * } if(stepNumber>0){ return 1; }
			 */
		}

		return 0;
	}

	public boolean isANounPhrase(Sentence sentence) {
		List<Token> tokens = sentence.getTokens();
		if (tokens.size() > 2) {
			for (Token token : tokens) {
				if (token.getGeneralPos().equals("VB")) {
					return false;
				}
			}
		}
		return true;
	}

	final private static String[] UNDETECTED_VERBS = { "show", "boomark", "rename", "run", "select", "post", "stop",
			"goto", "enter" };

	public boolean isAnAction(Token firstToken, Token secondToken) {

		if (firstToken.getPos().equals("VB") || firstToken.getPos().equals("VBP")) {
			return true;
		} else {
			if (secondToken != null) {

				if (firstToken.getPos().equals("RB")
						&& (secondToken.getPos().equals("VB") || secondToken.getPos().equals("VBP"))) {
					return true;
				}
			}
			if (Arrays.stream(UNDETECTED_VERBS).anyMatch(p -> firstToken.getLemma().equals(p))) {
				return true;
			}
		}
		return false;
	}

	final static String[] NOUNS_TERM = { "page", "error", "exception", "warning", "message", "grief", "use" };

	public boolean hasANounTerm(List<Token> tokens) {
		for (Token token : tokens) {
			if (token.getGeneralPos().equals("NN")) {
				if (Arrays.stream(NOUNS_TERM).anyMatch(t -> t.equals(token.getLemma()))) {
					return true;
				}
			}

		}

		return false;
	}
}
