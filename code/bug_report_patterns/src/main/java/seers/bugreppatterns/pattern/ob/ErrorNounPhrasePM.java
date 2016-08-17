package seers.bugreppatterns.pattern.ob;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seers.bugreppatterns.pattern.ObservedBehaviorPatternMatcher;
import seers.textanalyzer.entity.Sentence;
import seers.textanalyzer.entity.Token;

public class ErrorNounPhrasePM extends ObservedBehaviorPatternMatcher {

	public final static String[] FALSE_VERBS = { "build", "duplicate", "displaying", "encode", "freeze", "httpd",
			"misplaced", "miss", "orphan", "stack", "truncate", };

	@Override
	public int matchSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
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

		return 0;
	}

	private int matchSubSentence(Sentence sentence) throws Exception {
		List<Token> tokens = sentence.getTokens();
		ArrayList<Integer> indexVerb = new ArrayList<Integer>();

		boolean noVerb = true;
		for (int i = 0; i < tokens.size(); i++) {
			Token token = tokens.get(i);
			if (token.getGeneralPos().equals("VB")) {
				noVerb = false;
				indexVerb.add(i);
			}
		}
		// find Noun_Phrase with error terms
		if (noVerb) {
			if (checkErrorNounPhrase(tokens) == 1) {
				return 1;
			}
			// verify that is not an Error how sentence
		} else {
			for (int i = 0; i < indexVerb.size(); i++) {
				Token token = tokens.get(indexVerb.get(i));
				if (Arrays.stream(FALSE_VERBS).anyMatch(t -> token.getWord().toLowerCase().contains(t))) {
					return 1;
				}
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

	private int checkErrorNounPhrase(List<Token> tokens) {
		for (Token token : tokens) {
			if (Arrays.stream(NegativeTerms.NOUNS).anyMatch(t -> token.getLemma().contains(t))
					&& token.getGeneralPos().equals("NN")) {
				return 1;
			} else if (Arrays.stream(NegativeTerms.ADJECTIVES).anyMatch(t -> token.getLemma().contains(t))
					&& (token.getGeneralPos().equals("JJ") || token.getGeneralPos().equals("NN"))) {
				return 1;
			}
		}
		return 0;
	}

}
