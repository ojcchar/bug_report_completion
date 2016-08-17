package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.TextProcessor;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorNounPhrasePM extends ObservedBehaviorPatternMatcher {

	public final static String[] FALSE_VERBS = { "build", "httpd" };

	private final static String[] SUBJECTS = { "NN", "VB", "DT", "RB", "EX", "IN" };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();

		// check for no prepositions -> ProblemInPM case
		if (ProblemInPM.findPrepositions(tokens).isEmpty()) {

			// divide sentence into phrases and analyze them
			List<Integer> symbols = findSemiColonOrParenthesis(tokens);

			if (symbols.isEmpty()) {
				return matchSubSentence(sentence);
			} else {
				boolean match = false;
				for (int i = 0; i <= symbols.size(); i++) {
					int start = i == 0 ? 0 : symbols.get(i - 1) + 1;
					int end = i == symbols.size() ? tokens.size() : symbols.get(i);
					Sentence sentence2 = new Sentence(sentence.getId(), tokens.subList(start, end));
					match = match || matchSubSentence(sentence2) == 1 ? true : false;
				}
				if (match) {
					return 1;
				}
			}
		}
		return 0;
	}

	private int matchSubSentence(Sentence sentence) throws Exception {
		//System.out.println(sentence);
		List<Token> tokens = sentence.getTokens();
		ArrayList<Integer> indexVerb = new ArrayList<Integer>();

		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {
				indexVerb.add(i);
			}
		}
		// find Noun_Phrase with error terms
		if (indexVerb.isEmpty()) {
			if (checkErrorNounPhrase(tokens) == 1) {
				return 1;
			}
			// verify that is not an Error how sentence
		} else {
			boolean matches = true;
			for (int i = 0; i < indexVerb.size(); i++) {

				Token token = tokens.get(indexVerb.get(i));

				if (indexVerb.get(i) == 0) {
					matches = matches && true;
				} else {
					Token previousToken = tokens.get(indexVerb.get(i) - 1);
					if (token.getPos().equals("VBG") && !previousToken.getGeneralPos().equals("VB")) {
						matches = matches && true;
					} else {
						if (Arrays.stream(SUBJECTS).anyMatch(t -> previousToken.getGeneralPos().equals(t))) {
							matches = matches && false;
						}
					}
				}
				if (Arrays.stream(FALSE_VERBS).anyMatch(t -> token.getWord().toLowerCase().equals(t))) {
					matches = matches || true;
				}
			}
			if (matches) {
				return 1;
			}
		}
		return 0;
	}

	private List<Integer> findSemiColonOrParenthesis(List<Token> tokens) {
		List<Integer> symbols = new ArrayList<>();
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getWord().equals(":") || token.getWord().equals("-LRB-") || token.getWord().equals("-RRB-")) {
				symbols.add(i);
			}
		}
		return symbols;
	}

	public static int checkErrorNounPhrase(List<Token> tokens) {
		//System.out.println(new Sentence(OB, tokens));
		int i = 0;
		for (Token token : tokens) {
			if (Arrays.stream(NegativeTerms.NOUNS).anyMatch(t -> token.getLemma().startsWith(t))
					&& (token.getGeneralPos().equals("NN") || token.getGeneralPos().equals("VB")
							|| token.getGeneralPos().equals("CD"))) {
				return 1;
			} else if (token.getLemma().matches("([A-Za-z0-9.]+)(exception)") && token.getGeneralPos().equals("NN")) {
				return 1;
			} else if (token.getLemma().matches("([A-Za-z0-9.]+)(error)") && token.getGeneralPos().equals("NN")) {
				return 1;
			} else if (Arrays.stream(NegativeTerms.ADJECTIVES).anyMatch(t -> token.getLemma().startsWith(t))
					&& (token.getGeneralPos().equals("JJ") || token.getGeneralPos().equals("NN")
							|| token.getGeneralPos().equals("RB") || token.getGeneralPos().equals("VB"))) {
				return 1;
			} else if (token.getLemma().equals("stack") && (i + 1 < tokens.size())
					&& tokens.get(i).getLemma().equals("trace")) {
				return 1;
			}

			i++;
		}
		
		return 0;
	}

}
